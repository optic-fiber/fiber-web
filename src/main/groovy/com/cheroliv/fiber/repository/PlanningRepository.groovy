package com.cheroliv.fiber.repository

import com.cheroliv.fiber.domain.Planning
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanningRepository extends PagingAndSortingRepository<Planning,Long> {
}
