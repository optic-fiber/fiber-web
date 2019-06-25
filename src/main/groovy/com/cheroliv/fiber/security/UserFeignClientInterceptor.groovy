package com.cheroliv.fiber.security

import feign.RequestInterceptor
import feign.RequestTemplate
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

import java.util.function.Consumer

@CompileStatic
@Component
class UserFeignClientInterceptor implements RequestInterceptor {
    static final String AUTHORIZATION_HEADER = "Authorization"
    static final String BEARER = "Bearer"

    @Override
    void apply(RequestTemplate template) {
        SecurityUtils.currentUserJWT
                .ifPresent(new Consumer<String>() {
                    @Override
                    void accept(String s) {
                        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER, s))
                    }
                })
    }
}
