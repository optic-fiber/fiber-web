package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.security.TokenProvider
import com.cheroliv.fiber.web.rest.vm.LoginVM
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
class UserJWTController {

    final TokenProvider tokenProvider
    final AuthenticationManagerBuilder authenticationManagerBuilder

    @Autowired
    UserJWTController(
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider
        this.authenticationManagerBuilder = authenticationManagerBuilder
    }

    @PostMapping("/authenticate")
    ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.username, loginVM.password)

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().setAuthentication(authentication)
        boolean rememberMe = (loginVM.rememberMe == null) ? false : loginVM.rememberMe
        String jwt = tokenProvider.createToken(authentication, rememberMe)
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt)
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        @JsonProperty("id_token")
        String idToken

        JWTToken(String idToken) {
            this.idToken = idToken
        }
    }
}
