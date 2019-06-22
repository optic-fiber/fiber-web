package com.cheroliv.fiber.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L

    LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists")
    }
}
