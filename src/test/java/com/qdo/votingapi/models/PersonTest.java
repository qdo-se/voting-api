package com.qdo.votingapi.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class PersonTest {
    @Test
    void testCreatePerson() {
        UUID id = UUID.randomUUID();
        String name = "John Doe";

        Person person = new Person(id, name);

        Assertions.assertEquals(id, person.getId());
        Assertions.assertEquals(name, person.getName());
    }
}
