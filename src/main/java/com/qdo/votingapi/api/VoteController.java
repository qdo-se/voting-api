package com.qdo.votingapi.api;

import com.qdo.votingapi.models.Vote;
import com.qdo.votingapi.responses.CustomApiSuccessResponse;
import com.qdo.votingapi.services.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Vote", description = "Record vote made by a person to a question")
@RequestMapping("api/v1/vote")
@RestController
public class VoteController {
    private final VoteService voteService;

    /**
     * Vote Controller's constructor
     * @param voteService Vote Service
     */
    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Record a vote made by a person to a question
     * @param vote Vote's object
     * @return A api response
     */
    @ApiOperation(value = "Record a vote made by a person to a question")
    @PostMapping
    public ResponseEntity<?> addVote(@Valid @NotNull @RequestBody Vote vote) {
        synchronized (voteService) {
            voteService.addVote(vote);
        }

        return new ResponseEntity<CustomApiSuccessResponse>(
                new CustomApiSuccessResponse("A vote is recorded."),
                HttpStatus.OK
        );
    }
}

