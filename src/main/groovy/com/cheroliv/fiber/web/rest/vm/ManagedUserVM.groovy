package com.cheroliv.fiber.web.rest.vm


import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.dto.UserDTO
import groovy.transform.CompileStatic

import javax.validation.constraints.Size

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
@CompileStatic
class ManagedUserVM extends UserDTO {

    static final int PASSWORD_MIN_LENGTH = 4

    static final int PASSWORD_MAX_LENGTH = 100

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    String password


    ManagedUserVM(User user) {
        super(user)
    }

    @Override
    String toString() {
        return "ManagedUserVM{" +
                "} " + super.toString()
    }
}
