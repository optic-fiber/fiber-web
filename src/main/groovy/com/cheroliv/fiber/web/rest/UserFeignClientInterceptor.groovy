package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.security.SecurityUtils
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

import java.util.function.Consumer

@Component
class UserFeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization"
    private static final String BEARER = "Bearer"

    @Override
    void apply(RequestTemplate template) {
        SecurityUtils.getCurrentUserJWT()
                .ifPresent(new Consumer<String>() {
                    @Override
                    void accept(String s) {
                        template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER, s))
                    }
                })
    }
}
