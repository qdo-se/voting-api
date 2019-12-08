package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {
    int insertPerson(UUID id, Person person);

    default int insertPerson(Person person) {
        UUID id = UUID.randomUUID();
        return insertPerson(id, person);
    }

    List<Person> findAllPeople();

    Optional<Person> findPersonById(UUID id);

    Optional<Person> findPersonByName(String name);
}
