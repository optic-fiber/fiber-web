package com.cheroliv.fiber.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.time.Instant

@CompileStatic
@ToString
@Entity
@Table(name = "`user`", indexes = [
        @Index(name = "`unique_idx_user_login`", columnList = "`login`", unique = true),
        @Index(name = "`unique_idx_user_email`", columnList = "`email`", unique = true),
        @Index(name = "`idx_user_password_hash`", columnList = "`password_hash`"),
        @Index(name = "`idx_user_first_name`", columnList = "`first_name`"),
        @Index(name = "`idx_user_last_name`", columnList = "`last_name`"),
        @Index(name = "`idx_user_activated`", columnList = "`activated`"),
        @Index(name = "`idx_user_lang_key`", columnList = "`lang_key`"),
        @Index(name = "`idx_user_image_url`", columnList = "`image_url`")])
class User implements Serializable {
    static final long serialVersionUID = 1L
    static final String LOGIN_REGEX = '^[_.@A-Za-z0-9-]*$'

    static final String SYSTEM_ACCOUNT = "system"
    static final String DEFAULT_LANGUAGE = "fr"
    static final String ANONYMOUS_USER = "anonymoususer"

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    Long id

    @NotNull
    @Pattern(regexp = LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50,
            nullable = false, name = "`login`")
    String login

    @JsonIgnore
    @NotNull
//    @Size(min = 60, max = 60)
    @Column(name = "`password_hash`",
            length = 60, nullable = false)
    String password

    @Size(max = 50)
    @Column(name = "`first_name`", length = 50)
    String firstName

    @Size(max = 50)
    @Column(name = "`last_name`", length = 50)
    String lastName

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, name = "`email`")
    String email

    @NotNull
    @Column(nullable = false, name = "`activated`")
    Boolean activated = false

    @Size(min = 2, max = 6)
    @Column(name = "`lang_key`", length = 6)
    String langKey

    @Size(max = 256)
    @Column(name = "`image_url`", length = 256)
    String imageUrl

    @Size(max = 20)
    @Column(name = "`activation_key`", length = 20)
    @JsonIgnore
    String activationKey

    @Size(max = 20)
    @Column(name = "`reset_key`", length = 20)
    @JsonIgnore
    String resetKey

    @Column(name = "`reset_date`")
    Instant resetDate = null

    @Column(name = "`created_date`", updatable = false)
    @JsonIgnore
    Instant createdDate

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "`user_authority`",
            joinColumns = [@JoinColumn(
                    name = "`user_id`",
                    referencedColumnName = "`id`")],
            inverseJoinColumns = [@JoinColumn(
                    name = "`authority_name`",
                    referencedColumnName = "`name`")])
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    Set<Authority> authorities = new HashSet<>()

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof User)) return false

        User user = (User) o

        if (activated != user.activated) return false
        if (activationKey != user.activationKey) return false
        if (createdDate != user.createdDate) return false
        if (email != user.email) return false
        if (firstName != user.firstName) return false
        if (imageUrl != user.imageUrl) return false
        if (langKey != user.langKey) return false
        if (lastName != user.lastName) return false
        if (login != user.login) return false
        if (password != user.password) return false
        if (resetDate != user.resetDate) return false
        if (resetKey != user.resetKey) return false

        return true
    }

    int hashCode() {
        int result
        result = (login != null ? login.hashCode() : 0)
        result = 31 * result + (password != null ? password.hashCode() : 0)
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0)
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0)
        result = 31 * result + (email != null ? email.hashCode() : 0)
        result = 31 * result + (activated != null ? activated.hashCode() : 0)
        result = 31 * result + (langKey != null ? langKey.hashCode() : 0)
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0)
        result = 31 * result + (activationKey != null ? activationKey.hashCode() : 0)
        result = 31 * result + (resetKey != null ? resetKey.hashCode() : 0)
        result = 31 * result + (resetDate != null ? resetDate.hashCode() : 0)
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0)
        return result
    }
}
