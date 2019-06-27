package com.cheroliv.fiber

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport

import javax.validation.Validator

@Slf4j
@CompileStatic
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ApplicationTest {

    final Validator validator
    final ApplicationContext context

    @Autowired
    ApplicationTest(ApplicationContext context,
                    Validator validator) {
        this.validator = validator
        this.context = context
    }

    @Test
    @DisplayName("Test Spring Context")
    void contextLoads() {
        log.info("context loaded")
    }

    @Test
    @DisplayName("Test Spring Validator injection")
    void testContextContainsValidator() {
        assert validator.class.name == LocalValidatorFactoryBean.class.name
    }

    @Test
    @DisplayName("Test Spring context contains SecurityProblemSupport")
    void testContextContainsSecurityProblemSupport() {
        assert SecurityProblemSupport.class.name ==
                context.getBean(SecurityProblemSupport.class).class.name
    }


}
