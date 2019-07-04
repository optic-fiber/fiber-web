package com.cheroliv.fiber.service

import com.cheroliv.fiber.config.FiberProperties
import com.cheroliv.fiber.domain.User
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

import javax.mail.internet.MimeMessage
import java.nio.charset.StandardCharsets


@Slf4j
@Service
@CompileStatic
class MailServiceImpl implements MailService {



    final FiberProperties jHipsterProperties = FiberProperties.instance
    final JavaMailSender javaMailSender
    final MessageSource messageSource
    final SpringTemplateEngine templateEngine

    @Autowired
    MailServiceImpl(
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender
        this.messageSource = messageSource
        this.templateEngine = templateEngine
    }

    @Async
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content)

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage()
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name())
            message.setTo(to)
            message.setFrom(jHipsterProperties.getMail().getFrom())
            message.setSubject(subject)
            message.setText(content, isHtml)
            javaMailSender.send(mimeMessage)
            log.debug("Sent email to User '{}'", to)
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e)
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage())
            }
        }
    }

    @Async
    void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey())
        Context context = new Context(locale)
        context.setVariable(USER, user)
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl())
        String content = templateEngine.process(templateName, context)
        String subject = messageSource.getMessage(titleKey, null, locale)
        sendEmail(user.getEmail(), subject, content, false, true)
    }

    @Async
    void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail())
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title")
    }

    @Async
    void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail())
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title")
    }

    @Async
    void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail())
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title")
    }
}
