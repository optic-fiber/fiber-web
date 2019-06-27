package com.cheroliv.fiber.service

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.exceptions.EmailAlreadyUsedException
import com.cheroliv.fiber.exceptions.InvalidPasswordException
import com.cheroliv.fiber.exceptions.LoginAlreadyUsedException
import com.cheroliv.fiber.repository.AuthorityRepository
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.security.AuthoritiesConstants
import com.cheroliv.fiber.security.SecurityUtils
import com.cheroliv.fiber.security.UserDTO
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors


@Slf4j
@CompileStatic
@Service
@Transactional
class UserServiceSpring implements UserService {

    final UserRepository userRepository
    final PasswordEncoder passwordEncoder
    final AuthorityRepository authorityRepository
    final CacheManager cacheManager

    @Autowired
    UserServiceSpring(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      AuthorityRepository authorityRepository,
                      CacheManager cacheManager) {
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
        this.authorityRepository = authorityRepository
        this.cacheManager = cacheManager
    }


    Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key)
        userRepository.findOneByActivationKey(key)
                .map(new Function<User, User>() {
                    @Override
                    User apply(User user) {
                        // activate given user for the registration key.
                        user.activated = true
                        user.activationKey = null
                        UserServiceSpring.this.clearUserCaches(user)
                        log.debug("Activated user: {}", user)
                        user
                    }
                })
    }

    Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key)
        return userRepository.findOneByResetKey(key)
                .filter(new Predicate<User>() {
                    @Override
                    boolean test(User user) {
                        return user.resetDate.isAfter(Instant.now().minusSeconds(86400))
                    }
                })
                .map(new Function<User, User>() {
                    @Override
                    User apply(User user) {
                        user.password = passwordEncoder.encode(newPassword)
                        user.resetKey = null
                        user.resetDate = null
                        UserServiceSpring.this.clearUserCaches(user)
                        return user
                    }
                })
    }

    Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
                .filter(new Predicate<User>() {
                    @Override
                    boolean test(User user) {
                        return user.activated
                    }
                })
                .map(new Function<User, User>() {
                    @Override
                    User apply(User user) {
                        user.resetKey = SecurityUtils.generateResetKey()
                        user.resetDate = Instant.now()
                        UserServiceSpring.this.clearUserCaches(user)
                        return user
                    }
                })
    }

    User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.login.toLowerCase()).ifPresent(new Consumer<User>() {
            @Override
            void accept(User existingUser) {
                boolean removed = UserServiceSpring.this.removeNonActivatedUser(existingUser)
                if (!removed) {
                    throw new LoginAlreadyUsedException()
                }
            }
        })
        userRepository.findOneByEmailIgnoreCase(userDTO.email).ifPresent(new Consumer<User>() {
            @Override
            void accept(User existingUser) {
                boolean removed = UserServiceSpring.this.removeNonActivatedUser(existingUser)
                if (!removed) {
                    throw new EmailAlreadyUsedException()
                }
            }
        })
        User newUser = new User()
        String encryptedPassword = passwordEncoder.encode(password)
        newUser.login = userDTO.login.toLowerCase()
        newUser.password = encryptedPassword
        newUser.firstName = userDTO.firstName
        newUser.lastName = userDTO.lastName
        newUser.email = userDTO.email.toLowerCase()
        newUser.imageUrl = userDTO.imageUrl
        newUser.langKey = userDTO.langKey
        newUser.activated = false
        newUser.setActivationKey(SecurityUtils.generateActivationKey())
        Set<Authority> authorities = new HashSet<>()
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(new Consumer<Authority>() {
            @Override
            void accept(Authority e) {
                authorities.add(e)
            }
        })
        newUser.authorities = authorities
        userRepository.save(newUser)
        this.clearUserCaches(newUser)
        log.debug("Created Information for User: {}", newUser)
        newUser
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.activated) {
            return false
        }
        userRepository.delete(existingUser)
        userRepository.flush()
        this.clearUserCaches(existingUser)
        true
    }

    User createUser(UserDTO userDTO) {
        User user = new User()
        user.login = userDTO.login.toLowerCase()
        user.firstName = userDTO.firstName
        user.lastName = userDTO.lastName
        user.email = userDTO.email.toLowerCase()
        user.imageUrl = userDTO.imageUrl
        if (userDTO.langKey == null) {
            user.langKey = User.DEFAULT_LANGUAGE // default language
        } else {
            user.langKey = userDTO.langKey
        }
        String encryptedPassword = passwordEncoder.encode(SecurityUtils.generatePassword())
        user.setPassword(encryptedPassword)
        user.setResetKey(SecurityUtils.generateResetKey())
        user.setResetDate(Instant.now())
        user.setActivated(true)
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>()
            userDTO.getAuthorities().each { String s ->
                Optional<Authority> byId = authorityRepository.findById(s)
                if (byId.isPresent()) {
                    Authority authority = byId.get()
                    authorities.add(authority)
                }
            }
            user.setAuthorities(authorities)
        }
        userRepository.save(user)
        this.clearUserCaches(user)
        log.debug("Created Information for User: {}", user)
        return user
    }


    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.currentUserLogin
                .flatMap(new Function<String, Optional<? extends User>>() {
                    @Override
                    Optional<? extends User> apply(String login) {
                        return userRepository.findOneByLogin(login)
                    }
                })
                .ifPresent(new Consumer<User>() {
                    @Override
                    void accept(User user) {
                        user.setFirstName(firstName)
                        user.setLastName(lastName)
                        user.setEmail(email.toLowerCase())
                        user.setLangKey(langKey)
                        user.setImageUrl(imageUrl)
                        UserServiceSpring.this.clearUserCaches(user)
                        log.debug("Changed Information for User: {}", user)
                    }
                })
    }


    Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(new Predicate<Optional<User>>() {
                    @Override
                    boolean test(Optional<User> user1) {
                        return user1.isPresent()
                    }
                })
                .map(new Function<Optional<User>, User>() {
                    @Override
                    User apply(Optional<User> user2) {
                        return user2.get()
                    }
                })
                .map(new Function<User, User>() {
                    @Override
                    User apply(User user) {
                        UserServiceSpring.this.clearUserCaches(user)
                        user.setLogin(userDTO.getLogin().toLowerCase())
                        user.setFirstName(userDTO.getFirstName())
                        user.setLastName(userDTO.getLastName())
                        user.setEmail(userDTO.getEmail().toLowerCase())
                        user.setImageUrl(userDTO.getImageUrl())
                        user.setActivated(userDTO.isActivated())
                        user.setLangKey(userDTO.getLangKey())
                        Set<Authority> managedAuthorities = user.getAuthorities()
                        managedAuthorities.clear()
                        for (String s : userDTO.getAuthorities()) {
                            Optional<Authority> byId = authorityRepository.findById(s)
                            if (byId.isPresent()) {
                                Authority authority = byId.get()
                                managedAuthorities.add(authority)
                            }
                        }
                        UserServiceSpring.this.clearUserCaches(user)
                        log.debug("Changed Information for User: {}", user)
                        return user
                    }
                })
                .map(new Function<User, UserDTO>() {
                    @Override
                    UserDTO apply(User user) {
                        return new UserDTO(user)
                    }
                })
    }

    void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(new Consumer<User>() {
            @Override
            void accept(User user) {
                userRepository.delete(user)
                UserServiceSpring.this.clearUserCaches(user)
                log.debug("Deleted User: {}", user)
            }
        })
    }

    void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.currentUserLogin
                .flatMap(new Function<String, Optional<? extends User>>() {
                    @Override
                    Optional<? extends User> apply(String login) {
                        return userRepository.findOneByLogin(login)
                    }
                })
                .ifPresent(new Consumer<User>() {
                    @Override
                    void accept(User user) {
                        String currentEncryptedPassword = user.getPassword()
                        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                            throw new InvalidPasswordException()
                        }
                        String encryptedPassword = passwordEncoder.encode(newPassword)
                        user.setPassword(encryptedPassword)
                        UserServiceSpring.this.clearUserCaches(user)
                        log.debug("Changed password for User: {}", user)
                    }
                })
    }

    @Transactional(readOnly = true)
    Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, User.ANONYMOUS_USER).map(new Function<User, UserDTO>() {
            @Override
            UserDTO apply(User user) {
                return new UserDTO(user)
            }
        })
    }

    @Transactional(readOnly = true)
    Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login)
    }

    @Transactional(readOnly = true)
    Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id)
    }

    @Transactional(readOnly = true)
    Optional<User> getUserWithAuthorities() {
        return SecurityUtils.currentUserLogin.flatMap(new Function<String, Optional<? extends User>>() {
            @Override
            Optional<? extends User> apply(String login) {
                return userRepository.findOneWithAuthoritiesByLogin(login)
            }
        })
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(new Consumer<User>() {
                    @Override
                    void accept(User user) {
                        log.debug("Deleting not activated user {}", user.getLogin())
                        userRepository.delete(user)
                        UserServiceSpring.this.clearUserCaches(user)
                    }
                })
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(new Function<Authority, String>() {
            @Override
            String apply(Authority authority) {
                return authority.getName()
            }
        }).collect(Collectors.toList())
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin())
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail())
    }
}

