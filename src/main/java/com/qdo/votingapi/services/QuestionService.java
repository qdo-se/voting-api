package com.qdo.votingapi.services;

import com.qdo.votingapi.dao.QuestionDao;
import com.qdo.votingapi.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {
    private final QuestionDao questionDao;

    @Autowired
    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public int addQuestion(Question question) {
        return questionDao.insertQuestion(question);
    }

    public List<Question> getAllQuestions() {
        return questionDao.findAllQuestions();
    }

    public Optional<Question> getQuestionById(UUID id) {
        return questionDao.findQuestionById(id);
    }
}
