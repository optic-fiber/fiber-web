package com.cheroliv.fiber.service

import com.cheroliv.fiber.repository.PlanningRepository
import com.cheroliv.fiber.service.PlanningService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service
class PlanningServiceSpring implements PlanningService {
    final PlanningRepository planningRepository

    @Autowired
    PlanningServiceSpring(PlanningRepository planningRepository) {
        this.planningRepository = planningRepository
    }


}
