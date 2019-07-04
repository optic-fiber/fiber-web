package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.dto.UserDTO
import com.cheroliv.fiber.exceptions.BadRequestAlertException
import com.cheroliv.fiber.exceptions.EmailAlreadyUsedException
import com.cheroliv.fiber.exceptions.LoginAlreadyUsedException
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.service.MailService
import com.cheroliv.fiber.service.UserService
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

import javax.validation.Valid
import java.util.function.Function

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@CompileStatic
class UserResource {


    @Value('${fiber.application-name}')
    String applicationName

    final UserService userService

    final UserRepository userRepository

    final MailService mailService

    @Autowired
    UserResource(
            UserService userService,
            UserRepository userRepository,
            MailService mailService) {
        this.userService = userService
        this.userRepository = userRepository
        this.mailService = mailService
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the{@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException{@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize('hasRole(ROLE_ADMIN)')
    ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO)

        if (userDTO.id != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists")
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.login.toLowerCase()).present) {
            throw new LoginAlreadyUsedException()
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.email).present) {
            throw new EmailAlreadyUsedException()
        } else {
            User newUser = userService.createUser(userDTO)
            mailService.sendCreationEmail(newUser)
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                    .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
                    .body(newUser)
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the{@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException{@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException{@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize('hasRole(ROLE_ADMIN)')
    ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO)
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.email)
        if (existingUser.present && (!existingUser.get().id.equals(userDTO.id))) {
            throw new EmailAlreadyUsedException()
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase())
        if (existingUser.present && (!existingUser.get().id.equals(userDTO.id))) {
            throw new LoginAlreadyUsedException()
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO)

        ResponseUtil.wrapOrNotFound(updatedUser,
                HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()))
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param pageable the pagination information.
     * @return the{@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam MultiValueMap<String, String> queryParams,
            UriComponentsBuilder uriBuilder,
            Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable)
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                uriBuilder.queryParams(queryParams),
                page)
        new ResponseEntity<>(page.content, headers, HttpStatus.OK)
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(ROLE_ADMIN)")
    List<String> getAuthorities() {
        userService.authorities
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the{@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping('/users/{login:^[_.@A-Za-z0-9-]*$}')
    ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login)
        return ResponseUtil.wrapOrNotFound(
                userService.getUserWithAuthoritiesByLogin(login)
                        .map(new Function<User, UserDTO>() {
                            @Override
                            UserDTO apply(User user) {
                                return new UserDTO(user)
                            }
                        }))
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the{@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    @DeleteMapping('/users/{login:^[_.@A-Za-z0-9-]*$}')
    @PreAuthorize("hasRole(ROLE_ADMIN)")
    ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login)
        userService.deleteUser(login)
        ResponseEntity.noContent()
                .headers(HeaderUtil
                        .createAlert(
                                applicationName,
                                "userManagement.deleted",
                                login)).build()
    }
}
