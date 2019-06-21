package com.cheroliv.fiber.service

import com.cheroliv.fiber.repository.InterRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@CompileStatic
@Service("interService")
class InterServiceImpl implements InterService{
    @Autowired
    InterRepository interRepository
}
