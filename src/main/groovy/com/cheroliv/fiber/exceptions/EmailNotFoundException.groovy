package com.cheroliv.fiber.exceptions

import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Status

class EmailNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L

    EmailNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Email address not registered", Status.BAD_REQUEST)
    }
}
