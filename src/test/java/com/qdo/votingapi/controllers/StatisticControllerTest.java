package com.qdo.votingapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdo.votingapi.dao.*;
import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;
import com.qdo.votingapi.models.QuestionStatistic;
import com.qdo.votingapi.models.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatisticDao statisticDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // RESET DB
    public void getSingleStatistic() throws Exception {
        Question question = questionDao.findAllQuestions().get(0); // get first question

        int countYes = 7;
        int countNo = 5;

        int count = 0;

        for (int i = 0; i < countYes; i++) {
            postVote(new Person(null, "test" + count++), question, true, status().isOk());
        }

        for (int i = 0; i < countNo; i++) {
            postVote(new Person(null, "test" + count++), question, false, status().isOk());
        }

        MvcResult mockResult = mockMvc.perform(get("/api/v1/question/statistic/" + question.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mockResult.getResponse().getContentAsString();

        QuestionStatistic statistic = objectMapper.readValue(responseString, QuestionStatistic.class);

        Assertions.assertNotNull(statistic);
        Assertions.assertEquals(countYes, statistic.getCountYes());
        Assertions.assertEquals(countNo, statistic.getCountNo());
        Assertions.assertEquals("58%", statistic.getPercentageYes());
        Assertions.assertEquals("42%", statistic.getPercentageNo());
    }

    private void postVote(Person person, Question question, boolean answer, ResultMatcher resultMatcher) throws Exception {
        // Convert object to json
        String json = objectMapper.writeValueAsString(
                new Vote(person.getName(), question.getId(), answer)
        );

        mockMvc.perform(post("/api/v1/vote")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(resultMatcher)
                .andReturn();
    }
}
