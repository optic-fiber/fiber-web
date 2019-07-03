package com.cheroliv.fiber.domain.enumeration

import groovy.transform.CompileStatic


@CompileStatic
enum ContractEnum {
    LM, IQ, CABLE_ROUTING
    static ContractEnum valueOfName( String name ) {
        values().find { ContractEnum it ->
            it.name() == name
        }
    }
}
