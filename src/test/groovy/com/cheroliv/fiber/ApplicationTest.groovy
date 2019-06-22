package com.cheroliv.fiber

import groovy.util.logging.Slf4j
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApplicationTest {
    @Test
    @DisplayName("Test Spring Context")
    void contextLoads() {
        log.info("context loaded")
    }
}
