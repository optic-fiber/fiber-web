package com.cheroliv.fiber.config

import com.cheroliv.fiber.web.rest.InterController
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@CompileStatic
@Configuration
@EnableSwagger2
@EnableCaching
@EnableFeignClients(basePackages = "com.cheroliv.fiber")
@Import([FeignClientsConfiguration.class,
        SecurityConfiguration.class])
@ComponentScan("com.cheroliv.fiber")
class Config {
}
