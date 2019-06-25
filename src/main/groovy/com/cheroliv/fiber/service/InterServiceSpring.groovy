package com.cheroliv.fiber.service

import com.cheroliv.fiber.repository.InterRepository
import com.cheroliv.fiber.service.InterService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service
class InterServiceSpring implements InterService {

    final InterRepository interRepository

    @Autowired
    InterServiceSpring(InterRepository interRepository) {
        this.interRepository = interRepository
    }
}
