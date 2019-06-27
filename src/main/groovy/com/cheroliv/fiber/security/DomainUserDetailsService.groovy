package com.cheroliv.fiber.security

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.exceptions.UserNotActivatedException
import com.cheroliv.fiber.repository.UserRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collector
import java.util.stream.Collectors


@Slf4j
@CompileStatic
@Component("userDetailsService")
class DomainUserDetailsService implements UserDetailsService {

    final UserRepository userRepository

    @Autowired
    DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    @Transactional
    UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login)

        if (new EmailValidator().isValid(login, null)) {
            userRepository.findOneWithAuthoritiesByEmail(login)
                    .map(new Function<User, org.springframework.security.core.userdetails.User>() {
                        @Override
                        org.springframework.security.core.userdetails.User apply(User user) {
                            DomainUserDetailsService.this.createSpringSecurityUser(login, user)
                        }
                    })
                    .orElseThrow(new Supplier<UsernameNotFoundException>() {
                        @Override
                        UsernameNotFoundException get() {
                            new UsernameNotFoundException("User with email " + login + " was not found in the database")
                        }
                    })
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH)
        userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
                .map(new Function<User, org.springframework.security.core.userdetails.User>() {
                    @Override
                    org.springframework.security.core.userdetails.User apply(User user) {
                        DomainUserDetailsService.this.createSpringSecurityUser(lowercaseLogin, user)
                    }
                })
                .orElseThrow(new Supplier<UsernameNotFoundException>() {
                    @Override
                    UsernameNotFoundException get() {
                        new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")
                    }
                })

    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated")
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(new Function<Authority, SimpleGrantedAuthority>() {
                    @Override
                    SimpleGrantedAuthority apply(Authority authority) {
                        new SimpleGrantedAuthority(authority.name)
                    }
                })
                .collect(Collectors.toList() as Collector<? super SimpleGrantedAuthority, ?, List<GrantedAuthority>>)
        new org.springframework.security.core.userdetails.User(user.login,
                user.password,
                grantedAuthorities)
    }
}
