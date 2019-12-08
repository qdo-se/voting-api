package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QuestionDaoSQL implements QuestionDao {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public QuestionDaoSQL(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int insertQuestion(UUID id, Question question) {
        final String sql = "INSERT INTO question (id, content) VALUES (:id, :content)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        namedParameters.addValue("content", question.getContent());

        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public List<Question> findAllQuestions() {
        final String sql = "SELECT id, content FROM question";

        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String content = resultSet.getString("content");
            return new Question(id, content);
        });
    }

    @Override
    public Optional<Question> findQuestionById(UUID id) {
        final String sql = "SELECT id, content FROM question WHERE id = ?";

        List<Question> questions = jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            UUID personId = UUID.fromString(resultSet.getString("id"));
            String content = resultSet.getString("content");
            return new Question(personId, content);
        });

        if (questions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(questions.get(0));
    }
}
