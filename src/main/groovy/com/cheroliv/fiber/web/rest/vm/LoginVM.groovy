package com.cheroliv.fiber.web.rest.vm

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * View Model object for storing a user's credentials.
 */
class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    String username

    @NotNull
    @Size(min = 4, max = 100)
    String password

    Boolean rememberMe

    @Override
    String toString() {
        return "LoginVM{" +
                "username='" + username + '\'' +
                ", rememberMe=" + rememberMe +
                '}'
    }
}
