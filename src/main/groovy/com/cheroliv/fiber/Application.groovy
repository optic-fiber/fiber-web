package com.cheroliv.fiber

import com.cheroliv.fiber.config.Config
import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.repository.AuthorityRepository
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.security.AuthoritiesConstants
import com.cheroliv.fiber.security.UserDTO
import com.cheroliv.fiber.service.UserService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

import javax.annotation.PostConstruct
import javax.transaction.Transactional

@CompileStatic
@Slf4j
@SpringBootApplication
@Import([Config.class])
class Application {
    static void main(String[] args) {
        log.info "running spring boot"
        SpringApplication.run(Application, args)
    }
    @Autowired
    UserService userService
    @Autowired
    AuthorityRepository authorityRepository
    @Autowired
    UserRepository userRepository

    @Transactional
    @PostConstruct
    void createDefaultUsers() {
        if (!authorityRepository.findById(AuthoritiesConstants.USER).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.USER))
        if (!authorityRepository.findById(AuthoritiesConstants.ADMIN).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.ADMIN))
        if (!authorityRepository.findById(AuthoritiesConstants.ANONYMOUS).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.ANONYMOUS))

        Optional<User> optionalAdminUser = userService.getUserWithAuthoritiesByLogin "admin"
        if (!optionalAdminUser.present) {
            userService.registerUser(new UserDTO(new User(
                    login: "admin",
                    firstName: "admin",
                    lastName: "admin",
                    email: "admin@localhost")),
                    "admin")
            User user = userRepository.findOneByLogin("admin")?.get()
            user.activated = true
            user.authorities = [authorityRepository.findById(AuthoritiesConstants.USER)?.get(),
                                authorityRepository.findById(AuthoritiesConstants.ADMIN)?.get()] as Set
            userRepository.save(user)
        }
        Optional<User> optionalUser = userService.getUserWithAuthoritiesByLogin("user")
        if (!optionalUser.present) {
            Set authorities = [new Authority(name: AuthoritiesConstants.USER)] as Set
            userService.registerUser(new UserDTO(new User(
                    login: "user",
                    firstName: "user",
                    lastName: "user",
                    email: "user@localhost",
                    authorities: authorities)),
                    "user")
            User user = userRepository.findOneByLogin("user")?.get()
            user.activated = true
            userRepository.save(user)
        }
        Optional<User> optionalSystemUser = userService.getUserWithAuthoritiesByLogin("system")
        if (!optionalUser.present) {
            Set authorities = [new Authority(name: AuthoritiesConstants.ADMIN),
                               new Authority(name: AuthoritiesConstants.USER)] as Set
            userService.registerUser(new UserDTO(new User(
                    login: "system",
                    firstName: "system",
                    lastName: "system",
                    email: "system@localhost",
                    authorities: authorities)),
                    "system")
            User user = userRepository.findOneByLogin("system")?.get()
            user.activated = true
            userRepository.save(user)
        }

    }
}
