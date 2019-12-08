package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class VoteDaoSQL implements VoteDao {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public VoteDaoSQL(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int insertVote(UUID id, Question question, boolean answer) {
        final String sql = "INSERT INTO vote (id, question_id, answer, created_at)"
                + " VALUES (:id, :question_id, :answer, :created_at)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        namedParameters.addValue("question_id", question.getId());
        namedParameters.addValue("answer", answer, Types.BOOLEAN);
        namedParameters.addValue("created_at", LocalDateTime.now(), Types.DATE);

        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public int countTotalVote() {
        final String sql = "SELECT count(*) FROM vote";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        return count != null ? count : 0;
    }
}
