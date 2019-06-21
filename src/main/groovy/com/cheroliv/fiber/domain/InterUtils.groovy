package com.cheroliv.fiber.domain

import groovy.transform.CompileStatic

import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

@CompileStatic
class InterUtils {
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


    static LocalTime parseStringHeureToLocalTime(String strHeure) {
        LocalTime.of(Integer.parseInt("${strHeure.charAt(0)}${strHeure.charAt(1)}"), 0)
    }

    static LocalDate parseStringDateToLocalDate(String strDate) {
        LocalDate.parse(strDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }


    static Integer timeStringToInteger(String strHeure) {
        Integer.parseInt "${strHeure.charAt(0)}${strHeure.charAt(1)}"
    }

//    static Inter mapToInstance(Map<String,String> map){
//        new Inter(
//                id: Long.parseLong(map["id"]),
//                nd: map["nd"],
//                firstNameClient: map["nom"],
//                prenom: map["prenom"],
//                contrat: map["contrat"],
//                type: map["type"]
//                ,
//                date: parseStringDateToLocalDate(map["date"]),
//                heure: parseStringHeureToLocalTime(map["heure"])
//        )
//    }

    static String convertNombreEnMois(Integer mois) throws NumberFormatException {
//        assert mois > 0 && mois < 13: "mois doit etre entre 1 et 12"
        switch (mois) {
            case 1: return Month.JANUARY.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 2: return Month.FEBRUARY.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 3: return Month.MARCH.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 4: return Month.APRIL.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 5: return Month.MAY.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 6: return Month.JUNE.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 7: return Month.JULY.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 8: return Month.AUGUST.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 9: return Month.SEPTEMBER.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 10: return Month.OCTOBER.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 11: return Month.NOVEMBER.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            case 12: return Month.DECEMBER.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            default: throw new IllegalArgumentException("mauvais mois dans la base")
        }
    }

//    static String[] toArrayString(Inter inter) {
//        String[]obj=
//        [inter.nd, inter.type, inter.contrat,
//         inter.heure.hour < 10 ? "0${inter.heure.hour}" : inter.heure.hour,
//         inter.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//         inter.nom.toLowerCase() == "null" || inter.nom == null ? "" : inter.nom,
//         inter.prenom.toLowerCase() == "null" || inter.prenom == null ? "" : inter.prenom]
//        obj
//    }
}
