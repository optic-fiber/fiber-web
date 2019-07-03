package com.cheroliv.fiber.config


import groovy.transform.CompileStatic
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@CompileStatic
@Configuration
@EnableFeignClients(basePackages = "com.cheroliv.fiber")
@Import([FeignClientsConfiguration.class,
        SecurityConfiguration.class,
        WebConfigurer.class])
@ComponentScan("com.cheroliv.fiber")
class Config {
}
