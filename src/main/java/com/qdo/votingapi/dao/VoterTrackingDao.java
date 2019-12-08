package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface VoterTrackingDao {
    int insertVoterTracking(UUID id, @NotNull Person person, @NotNull Question question);

    default int insertVoterTracking(Person person, Question question) {
        UUID id = UUID.randomUUID();
        return insertVoterTracking(id, person, question);
    }

    boolean hasVoterTracking(Person person, Question question);
}
