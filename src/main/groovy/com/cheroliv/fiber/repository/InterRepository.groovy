package com.cheroliv.fiber.repository

import com.cheroliv.fiber.domain.Inter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InterRepository extends CrudRepository<Inter,Long>{
}
