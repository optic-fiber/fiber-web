package com.cheroliv.fiber.service

import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.dto.UserDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {
    Optional<User> activateRegistration(String key)
    Optional<User> completePasswordReset(String newPassword, String key)
    Optional<User> requestPasswordReset(String mail)
    User registerUser(UserDTO userDTO, String password)
    User createUser(UserDTO userDTO)
    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl)
    Optional<UserDTO> updateUser(UserDTO userDTO)
    void deleteUser(String login)
    void changePassword(String currentClearTextPassword, String newPassword)
    Page<UserDTO> getAllManagedUsers(Pageable pageable)
    Optional<User> getUserWithAuthoritiesByLogin(String login)
    Optional<User> getUserWithAuthorities(Long id)
    Optional<User> getUserWithAuthorities()
    void removeNotActivatedUsers()
    List<String> getAuthorities()

}
