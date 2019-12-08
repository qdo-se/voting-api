package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Question;
import com.qdo.votingapi.models.QuestionStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StatisticDaoSQL implements StatisticDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatisticDaoSQL(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<QuestionStatistic> findAllQuestionVoteCounts() {
        return namedParameterJdbcTemplate.query(getSelectQuery(), (resultSet, i) -> {
            UUID questionId = UUID.fromString(resultSet.getString("id"));
            String questionContent = resultSet.getString("content");
            int countYes = resultSet.getInt("count_yes");
            int countNo = resultSet.getInt("count_no");
            return new QuestionStatistic(new Question(questionId, questionContent), countYes, countNo);
        });
    }

    @Override
    public Optional<QuestionStatistic> findQuestionVoteCount(UUID id) {
        final String query = getSelectQuery() + " WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        List<QuestionStatistic> questionStatistics = namedParameterJdbcTemplate.query(
                query,
                namedParameters,
                (resultSet, i) -> {
                    UUID questionId = UUID.fromString(resultSet.getString("id"));
                    String questionContent = resultSet.getString("content");
                    int countYes = resultSet.getInt("count_yes");
                    int countNo = resultSet.getInt("count_no");
                    return new QuestionStatistic(new Question(questionId, questionContent), countYes, countNo);
                }
        );

        if (questionStatistics.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(questionStatistics.get(0));
    }

    private String getSelectQuery() {
        return "SELECT id, content"
                + ", (select count(*) from vote where vote.question_id = question.id and vote.answer = 1) as count_yes"
                + ", (select count(*) from vote where vote.question_id = question.id and vote.answer = 0) as count_no"
                + " FROM question";
    }
}
