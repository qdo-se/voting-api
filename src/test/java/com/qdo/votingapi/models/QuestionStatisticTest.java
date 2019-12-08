package com.qdo.votingapi.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class QuestionStatisticTest {
    @Test
    void testCreateQuestionStatistic() {
        int countYes = 113;
        int countNo = 129;

        Question question = new Question(UUID.randomUUID(), "What is it?");

        QuestionStatistic statistic = new QuestionStatistic(question, countYes, countNo);

        Assertions.assertEquals(countYes, statistic.getCountYes());
        Assertions.assertEquals(countNo, statistic.getCountNo());
        Assertions.assertEquals(countYes + countNo, statistic.getTotal());

        Assertions.assertEquals("47%", statistic.getPercentageYes());
        Assertions.assertEquals("53%", statistic.getPercentageNo());
    }

    @Test
    void testCreateQuestionStatisticEmptyCount() {
        int countYes = 0;
        int countNo = 0;

        Question question = new Question(UUID.randomUUID(), "What is it?");

        QuestionStatistic statistic = new QuestionStatistic(question, countYes, countNo);

        Assertions.assertEquals(countYes, statistic.getCountYes());
        Assertions.assertEquals(countNo, statistic.getCountNo());
        Assertions.assertEquals(countYes + countNo, statistic.getTotal());

        Assertions.assertEquals(null, statistic.getPercentageYes());
        Assertions.assertEquals(null, statistic.getPercentageNo());
    }
}
