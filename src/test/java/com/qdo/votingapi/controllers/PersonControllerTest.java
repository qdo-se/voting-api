package com.qdo.votingapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdo.votingapi.dao.PersonDao;
import com.qdo.votingapi.models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addPerson() throws Exception {
        int oldSize = personDao.findAllPeople().size();

        String json = objectMapper.writeValueAsString(new Person(null, "Hello World"));

        mockMvc.perform(post("/api/v1/person")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        int newSize = personDao.findAllPeople().size();

        Assertions.assertEquals(oldSize + 1, newSize);
    }

    @Test
    public void getAllPeople() throws Exception {
        MvcResult mockResult = mockMvc.perform(get("/api/v1/person"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mockResult.getResponse().getContentAsString();

        // Convert json array to person array
        Person[] people = objectMapper.readValue(responseString, Person[].class);

        Assertions.assertEquals(personDao.findAllPeople().size(), people.length);
    }
}
