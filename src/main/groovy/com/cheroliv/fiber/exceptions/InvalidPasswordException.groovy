package com.cheroliv.fiber.exceptions

import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Status

class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L

    InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST)
    }
}

