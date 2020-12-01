package com.qdo.votingapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Question {
    @ApiModelProperty(notes = "Question's id", hidden = true)
    private final UUID id;

    @NotBlank
    @ApiModelProperty(notes = "Question's content")
    private final String content;

    /**
     * Question constructor
     * @param id Question's UUID
     * @param content Question's content
     */
    public Question(@JsonProperty("id") UUID id,
                    @JsonProperty("content") String content) {
        this.id = id;
        this.content = content;
    }

    /**
     * Get Question's is
     * @return Question's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get Question's content
     * @return Question's content
     */
    public String getContent() {
        return content;
    }
}
