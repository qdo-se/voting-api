package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Repository
public class VoterTrackingDaoSQL implements VoterTrackingDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public VoterTrackingDaoSQL(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int insertVoterTracking(UUID id, @NotNull Person person, @NotNull Question question) {
        final String sql = "INSERT INTO voter_tracking (id, person_id, question_id)"
                + " VALUES (:id, :person_id, :question_id)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        namedParameters.addValue("person_id", person.getId());
        namedParameters.addValue("question_id", question.getId());

        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public boolean hasVoterTracking(Person person, Question question) {
        final String sql = "SELECT count(*) FROM voter_tracking WHERE person_id = :person_id AND question_id = :question_id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("person_id", person.getId());
        namedParameters.addValue("question_id", question.getId());

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);

        return count != null && count > 0;
    }
}
