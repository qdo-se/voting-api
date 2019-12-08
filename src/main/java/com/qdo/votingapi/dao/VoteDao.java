package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteDao {
    int insertVote(UUID id, Question question, boolean vote);

    default int insertVote(Question question, boolean vote) {
        UUID id = UUID.randomUUID();
        return insertVote(id, question, vote);
    }

    int countTotalVote();
}

