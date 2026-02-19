package fr.afpa.web.personweb.service;

import fr.afpa.web.personweb.model.Person;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PersonService {

    private static final String API_URL = "http://localhost:9000/persons";

    private final RestTemplate restTemplate = new RestTemplate();

    public Person[] getAllPersons() {
        return restTemplate.getForObject(API_URL, Person[].class);
    }

    public void createPerson(Person person) {
        restTemplate.postForObject(API_URL, person, Person.class);
    }

    public void deletePerson(Integer id) {
        restTemplate.delete(API_URL + "/" + id);
    }
}
