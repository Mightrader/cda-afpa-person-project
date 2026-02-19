package fr.afpa.web.personweb.controller;

import fr.afpa.web.personweb.model.Person;
import fr.afpa.web.personweb.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("persons", personService.getAllPersons());
        model.addAttribute("person", new Person());
        return "index";
    }

    @PostMapping("/add")
    public String addPerson(@ModelAttribute Person person) {
        personService.createPerson(person);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable Integer id) {
        personService.deletePerson(id);
        return "redirect:/";
    }
}
