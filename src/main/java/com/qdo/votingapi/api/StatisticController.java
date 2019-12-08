package com.qdo.votingapi.api;

import com.qdo.votingapi.exceptions.ApiNotFoundException;
import com.qdo.votingapi.models.QuestionStatistic;
import com.qdo.votingapi.services.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "Statistic", description = "Retrieve some facts about questions")
@RequestMapping("api/v1/question/statistic")
@RestController
public class StatisticController {
    private final StatisticService statisticService;

    /**
     * Statistic Controller's constructor
     * @param statisticService Statistic Service
     */
    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    /**
     * Get statistic for all questions
     * @return List of Question Statistic objects
     */
    @ApiOperation(value = "Get statistic for all questions")
    @GetMapping
    public List<QuestionStatistic> getAllQuestionVoteCounts() {
        return statisticService.getAllQuestionVoteCounts();
    }

    /**
     * Get statistic for a question
     * @return Question Statistic object
     */
    @ApiOperation(value = "Get statistic for a specific questions by UUID")
    @GetMapping(path = "{id}")
    public QuestionStatistic getQuestionVoteCount(@PathVariable("id") UUID id) {
        Optional<QuestionStatistic> questionMaybe = statisticService.getQuestionVoteCount(id);

        if (questionMaybe.isEmpty()) {
            throw new ApiNotFoundException("Question with " + id + " is not found.");
        }

        return questionMaybe.get();
    }
}
