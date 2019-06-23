package com.cheroliv.fiber.exceptions

import groovy.transform.CompileStatic
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Status

@CompileStatic
class BadRequestAlertException extends AbstractThrowableProblem {

    static final long serialVersionUID = 1L

    final String entityName

    final String errorKey

    BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey)
    }

    BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey))
        this.entityName = entityName
        this.errorKey = errorKey
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>()
        parameters.put("message", "error." + errorKey)
        parameters.put("params", entityName)
        parameters
    }
}
