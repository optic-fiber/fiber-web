package com.cheroliv.fiber.security

import groovy.transform.CompileStatic
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import java.util.function.Function
import java.util.function.Predicate


@CompileStatic
final class SecurityUtils {
    static final int DEF_COUNT = 20

    static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.context
        Optional.ofNullable(securityContext.authentication)
                .map(new Function<Authentication, String>() {
                    @Override
                    String apply(Authentication authentication) {
                        if (authentication.principal instanceof UserDetails) {
                            UserDetails springSecurityUser = authentication.principal as UserDetails
                            return springSecurityUser.username
                        } else if (authentication.principal instanceof String) {
                            return authentication.principal as String
                        }
                        return null
                    }
                })
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.context
        Optional.ofNullable(securityContext.authentication)
                .filter(new Predicate<Authentication>() {
                    @Override
                    boolean test(Authentication authentication) {
                        return authentication.credentials instanceof String
                    }
                })
                .map(new Function<Authentication, String>() {
                    @Override
                    String apply(Authentication authentication) {
                        return authentication.credentials as String
                    }
                })
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.context
        Optional.ofNullable(securityContext.authentication)
                .map(new Function<Authentication, Boolean>() {
                    @Override
                    Boolean apply(Authentication authentication) {
                        authentication.authorities.each {
                            GrantedAuthority grantedAuthority ->
                                if (grantedAuthority.authority.equals(AuthoritiesConstants.ANONYMOUS)) {
                                    return false
                                }
                        }
                        return true
                    }
                })
                .orElse (false)
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole( )} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.context
        return Optional.ofNullable(securityContext.authentication)
                .map(new Function<Authentication, Boolean>() {
                    @Override
                    Boolean apply(Authentication authentication) {
                        authentication.getAuthorities().each {
                            GrantedAuthority grantedAuthority ->
                                if (grantedAuthority.authority.equals(authority)) {
                                    return true
                                }
                        }
                        return false
                    }
                })
                .orElse(false)
    }
    /**
     * Generate a password.
     *
     * @return the generated password.
     */
    static String generatePassword() {
        RandomStringUtils.randomAlphanumeric DEF_COUNT
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key.
     */
    static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT)
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key.
     */
    static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT)
    }

}
