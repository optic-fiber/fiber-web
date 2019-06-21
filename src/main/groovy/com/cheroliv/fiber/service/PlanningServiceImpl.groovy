package com.cheroliv.fiber.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service("planningService")
class PlanningServiceImpl implements PlanningService {
    @Autowired
    PlanningService planningService
}
