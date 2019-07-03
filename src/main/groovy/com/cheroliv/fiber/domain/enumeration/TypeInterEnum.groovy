package com.cheroliv.fiber.domain.enumeration

import groovy.transform.CompileStatic

@CompileStatic
enum TypeInterEnum {
    BAAP, BAOC, BAFA, BAST, PLP, SAV

    static TypeInterEnum valueOfName(String name) {
        values().find { TypeInterEnum it ->
            it.name() == name
        }
    }
}
