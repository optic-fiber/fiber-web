package com.cheroliv.fiber.domain

import com.cheroliv.fiber.domain.enumeration.ContractEnum
import com.cheroliv.fiber.domain.enumeration.TypeInterEnum
import com.cheroliv.fiber.groups.InterChecks
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Slf4j
@CompileStatic
@TestMethodOrder(MethodOrderer.OrderAnnotation)
class InterTest {

    static Validator validator

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    @Order(1)
    @DisplayName("InterTest.testNdSizeConstraint")
    void testNdSizeConstraint() {
        Inter inter = new Inter(nd: "101010101")
        assert 10 != inter.nd.size()
        Set<ConstraintViolation<Inter>> constraintViolations =
                validator.validateProperty inter, "nd", InterChecks
        assert constraintViolations
                .iterator()
                .next()
                .messageTemplate ==
                InterConstants.ND_SIZE_CSTRT_TPL_MSG
        inter.nd = "0101010101"
        assert inter.nd.size() == 10
        constraintViolations =
                validator.validateProperty inter, "nd", InterChecks
        assert 0 == constraintViolations.size()
    }

    /**
     * InterTest :
     *  test building a valid inter with the CSV file
     *  get a ZonedDateTime with a LocalDate and a LocalTime
     *  testing valueOfName method of the enums
     *  testing InterUtils.getDateTime()
     *
     * @param id
     * @param nd
     * @param nom
     * @param prenom
     * @param heure
     * @param date
     * @param contrat
     * @param type
     */
    @Order(2)
    @ParameterizedTest()
    @DisplayName("InterTest.testBuildingInterWithCsv")
    @CsvFileSource(resources = "/inter.csv", numLinesToSkip = 1)
    void testBuildingInterWithCsv(Integer id,
                                  String nd,
                                  String nom,
                                  String prenom,
                                  String heure,
                                  LocalDate date,
                                  String contrat,
                                  String type) {

        LocalDate currentLocalDate = date
        LocalTime currentLocalTime = InterUtils.parseStringHeureToLocalTime(heure)
        ZonedDateTime currentZonedDateTime = InterUtils.getDateTime(
                currentLocalDate,
                currentLocalTime,
                ZoneId.systemDefault())

        Inter interCsv = new Inter(
                id: id,
                nd: nd,
                lastNameClient: nom,
                firstNameClient: prenom,
                dateTimeInter: currentZonedDateTime,
                contract: ContractEnum.valueOfName(contrat),
                typeInter: TypeInterEnum.valueOfName(type))

        //invalid! because Planning is null
        // and belong to the default validation check group
        assert !validator.validateProperty(interCsv, "planning").empty
        //correct! because we only validate the inter not the relation
        assert validator.validate(interCsv, InterChecks).empty
    }
//
//    @ParameterizedTest()
//    @DisplayName("InterTest.testToArrayString")
//    @CsvFileSource(resources = "/inters.csv", numLinesToSkip = 1)
//    void testToArrayString_ArrayContent(Integer id,
//                                        String nd,
//                                        String nom,
//                                        String prenom,
//                                        String heure,
//                                        LocalDate date,
//                                        String contrat,
//                                        String type) {
//        Inter interCsv = new Inter(
//                id: id,
//                nd: nd,
//                nom: nom,
//                prenom: prenom,
//                heure: parseStringHeureToInteger(heure),
//                date: date,
//                contrat: contrat,
//                type: type)
//        //les elements du tableau sont egaux aux parametres du test et bien ordonné
//        //tel que: [nd, type, contrat, heure, date, nom, prenom]
//        assertEquals interCsv.nd, interCsv.toArrayString()[0]
//        assertEquals interCsv.type, interCsv.toArrayString()[1]
//        assertEquals interCsv.contrat, interCsv.toArrayString()[2]
//        assertEquals interCsv.heure, parseInt(interCsv.toArrayString()[3])
//        assertEquals interCsv.date.format(ofPattern("dd/MM/yyyy")),
//                interCsv.toArrayString()[4]
//        assertEquals interCsv.nom.toLowerCase() == "null" ?
//                "" : interCsv.nom, interCsv.toArrayString()[5]
//        assertEquals interCsv.prenom.toLowerCase() == "null" ?
//                "" : interCsv.prenom, interCsv.toArrayString()[6]
//    }

//    @Test
//    @DisplayName("InterTest.testIdMinConstraint")
//    void testIdMinConstraint() {
//        Inter inter = new Inter(id: INTER_ID_MIN_VALUE - 1)
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "id"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                MIN_CSTRT_TEMPLATE_MSG
//
//
//        inter.id = 259
//        constraintViolations =
//                validator.validateProperty inter, "id"
//        assertEquals constraintViolations.size(), 0
//    }

//    @Test
//    @DisplayName("InterTest.testNdNotNullConstraint")
//    void testNdNotNullConstraint() {
//        Inter inter = new Inter()
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "nd"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                NOT_NULL_CSTRT_TEMPLATE_MSG
//
//        inter.nd = "0101010101"
//        constraintViolations =
//                validator.validateProperty inter, "nd"
//        assertEquals constraintViolations.size(), 0
//    }


//
//    @Test
//    @DisplayName("InterTest.testTypeNotNullConstraint")
//    void testTypeNotNullConstraint() {
//        Inter inter = new Inter()
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                NOT_NULL_CSTRT_TEMPLATE_MSG
//
//        inter.type = "BAAP"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testTypePatternConstraint")
//    void testTypePatternConstraint() {
//        Inter inter = new Inter(type: "foo")
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                PATTERN_CSTRT_TEMPLATE_MSG
//
//        inter.type = "BAAP"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//        inter.type = "BAOC"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//        inter.type = "BAFA"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//        inter.type = "BAST"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//        inter.type = "PLP"
//        constraintViolations =
//                validator.validateProperty inter, "type"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testContratNotNullConstraint")
//    void testContratNotNullConstraint() {
//        Inter inter = new Inter()
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                NOT_NULL_CSTRT_TEMPLATE_MSG
//
//        inter.contrat = "LM"
//        constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testContratPatternConstraint")
//    void testContratPatternConstraint() {
//        Inter inter = new Inter(contrat: "foo")
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                PATTERN_CSTRT_TEMPLATE_MSG
//
//        inter.contrat = "LM"
//        constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations.size(), 0
//        inter.contrat = "IQ"
//        constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations.size(), 0
//        inter.contrat = "Passage de cable"
//        constraintViolations =
//                validator.validateProperty inter, "contrat"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testHeureNotNullConstraint")
//    void testHeureNotNullConstraint() {
//        Inter inter = new Inter()
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                NOT_NULL_CSTRT_TEMPLATE_MSG
//
//        inter.heure = 9
//        constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testHeureMinConstraint")
//    void testHeureMinConstraint() {
//        Inter inter = new Inter(heure: HEURE_MIN_VALUE - 1)
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                MIN_CSTRT_TEMPLATE_MSG
//
//
//        inter.heure = HEURE_MIN_VALUE
//        constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testHeureMaxConstraint")
//    void testHeureMaxConstraint() {
//        Inter inter = new Inter(heure: HEURE_MAX_VALUE + 1)
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                MAX_CSTRT_TEMPLATE_MSG
//
//
//        inter.heure = HEURE_MAX_VALUE
//        constraintViolations =
//                validator.validateProperty inter, "heure"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testDateNotNullConstraint")
//    void testDateNotNullConstraint() {
//        Inter inter = new Inter()
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "date"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                NOT_NULL_CSTRT_TEMPLATE_MSG
//
//        inter.date = LocalDate.now()
//        constraintViolations =
//                validator.validateProperty inter, "date"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testPrenomSizeConstraint")
//    void testPrenomSizeConstraint() {
//        String prenom = ""
//        for (int i = 0; prenom.size() <= PRENOM_SIZE_VALUE; i++) {
//            prenom = prenom + i.toString()
//        }
//        assertFalse prenom.size() <= PRENOM_SIZE_VALUE
//        Inter inter = new Inter(firstNameClient: prenom)
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "prenom"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                SIZE_CSTRT_TEMPLATE_MSG
//
//        inter.prenom = "John"
//        constraintViolations =
//                validator.validateProperty inter, "prenom"
//        assertEquals constraintViolations.size(), 0
//    }
//
//    @Test
//    @DisplayName("InterTest.testNomSizeConstraint")
//    void testNomSizeConstraint() {
//        String nom = ""
//        for (int i = 0; nom.size() <= NOM_SIZE_VALUE; i++) {
//            nom = nom + i.toString()
//        }
//        assertFalse nom.size() <= NOM_SIZE_VALUE
//        Inter inter = new Inter(nom: nom)
//        Set<ConstraintViolation<Inter>> constraintViolations =
//                validator.validateProperty inter, "nom"
//        assertEquals constraintViolations
//                .iterator()
//                .next()
//                .messageTemplate,
//                SIZE_CSTRT_TEMPLATE_MSG
//
//        inter.nom = "Doe"
//        constraintViolations =
//                validator.validateProperty inter, "nom"
//        assertEquals constraintViolations.size(), 0
//    }
}
