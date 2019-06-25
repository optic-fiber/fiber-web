package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.security.DomainUserDetailsService
import com.cheroliv.fiber.service.UserService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@CompileStatic
@RestController
@RequestMapping("users")
class UserController {
    final UserService userService
    final DomainUserDetailsService userDetailsService

    @Autowired
    UserController(UserService userService,
                   DomainUserDetailsService userDetailsService) {
        this.userService = userService
        this.userDetailsService = userDetailsService
    }


}
