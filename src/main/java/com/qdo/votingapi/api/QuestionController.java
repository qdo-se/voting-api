package com.qdo.votingapi.api;

import com.qdo.votingapi.exceptions.ApiNotFoundException;
import com.qdo.votingapi.models.Question;
import com.qdo.votingapi.responses.CustomApiErrorResponse;
import com.qdo.votingapi.responses.CustomApiSuccessResponse;
import com.qdo.votingapi.services.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "Question", description = "Retrieve and add question")
@RequestMapping("api/v1/question")
@RestController
public class QuestionController {
    private final QuestionService questionService;

    /**
     * Question Controller's constructor
     * @param questionService Question Service
     */
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Add a new question
     * @param question Question object
     * @return An api response
     */
    @ApiOperation(value = "Add a new question")
    @PostMapping
    public ResponseEntity<?> addQuestion(@Valid @NotNull @RequestBody Question question) {
        String exMessage = "";

        try {
            if (questionService.addQuestion(question) > 0) {
                return new ResponseEntity<CustomApiSuccessResponse>(
                        new CustomApiSuccessResponse("Added a question."),
                        HttpStatus.OK
                );
            }
        } catch (RuntimeException ex) {
            exMessage = " " + ex.getMessage();
        }

        return new ResponseEntity<CustomApiErrorResponse>(
                new CustomApiErrorResponse("Failed to add a question." + exMessage, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Get all questions
     * @return List of Question objects
     */
    @ApiOperation(value = "Retrieve all questions")
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    /**
     * Get a question by id
     * @param id Question's UUID
     * @return Question Object
     */
    @ApiOperation(value = "Retrieve a question by a specific id")
    @GetMapping(path = "{id}")
    public Question getQuestionById(@PathVariable("id") UUID id) {
        Optional<Question> questionMaybe = questionService.getQuestionById(id);

        if (questionMaybe.isEmpty()) {
            throw new ApiNotFoundException("Person with " + id + " is not found.");
        }

        return questionMaybe.get();
    }
}
