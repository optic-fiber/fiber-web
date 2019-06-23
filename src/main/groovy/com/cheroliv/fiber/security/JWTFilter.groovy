package com.cheroliv.fiber.security

import groovy.transform.CompileStatic
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@CompileStatic
class JWTFilter extends GenericFilterBean {

    static final String AUTHORIZATION_HEADER = "Authorization"

    TokenProvider tokenProvider

    JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider
    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = servletRequest as HttpServletRequest
        String jwt = resolveToken(httpServletRequest)
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.context.setAuthentication(authentication)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        null
    }
}
