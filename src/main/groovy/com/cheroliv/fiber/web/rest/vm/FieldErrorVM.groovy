package com.cheroliv.fiber.web.rest.vm

class FieldErrorVM implements Serializable {

     static final long serialVersionUID = 1L

     final String objectName

     final String field

     final String message

    FieldErrorVM(String dto, String field, String message) {
        this.objectName = dto
        this.field = field
        this.message = message
    }
}
