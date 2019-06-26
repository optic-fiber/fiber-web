package com.cheroliv.fiber.repository

import com.cheroliv.fiber.domain.Inter
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface InterRepository extends PagingAndSortingRepository<Inter, Long> {
}
