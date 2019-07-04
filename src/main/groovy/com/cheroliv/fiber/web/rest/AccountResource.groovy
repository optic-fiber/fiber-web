package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.dto.PasswordChangeDTO
import com.cheroliv.fiber.dto.UserDTO
import com.cheroliv.fiber.exceptions.EmailAlreadyUsedException
import com.cheroliv.fiber.exceptions.EmailNotFoundException
import com.cheroliv.fiber.exceptions.InvalidPasswordException
import com.cheroliv.fiber.exceptions.LoginAlreadyUsedException
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.security.SecurityUtils
import com.cheroliv.fiber.service.MailService
import com.cheroliv.fiber.service.UserService
import com.cheroliv.fiber.web.rest.vm.KeyAndPasswordVM
import com.cheroliv.fiber.web.rest.vm.ManagedUserVM
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.util.function.Function
import java.util.function.Supplier


@Slf4j
@RestController
@RequestMapping("/api")
@CompileStatic
class AccountResource {

    static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message)
        }
    }

    final UserRepository userRepository
    final UserService userService
    final MailService mailService

    @Autowired
    AccountResource(
            UserRepository userRepository,
            UserService userService,
            MailService mailService) {
        this.userRepository = userRepository
        this.userService = userService
        this.mailService = mailService
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException{@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException{@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException{@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.password)) {
            throw new InvalidPasswordException()
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.password)
        mailService.sendActivationEmail(user)
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException{@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key)
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key")
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated")
        request.remoteUser
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException{@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    UserDTO getAccount() {
        return userService.userWithAuthorities
                .map(new Function<User, UserDTO>() {
                    @Override
                    UserDTO apply(User user) {
                        return new UserDTO(user)
                    }
                })
                .orElseThrow(new Supplier<AccountResourceException>() {
                    @Override
                    AccountResourceException get() {
                        return new AccountResourceException("User could not be found")
                    }
                })
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException{@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException{@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.currentUserLogin.orElseThrow(new Supplier<AccountResourceException>() {
            @Override
            AccountResourceException get() {
                return new AccountResourceException("Current user login not found")
            }
        })
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.email)
        if (existingUser.present && (!existingUser.get().login.equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException()
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin)
        if (!user.present) {
            throw new AccountResourceException("User could not be found")
        }
        userService.updateUser(
                userDTO.firstName,
                userDTO.lastName,
                userDTO.email,
                userDTO.langKey,
                userDTO.imageUrl)
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException{@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.newPassword)) {
            throw new InvalidPasswordException()
        }
        userService.changePassword(
                passwordChangeDto.currentPassword,
                passwordChangeDto.newPassword)
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     * @throws EmailNotFoundException{@code 400 (Bad Request)} if the email address is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    void requestPasswordReset(@RequestBody String mail) {
        mailService.sendPasswordResetMail(
                userService.requestPasswordReset(mail)
                        .orElseThrow(new Supplier<EmailNotFoundException>() {
                            @Override
                            EmailNotFoundException get() {
                                return new EmailNotFoundException()
                            }
                        })
        )
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException{@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException{@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.newPassword)) {
            throw new InvalidPasswordException()
        }
        Optional<User> user =
                userService.completePasswordReset(
                        keyAndPassword.newPassword,
                        keyAndPassword.key)

        if (!user.present) {
            throw new AccountResourceException("No user was found for this reset key")
        }
    }

    private static boolean checkPasswordLength(String password) {
        !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH
    }
}
