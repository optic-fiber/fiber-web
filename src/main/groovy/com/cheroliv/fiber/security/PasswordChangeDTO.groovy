package com.cheroliv.fiber.security

class PasswordChangeDTO {
    private String currentPassword
    private String newPassword

    PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword
        this.newPassword = newPassword
    }
}
