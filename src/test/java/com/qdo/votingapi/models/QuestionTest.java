package com.qdo.votingapi.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class QuestionTest {
    @Test
    void testCreateQuestion() {
        UUID id = UUID.randomUUID();
        String content = "Do you like test?";

        Question question = new Question(id, content);

        Assertions.assertEquals(id, question.getId());
        Assertions.assertEquals(content, question.getContent());
    }
}
