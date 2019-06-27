package com.cheroliv.fiber.web.rest

import com.cheroliv.fiber.domain.Planning
import com.cheroliv.fiber.repository.PlanningRepository
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@CompileStatic
@RestController
@RequestMapping(path = "/plannings",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin(origins = "*")
class PlanningController {
    final PlanningRepository planningRepository

    @Autowired
    PlanningController(PlanningRepository planningRepository) {
        this.planningRepository = planningRepository
    }

    @GetMapping
    Iterable<Planning> get() {
        planningRepository.findAll(
                PageRequest.of(0,
                        10,
                        Sort.by("dateTimeCreation")
                                .descending()))
                .content
    }


    @GetMapping("/{login}")
    Iterable<Planning> getByUser(@PathVariable("login") String login) {
        planningRepository.findByUserLogin(login,
                PageRequest.of(0,
                        10,
                        Sort.by("dateTimeCreation")
                                .descending()))
                .content
    }

    @CompileStatic(value = TypeCheckingMode.SKIP)
    @GetMapping("/{planningId}")
    ResponseEntity<Planning> get(@PathVariable("planningId") Long id) {
        Optional<Planning> result = planningRepository.findById(id)
        result.present ?
                new ResponseEntity<Planning>(result.get(), HttpStatus.OK) :
                new ResponseEntity<Planning>(null, HttpStatus.NOT_FOUND)
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Planning post(@RequestBody Planning planning) {
        planningRepository.save(planning)
    }


    @PutMapping("/{planningId}")
    void put(@RequestBody Planning planning) {
        planningRepository.save(planning)
    }

    @PatchMapping(path = "/{planningId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Planning patch(@PathVariable("{planningId}") Long id,
                   @RequestBody Planning patch) {
        Optional<Planning> result = planningRepository.findById(id)
        if (result.present) {
            Planning planning = result.get()
            if (planning.firstNameTech != null) {
                planning.firstNameTech = patch.firstNameTech
            }
            if (planning.lastNameTech != null) {
                planning.lastNameTech = patch.lastNameTech
            }
            if (planning.initialTech != null) {
                planning.initialTech = patch.initialTech
            }
            if (planning.open != null) {
                planning.open = patch.open
            }
        }
        planningRepository.save(patch)
    }

    @DeleteMapping("/{planningId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void delete(@PathVariable("/{planningId}") Long id) {
        try {
            planningRepository.deleteById(id)
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace()
        }
    }

}
