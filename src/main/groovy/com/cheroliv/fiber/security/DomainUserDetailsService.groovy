package com.cheroliv.fiber.security

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.repository.UserRepository
import groovy.util.logging.Slf4j
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * Authenticate a user from the database.
 */
@Slf4j
@Component("userDetailsService")
class DomainUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository

    DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    @Transactional
    UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login)

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmail(login)
                    .map(new Function<User, org.springframework.security.core.userdetails.User>() {
                        @Override
                        org.springframework.security.core.userdetails.User apply(User user) {
                            return DomainUserDetailsService.this.createSpringSecurityUser(login, user)
                        }
                    })
                    .orElseThrow(new Supplier<UsernameNotFoundException>() {
                        @Override
                        UsernameNotFoundException get() {
                            return new UsernameNotFoundException("User with email " + login + " was not found in the database")
                        }
                    })
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH)
        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
                .map(new Function<User, org.springframework.security.core.userdetails.User>() {
                    @Override
                    org.springframework.security.core.userdetails.User apply(User user) {
                        return DomainUserDetailsService.this.createSpringSecurityUser(lowercaseLogin, user)
                    }
                })
                .orElseThrow(new Supplier<UsernameNotFoundException>() {
                    @Override
                    UsernameNotFoundException get() {
                        return new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")
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
                        return new SimpleGrantedAuthority(authority.getName())
                    }
                })
                .collect(Collectors.toList())
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                grantedAuthorities)
    }
}
