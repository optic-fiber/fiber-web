package com.cheroliv.fiber.repository

import com.cheroliv.fiber.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
@Repository
interface AuthorityRepository extends JpaRepository<Authority, String> {
}
