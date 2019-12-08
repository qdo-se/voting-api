package com.qdo.votingapi.dao;

import com.qdo.votingapi.models.QuestionStatistic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StatisticDao {
    List<QuestionStatistic> findAllQuestionVoteCounts();

    Optional<QuestionStatistic> findQuestionVoteCount(UUID id);
}
