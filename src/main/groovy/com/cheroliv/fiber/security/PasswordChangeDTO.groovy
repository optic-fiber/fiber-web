package com.cheroliv.fiber.security

import groovy.transform.CompileStatic

@CompileStatic
class PasswordChangeDTO {
     String currentPassword
     String newPassword

    PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword
        this.newPassword = newPassword
    }
}
