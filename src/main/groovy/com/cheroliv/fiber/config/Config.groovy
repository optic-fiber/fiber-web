package com.cheroliv.fiber.config

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@CompileStatic
@Configuration
//@EnableFeignClients(basePackages = "com.cheroliv.fiber")
//@Import([FeignClientsConfiguration.class,
//        SecurityConfiguration.class,
//        WebConfigurer.class])
@ComponentScan("com.cheroliv.fiber")
class Config {
    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }
}
