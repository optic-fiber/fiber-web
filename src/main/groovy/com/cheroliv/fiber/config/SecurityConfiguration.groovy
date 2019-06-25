package com.cheroliv.fiber.config

import com.cheroliv.fiber.security.AuthoritiesConstants
import com.cheroliv.fiber.security.JWTConfigurer
import com.cheroliv.fiber.security.TokenProvider
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.web.filter.CorsFilter
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    final TokenProvider tokenProvider
    final CorsFilter corsFilter
    final SecurityProblemSupport problemSupport

    @Autowired
    SecurityConfiguration(TokenProvider tokenProvider,
                          CorsFilter corsFilter,
                          SecurityProblemSupport problemSupport) {
        this.tokenProvider = tokenProvider
        this.corsFilter = corsFilter
        this.problemSupport = problemSupport
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Override
    @CompileStatic
    void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**")
    }


    @Override
    void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()

        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)

        http.headers()
                .contentSecurityPolicy(
                        "default-src 'self'; " +
                                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                "style-src 'self' 'unsafe-inline'; " +
                                "img-src 'self' data:")
        http.headers()
                .referrerPolicy(ReferrerPolicyHeaderWriter
                        .ReferrerPolicy
                        .STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        http.headers()
                .featurePolicy("geolocation 'none'; " +
                        "midi 'none'; " +
                        "sync-xhr 'none'; " +
                        "microphone 'none'; " +
                        "camera 'none'; " +
                        "magnetometer 'none'; " +
                        "gyroscope 'none'; " +
                        "speaker 'none'; " +
                        "fullscreen 'self'; " +
                        "payment 'none'")
        http.headers()
                .frameOptions()
                .deny()

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/account/reset-password/init").permitAll()
                .antMatchers("/api/account/reset-password/finish").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/prometheus").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)

        http.httpBasic()

        http.apply(new JWTConfigurer(tokenProvider))

    }

}
