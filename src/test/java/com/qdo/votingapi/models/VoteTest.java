package com.qdo.votingapi.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

public class VoteTest {
    @Test
    void testCreateVote() {
        UUID questionId = UUID.randomUUID();
        String name = "Joe Doe";
        boolean answer = (new Random()).nextBoolean();

        Vote vote = new Vote(name, questionId, answer);

        Assertions.assertEquals(name, vote.getName());
        Assertions.assertEquals(questionId, vote.getQuestionId());
        Assertions.assertEquals(answer, vote.getAnswer());
    }
}
