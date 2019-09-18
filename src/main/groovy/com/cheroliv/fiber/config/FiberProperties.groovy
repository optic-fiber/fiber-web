package com.cheroliv.fiber.config


import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(
        prefix = 'fiber',
        ignoreUnknownFields = false)
class FiberProperties {


    String applicationName = "fiber"
    String classeurPathname = "output/recapClasseur.xlsx"
    final Swagger clientApp = new Swagger()


    static class Swagger {
        String defaultIncludePattern='/api/.*'
    }

}

final class PropTodo{
    def properties =
            """
fiber.user-data-foldername =.fiber
fiber.json-backup-filename=inter.json
fiber.mail.from=fiberweb.contact@gmail.com
fiber.mail.base-url=

fiber.swagger.default-include-pattern=/api/.*
fiber.swagger.title=fiber-web API
fiber.swagger.description=fiber-web API documentation
fiber.swagger.version=0.0.1
fiber.swagger.terms-of-service-url=
fiber.swagger.contact-name=
fiber.swagger.contact-url=
fiber.swagger.contact-email=
fiber.swagger.license=
fiber.swagger.license-url=
fiber.security.authentication.jwt.base64-secret=YjUwZGQ3Y2NmYmY5MzRmMDI0MmU3MGVlOWU5NzZmMzgzYzIyYWViZjcwYzRhNGU0MjkxMWViNmMzZDA5ZDgyZTNhMDEzMTYwMjAxNTVkMTdkYjc2MWNkOWI2NmIzNTQ3ZGM5YjVhZTk0YmJjODI0MWQ1NTg4NjRiYWM1NDk4ZTA=
fiber.security.authentication.jwt.token-validity-in-seconds=86400
fiber.security.authentication.jwt.token-validity-in-seconds-for-remember-me=2592000
"""
}