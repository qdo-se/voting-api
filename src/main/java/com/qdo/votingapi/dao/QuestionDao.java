package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionDao {
    int insertQuestion(UUID id, Question question);

    default int insertQuestion(Question question) {
        UUID id = UUID.randomUUID();
        return insertQuestion(id, question);
    }

    List<Question> findAllQuestions();

    Optional<Question> findQuestionById(UUID id);
}
