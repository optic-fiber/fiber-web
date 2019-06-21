package com.cheroliv.fiber.web.rest

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@Slf4j
@RestController
@RequestMapping("fiber")
class FiberController {
    @RequestMapping(
            value = "home",
            method = RequestMethod.GET)
    String home() {
        "Hello World!"
    }
}
