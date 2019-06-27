package com.cheroliv.fiber.domain

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Slf4j
@CompileStatic
class InterUtilsTest {
    @Test
    @DisplayName("InterTest.testTimeStringToInteger")
    void testTimeStringToInteger() {
        Integer[] heures =
                [9, 13, 8, 9, 8,
                 12, 12, 8, 13, 15]
        String[] strHeures = [
                "09:00:00", "13:00:00",
                "08:00:00", "09:00:00",
                "08:00:00", "12:00:00",
                "12:00:00", "08:00:00",
                "13:00:00", "15:00:00"]
        strHeures.eachWithIndex { it, idx ->
            Assertions.assertEquals InterUtils.timeStringToInteger(it), heures[idx]
        }
    }
}
