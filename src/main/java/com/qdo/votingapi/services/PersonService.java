package com.qdo.votingapi.services;

import com.qdo.votingapi.dao.PersonDao;
import com.qdo.votingapi.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public int addPerson(Person person) {
        return personDao.insertPerson(person);
    }

    public List<Person> getAllPeople() {
        return personDao.findAllPeople();
    }

    public Optional<Person> getPersonById(UUID id) {
        return personDao.findPersonById(id);
    }
}


