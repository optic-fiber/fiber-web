package com.cheroliv.fiber.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L

    EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists")
    }
}
