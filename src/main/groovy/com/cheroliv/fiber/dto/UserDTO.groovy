package com.cheroliv.fiber.dto

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import groovy.transform.CompileStatic

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.time.Instant

@CompileStatic
class UserDTO {
    Long id
    @NotBlank
    @Pattern(regexp = User.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    String login
    @Size(max = 50)
    String firstName
    @Size(max = 50)
    String lastName
    @Email
    @Size(min = 5, max = 254)
    String email
    @Size(max = 256)
    String imageUrl
    boolean activated = false
    @Size(min = 2, max = 10)
    String langKey
    Instant createdDate
    Set<String> authorities

    UserDTO(User user) {
        this.id = user.getId()
        this.login = user.getLogin()
        this.firstName = user.getFirstName()
        this.lastName = user.getLastName()
        this.email = user.getEmail()
        this.activated = user.getActivated()
        this.imageUrl = user.getImageUrl()
        this.langKey = user.getLangKey()
        this.createdDate = user.getCreatedDate()
        user.getAuthorities().each { Authority authority ->
            this.authorities.add(authority.getName())
        }
    }
}
