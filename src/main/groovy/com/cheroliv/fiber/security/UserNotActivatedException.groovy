package com.cheroliv.fiber.security

import groovy.transform.CompileStatic
import org.springframework.security.core.AuthenticationException

@CompileStatic
class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L

    UserNotActivatedException(String message) {
        super(message)
    }

    UserNotActivatedException(String message, Throwable t) {
        super(message, t)
    }
}
