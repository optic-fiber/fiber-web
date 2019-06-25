package com.cheroliv.fiber.config

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.repository.AuthorityRepository
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.security.AuthoritiesConstants
import com.cheroliv.fiber.security.UserDTO
import com.cheroliv.fiber.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Component
class DatabaseInitializer {
    final UserService userService
    final AuthorityRepository authorityRepository
    final UserRepository userRepository

    @Autowired
    DatabaseInitializer(UserService userService,
                        AuthorityRepository authorityRepository,
                        UserRepository userRepository) {
        this.userService = userService
        this.authorityRepository = authorityRepository
        this.userRepository = userRepository
    }


    @PostConstruct
    @Transactional
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
            userService.registerUser(new UserDTO(new User(
                    login: "user",
                    firstName: "user",
                    lastName: "user",
                    email: "user@localhost")),
                    "user")
            User user = userRepository.findOneByLogin("user")?.get()
            user.activated = true
            user.authorities = [authorityRepository.findById(AuthoritiesConstants.USER)?.get()] as Set
            userRepository.save(user)
        }
        Optional<User> optionalSystemUser = userService.getUserWithAuthoritiesByLogin("system")
        if (!optionalSystemUser.present) {
            userService.registerUser(new UserDTO(new User(
                    login: "system",
                    firstName: "system",
                    lastName: "system",
                    email: "system@localhost")),
                    "system")
            User user = userRepository.findOneByLogin("system")?.get()
            user.activated = true
            user.authorities = [authorityRepository.findById(AuthoritiesConstants.ADMIN)?.get(),
                                authorityRepository.findById(AuthoritiesConstants.USER)?.get()] as Set
            userRepository.save(user)
        }
        Optional<User> optionalAnonymousUser = userService.getUserWithAuthoritiesByLogin("anonymoususer")
        if (!optionalAnonymousUser.present) {
            userService.registerUser(new UserDTO(new User(
                    login: "anonymoususer",
                    firstName: "anonymoususer",
                    lastName: "anonymoususer",
                    email: "anonymoususer@localhost")),
                    "anonymoususer")
            User user = userRepository.findOneByLogin("anonymoususer")?.get()
            user.activated = true
            user.authorities = [] as Set
            userRepository.save(user)
        }

    }
}
