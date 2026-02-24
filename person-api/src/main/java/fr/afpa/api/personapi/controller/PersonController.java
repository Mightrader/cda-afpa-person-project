package fr.afpa.api.personapi.controller;

import fr.afpa.api.personapi.model.Person;
import fr.afpa.api.personapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public Iterable<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Integer id) {
        return personService.getPersonById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Integer id, @RequestBody Person person) {
        if (!personService.getPersonById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        person.setId(id);
        return personService.savePerson(person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Integer id) {
        if (!personService.getPersonById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }
        personService.deletePerson(id);
    }
}