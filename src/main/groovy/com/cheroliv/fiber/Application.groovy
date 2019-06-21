package com.cheroliv.fiber

import com.cheroliv.fiber.config.Config
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@CompileStatic
@Slf4j
@SpringBootApplication
@Import([Config.class])
class Application {
    static void main(String[] args) {
        log.info "running spring boot"
        SpringApplication.run(Application, args)
    }
}
