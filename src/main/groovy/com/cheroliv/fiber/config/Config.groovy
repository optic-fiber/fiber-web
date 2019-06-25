package com.cheroliv.fiber.config

import groovy.transform.CompileStatic
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.swagger2.annotations.EnableSwagger2

@CompileStatic
@Configuration
@ComponentScan("com.cheroliv.fiber")
@EnableCaching
@EnableFeignClients(basePackages = "com.cheroliv.fiber")
@EnableSwagger2
@Import([FeignClientsConfiguration.class,
        SecurityConfiguration.class])
class Config {
}
