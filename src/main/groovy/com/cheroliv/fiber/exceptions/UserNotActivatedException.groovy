package com.cheroliv.fiber.exceptions

import groovy.transform.CompileStatic
import org.springframework.security.core.AuthenticationException

@CompileStatic
class UserNotActivatedException extends AuthenticationException {

    static final long serialVersionUID = 1L

    UserNotActivatedException(String message) {
        super(message)
    }

    UserNotActivatedException(String message, Throwable t) {
        super(message, t)
    }
}
