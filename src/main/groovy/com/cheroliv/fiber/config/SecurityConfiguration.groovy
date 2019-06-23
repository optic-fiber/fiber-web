package com.cheroliv.fiber.config

import com.cheroliv.fiber.security.JWTConfigurer
import com.cheroliv.fiber.security.TokenProvider
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport

@CompileStatic
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    final TokenProvider tokenProvider

    private JWTConfigurer securityConfigurerAdapter() {
        new JWTConfigurer(tokenProvider)
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Override
    void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**")
    }


}
