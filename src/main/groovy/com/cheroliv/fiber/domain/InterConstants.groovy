package com.cheroliv.fiber.domain

interface InterConstants  {

    static String NOT_NULL_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.NotNull.message}"
    static String SIZE_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Size.message}"
    static String MIN_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Min.message}"
    static String PATTERN_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Pattern.message}"
    static String MAX_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Max.message}"

    static Integer INTER_ID_MIN_VALUE = 1
    static Long HEURE_MIN_VALUE = 8
    static Long HEURE_MAX_VALUE = 19
    static Integer PRENOM_SIZE_VALUE = 100
    static Integer NOM_SIZE_VALUE = 100

    static String INTER_ID_COLUMN_NAME = 'id'

    static String INTER_ND_COLUMN_NAME = 'nd'
    static String INTER_NOM_COLUMN_NAME = 'nom'
    static String INTER_PRENOM_COLUMN_NAME = 'prenom'
    static String INTER_HEURE_COLUMN_NAME = 'heure'
    static String INTER_DATE_COLUMN_NAME = 'date'
    static String INTER_CONTRAT_COLUMN_NAME = 'contrat'
    static String INTER_TYPE_COLUMN_NAME = 'type'
}