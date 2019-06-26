package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.repository.InterRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Slf4j
@CompileStatic
@RestController
@RequestMapping("inters")
class InterController {

    final InterRepository interRepository

    @Autowired
    InterController(InterRepository interRepository) {
        this.interRepository = interRepository
    }

}
