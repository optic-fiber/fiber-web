package com.cheroliv.fiber.security

import groovy.transform.CompileStatic
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@CompileStatic
class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    final TokenProvider tokenProvider

    JWTConfigurer(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider
    }

    @Override
    void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                new JWTFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter)
    }
}
