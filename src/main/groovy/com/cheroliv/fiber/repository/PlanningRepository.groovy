package com.cheroliv.fiber.repository

import com.cheroliv.fiber.domain.Planning
import groovy.transform.CompileStatic
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@CompileStatic
@Repository
interface PlanningRepository extends PagingAndSortingRepository<Planning, Long> {
    @Query("from Planning p where p.user.login=:login")
    Slice<Planning> findByUserLogin(@Param("login")String login, Pageable pageable)

    @Query("from Planning p where p.user.login=:login and u.planning.open=true")
    Optional<Planning> findByUserLoginAndOpen(String login)
}
