package com.qdo.votingapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@ApiModel(description = "Person")
public class Person {
    @ApiModelProperty(notes = "Person's id")
    private final UUID id;

    @ApiModelProperty(notes = "Person's name is stored as lower case")
    @NotBlank
    private final String name;

    /**
     * Person constructor
     * @param id Person's id
     * @param name Person's name
     */
    public Person(@JsonProperty("id") UUID id,
                  @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get Person id
     * @return Person id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get Person name
     * @return Person name
     */
    public String getName() {
        return name;
    }
}
