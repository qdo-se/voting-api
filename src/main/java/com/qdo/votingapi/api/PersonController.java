package com.qdo.votingapi.api;

import com.qdo.votingapi.exceptions.ApiNotFoundException;
import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.responses.CustomApiErrorResponse;
import com.qdo.votingapi.responses.CustomApiSuccessResponse;
import com.qdo.votingapi.services.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "Person", description = "Retrieve and add person")
@RequestMapping("api/v1/person")
@RestController
public class PersonController {
    private final PersonService personService;

    /**
     * Person Controller's constructor
     * @param personService Person Service
     */
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Add a new person
     * @param person Person Object
     * @return An api response
     */
    @ApiOperation(value = "Add a new person")
    @PostMapping
    public ResponseEntity<?> addPerson(@Valid @NotNull @RequestBody Person person) {
        String exMessage = "";

        try {
            if (personService.addPerson(person) > 0) {
                return new ResponseEntity<CustomApiSuccessResponse>(
                        new CustomApiSuccessResponse("Added a person."),
                        HttpStatus.OK
                );
            }
        } catch (RuntimeException ex) {
            exMessage = " " + ex.getMessage();
        }

        return new ResponseEntity<CustomApiErrorResponse>(
                new CustomApiErrorResponse("Failed to add a person." + exMessage, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Retrieve all people
     * @return List of Person object
     */
    @ApiOperation(value = "Retrieve all people")
    @GetMapping
    public List<Person> getAllPeople() {
        return personService.getAllPeople();
    }

    /**
     * Retrieve a single person by id
     * @param id UUID
     * @return Person object
     */
    @ApiOperation(value = "Retrieve a person by UUID")
    @GetMapping(path = "{id}")
    public Person getPersonById(@PathVariable("id") UUID id) {
        Optional<Person> personMaybe = personService.getPersonById(id);
        if (personMaybe.isEmpty()) {
            throw new ApiNotFoundException("Person with " + id + " is not found.");
        }
        return personMaybe.get();
    }
}
