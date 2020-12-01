package com.qdo.votingapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class Vote {
    @NotBlank(message = "name is mandatory")
    @ApiModelProperty(notes = "Person's name")
    private final String name;

    @NotNull
    @ApiModelProperty(notes = "Question's id")
    private final UUID questionId;

    @NotNull
    @ApiModelProperty(notes = "Person's answer for the question")
    private final boolean answer;

    /**
     * Vote constructor
     * @param name Person's or Voter's name
     * @param questionId Question Id
     * @param answer Answer Yes or No
     */
    public Vote(
            @JsonProperty("name") String name,
            @JsonProperty("questionId") UUID questionId,
            @JsonProperty("answer") boolean answer
    ) {
        this.questionId = questionId;
        this.name = name;
        this.answer = answer;
    }

    /**
     * Get Question Id
     * @return Question Id
     */
    public UUID getQuestionId() {
        return questionId;
    }

    /**
     * Get Person's name
     * @return Person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get Vote
     * @return vote Yes or No
     */
    public boolean getAnswer() {
        return answer;
    }
}
