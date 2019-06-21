package com.cheroliv.fiber.web.rest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Slf4j
@CompileStatic
@RestController
@RequestMapping
class InterController {

    @RequestMapping("inters")
    String add() {
        "add inter"
    }
}
