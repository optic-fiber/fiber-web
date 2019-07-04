package com.cheroliv.fiber.service

import com.cheroliv.fiber.repository.InterRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service
class InterServiceImpl implements InterService {

    final InterRepository interRepository

    @Autowired
    InterServiceImpl(InterRepository interRepository) {
        this.interRepository = interRepository
    }
}
