package com.cheroliv.fiber.service

import com.cheroliv.fiber.repository.PlanningRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service
class PlanningServiceImpl implements PlanningService {
    final PlanningRepository planningRepository

    @Autowired
    PlanningServiceImpl(PlanningRepository planningRepository) {
        this.planningRepository = planningRepository
    }


}
