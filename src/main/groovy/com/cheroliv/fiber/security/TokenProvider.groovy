package com.cheroliv.fiber.security

import com.cheroliv.fiber.config.FiberProperties
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

import java.nio.charset.StandardCharsets
import java.security.Key

@Slf4j
@CompileStatic
@Component
class TokenProvider implements InitializingBean {
    @Value('${fiber.security.authentication.jwt.base64-secret}')
    String base64Secret
    static final String AUTHORITIES_KEY = "auth"
    Key key
    long tokenValidityInMilliseconds
    long tokenValidityInMillisecondsForRememberMe
    final FiberProperties properties

    @Override
    void afterPropertiesSet() throws Exception {
        byte[] keyBytes
        log.info "properties.security.authentication.jwt.secret : ${properties.security.authentication.jwt.secret}"
        String secret = properties.security.authentication.jwt.secret
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                    "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.")
            keyBytes = secret.getBytes(StandardCharsets.UTF_8)
        } else {
            log.debug("Using a Base64-encoded JWT secret key")
            keyBytes = Decoders.BASE64.decode(base64Secret)
//            keyBytes = Decoders.BASE64.decode(security.authentication.jwt.base64Secret)
        }
        this.key = Keys.hmacShaKeyFor(keyBytes)
        this.tokenValidityInMilliseconds =
                1000 * properties.security.authentication.jwt.tokenValidityInSeconds
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * properties.security.authentication.jwt.tokenValidityInSecondsForRememberMe
    }

    String createToken(Authentication authentication, boolean rememberMe) {
        StringJoiner joiner = new StringJoiner(",")
        authentication.authorities.each { GrantedAuthority grantedAuthority ->
            String authority = grantedAuthority.authority
            joiner.add(authority)
        }
        String authorities = joiner.toString()

        long now = new Date().time
        Date validity
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe)
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds)
        }

        Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact()
    }

    Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .body

        List<SimpleGrantedAuthority> list = new ArrayList<>()
        claims[AUTHORITIES_KEY].toString().split(",").each { String s ->
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(s)
            list.add(simpleGrantedAuthority)
        }
        Collection<? extends GrantedAuthority> authorities = list

        User principal = new User(claims.getSubject(), "", authorities)

        new UsernamePasswordAuthenticationToken(principal, token, authorities)

    }

    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken)
            return true
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.")
            log.trace("Expired JWT token trace: {}", e)
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.")
            log.trace("Unsupported JWT token trace: {}", e)
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.")
            log.trace("JWT token compact of handler are invalid trace: {}", e)
        }
        false
    }
}
