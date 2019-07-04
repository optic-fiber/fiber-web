package com.cheroliv.fiber.service

import com.cheroliv.fiber.domain.User

interface MailService {

    static final String USER = "user"
    static final String BASE_URL = "baseUrl"
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml)


    void sendEmailFromTemplate(User user, String templateName, String titleKey)


    void sendActivationEmail(User user)


    void sendCreationEmail(User user)


    void sendPasswordResetMail(User user)

}
