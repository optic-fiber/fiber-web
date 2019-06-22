package com.cheroliv.fiber.security

import org.springframework.security.core.AuthenticationException

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = 1L

    UserNotActivatedException(String message) {
        super(message)
    }

    UserNotActivatedException(String message, Throwable t) {
        super(message, t)
    }
}
