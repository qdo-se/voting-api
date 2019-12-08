package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PersonDaoSQL implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PersonDaoSQL(JdbcTemplate jdbcTemplate,
                        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Person> findAllPeople() {
        final String sql = "SELECT id, name FROM person";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
    }

    @Override
    public Optional<Person> findPersonById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";

        List<Person> people = jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            UUID personId = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(personId, name);
        });

        if (people.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(people.get(0));
    }

    @Override
    public Optional<Person> findPersonByName(String searchName) {
        final String sql = "SELECT id, name FROM person WHERE LOWER(name) = LOWER(?)";

        List<Person> people = jdbcTemplate.query(
                sql,
                new Object[]{searchName},
                (resultSet, i) -> {
                    UUID personId = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    return new Person(personId, name);
                }
        );

        if (people.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(people.get(0));
    }

    @Override
    public int insertPerson(UUID id, Person newPerson) {
        final String sql = "INSERT INTO person (id, name) VALUES (:id, LOWER(:name))";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("name", newPerson.getName());
        namedParameters.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }
}
