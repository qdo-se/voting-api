package com.qdo.votingapi.services;

import com.qdo.votingapi.dao.PersonDao;
import com.qdo.votingapi.dao.QuestionDao;
import com.qdo.votingapi.dao.VoteDao;
import com.qdo.votingapi.dao.VoterTrackingDao;
import com.qdo.votingapi.exceptions.ApiConflictException;
import com.qdo.votingapi.exceptions.ApiNotFoundException;
import com.qdo.votingapi.exceptions.ApiTransactionException;
import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;
import com.qdo.votingapi.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {
    private final VoterTrackingDao voterTrackingDao;
    private final VoteDao voteDao;
    private final QuestionDao questionDao;
    private final PersonDao personDao;

    private final static Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    public VoteService(
            VoterTrackingDao voterTrackingDao,
            VoteDao voteDao,
            QuestionDao questionDao,
            PersonDao personDao
    ) {
        this.voterTrackingDao = voterTrackingDao;
        this.voteDao = voteDao;
        this.questionDao = questionDao;
        this.personDao = personDao;
    }

    @Transactional
    public void addVote(Vote vote) {
        try {
            Question question = retrieveQuestion(vote);

            Person person = retrievePerson(vote);

            validateVote(person, question);

            voteDao.insertVote(question, vote.getAnswer());

            voterTrackingDao.insertVoterTracking(person, question);

        } catch (ApiConflictException | ApiNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage());
            throw new ApiTransactionException("Vote transaction failed. " + ex.getMessage());
        }
    }

    private void validateVote(Person person, Question question) {
        boolean alreadyVote = voterTrackingDao.hasVoterTracking(person, question);

        if (alreadyVote) {
            throw new ApiConflictException("User [" + person.getName() + "] already voted for question [" + question.getContent() + "].");
        }
    }

    private Question retrieveQuestion(Vote vote) {
        Optional<Question> questionMaybe = questionDao.findQuestionById(vote.getQuestionId());

        if (questionMaybe.isEmpty()) {
            throw new ApiNotFoundException("Question with id " + vote.getQuestionId() + " could not be found.");
        }

        return questionMaybe.get();
    }

    private Person retrievePerson(Vote vote) {
        Optional<Person> personMaybe = personDao.findPersonByName(vote.getName());

        // Attempt to create a person if not found
        if (personMaybe.isEmpty()) {
            // If more than one thread attempt to create the same person, unique constrain will cause transaction error
            personDao.insertPerson(new Person(null, vote.getName()));

            // Retrieve person only after inserted into database to preserve integrity
            personMaybe = personDao.findPersonByName(vote.getName());

            // Last check if person is still empty, throw exception
            if (personMaybe.isEmpty()) {
                throw new ApiTransactionException("Failed to create a person.");
            }
        }

        return personMaybe.get();
    }
}
