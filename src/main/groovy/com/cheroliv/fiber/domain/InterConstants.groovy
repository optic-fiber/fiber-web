package com.cheroliv.fiber.domain

interface InterConstants  {

     static final String ND_SIZE_CSTRT_TPL_MSG='{com.cheroliv.fiber.domain.nd.size}'
    static final String ND_NOTNULL_CSTRT_TPL_MSG='{com.cheroliv.fiber.domain.nd.notnull}'



    static final String NOT_NULL_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.NotNull.message}"
    static final String SIZE_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Size.message}"
    static final String MIN_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Min.message}"
    static final String PATTERN_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Pattern.message}"
    static final String MAX_CSTRT_TEMPLATE_MSG = "{javax.validation.constraints.Max.message}"

    static final Integer INTER_ID_MIN_VALUE = 1
    static final Long HEURE_MIN_VALUE = 8
    static final Long HEURE_MAX_VALUE = 19
    static final Integer PRENOM_SIZE_VALUE = 100
    static final Integer NOM_SIZE_VALUE = 100

    static final String INTER_ID_COLUMN_NAME = 'id'

    static final String INTER_ND_COLUMN_NAME = 'nd'
    static final String INTER_NOM_COLUMN_NAME = 'nom'
    static final String INTER_PRENOM_COLUMN_NAME = 'prenom'
    static final String INTER_HEURE_COLUMN_NAME = 'heure'
    static final String INTER_DATE_COLUMN_NAME = 'date'
    static final String INTER_CONTRAT_COLUMN_NAME = 'contrat'
    static final String INTER_TYPE_COLUMN_NAME = 'type'
}
