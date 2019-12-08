package com.qdo.votingapi.services;

import com.qdo.votingapi.dao.StatisticDao;
import com.qdo.votingapi.models.QuestionStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatisticService {
    private final StatisticDao statisticDao;

    @Autowired
    public StatisticService(StatisticDao statisticDao) {
        this.statisticDao = statisticDao;
    }

    public List<QuestionStatistic> getAllQuestionVoteCounts() {
        return statisticDao.findAllQuestionVoteCounts();
    }

    public Optional<QuestionStatistic> getQuestionVoteCount(UUID id) {
        return statisticDao.findQuestionVoteCount(id);
    }
}
