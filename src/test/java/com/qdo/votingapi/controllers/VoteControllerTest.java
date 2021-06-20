package com.qdo.votingapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdo.votingapi.dao.PersonDao;
import com.qdo.votingapi.dao.QuestionDao;
import com.qdo.votingapi.dao.VoteDao;
import com.qdo.votingapi.dao.VoterTrackingDao;
import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;
import com.qdo.votingapi.models.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private VoterTrackingDao voterTrackingDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ObjectMapper objectMapper;

    private static Random random;

    @BeforeEach
    public void setUp() {
        random = new Random();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // RESET DB
    public void addVote() throws Exception {
        int oldVoteCount = voteDao.countTotalVote();

        Question question = questionDao.findAllQuestions().get(0); // get first question
        Person person = personDao.findAllPeople().get(0); // get first person

        postVote(person, question, status().isOk());

        int newVoteCount = voteDao.countTotalVote();

        Assertions.assertEquals(oldVoteCount + 1, newVoteCount);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // RESET DB
    public void addVoteConflict() throws Exception {
        int oldVoteCount = voteDao.countTotalVote();

        Question question = questionDao.findAllQuestions().get(0); // get first question
        Person person = personDao.findAllPeople().get(0); // get first person

        postVote(person, question, status().isOk());

        postVote(person, question, status().isConflict());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // RESET DB
    public void addVoteNewPerson() throws Exception {
        // Confirm there is no stranger
        Optional<Person> stranger = personDao.findPersonByName("Stranger");
        Assertions.assertTrue(stranger.isEmpty());

        // A stranger votes for a question
        Person person = new Person(null, "Stranger");
        Question question = questionDao.findAllQuestions().get(0); // get first question

        // Auto creating an account for him
        postVote(person, question, status().isOk());

        // Check stranger account is created
        stranger = personDao.findPersonByName("Stranger");
        Assertions.assertTrue(stranger.isPresent());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // RESET DB
    public void addVoteConcurrency() throws Exception {
        seedQuestionTable();
        seedPersonTable();

        List<Question> questionList = questionDao.findAllQuestions();

        List<Person> personList = personDao.findAllPeople();

        int numberOfThreads = questionList.size() * personList.size();


        // For tracking threads
        AtomicInteger runningCount = new AtomicInteger();
        AtomicInteger overlapCount = new AtomicInteger();
        AtomicInteger executedFutureCount = new AtomicInteger();

        // Create a service for execution of tasks in async mode
        // -> Future = represents a future result of an asynchronous computation
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        // A list of futures which will be created by Executor Service
        Collection<Future<Integer>> futures = new ArrayList<>(numberOfThreads);

        // A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.
        CountDownLatch startSignal = new CountDownLatch(1);


        for (Person person : personList) {
            for (Question question : questionList) {
                futures.add(
                        service.submit(
                                () -> {
                                    // Wait for signal to run concurrently
                                    startSignal.await();

                                    if (runningCount.get() > 0) {
//                                        System.err.println("Running count: " + runningCount.get());
                                        overlapCount.incrementAndGet();
                                    }

                                    runningCount.incrementAndGet();
                                    postVote(person, question, status().isOk());
                                    runningCount.decrementAndGet();

                                    return executedFutureCount.incrementAndGet();
                                }
                        ));
            }
        }

        // Open the floodgate!
        startSignal.countDown();

        // Wait for all threads to finish
        // Thread.sleep(100);

        // Delay technique to wait for all threads to finish
        for (Future<Integer> f : futures) {
            f.get();
        }

        Assertions.assertTrue(runningCount.get() == 0, "Some threads are still running.");
        Assertions.assertEquals(numberOfThreads, executedFutureCount.get(), "Some threads did not finished executing.");
        Assertions.assertTrue(overlapCount.get() > 0, "Concurrent threads do not exist.");

        int actualVoteCount = voteDao.countTotalVote();

        Assertions.assertEquals(numberOfThreads, actualVoteCount);
    }


    private void postVote(Person person, Question question, ResultMatcher resultMatcher) throws Exception {
        boolean answer = random.nextBoolean();

        // Convert object to json
        String json = objectMapper.writeValueAsString(
                new Vote(person.getName(), question.getId(), answer)
        );

        mockMvc.perform(post("/api/v1/vote")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
//                .andDo(print())
                .andExpect(resultMatcher)
                .andReturn();
    }

    private void seedPersonTable() {
        this.personDao.insertPerson(new Person(null, "personA"));
        this.personDao.insertPerson(new Person(null, "personB"));
        this.personDao.insertPerson(new Person(null, "personC"));
        this.personDao.insertPerson(new Person(null, "personD"));
        this.personDao.insertPerson(new Person(null, "personE"));
    }

    private void seedQuestionTable() {
        this.questionDao.insertQuestion(new Question(null, "questionA?"));
        this.questionDao.insertQuestion(new Question(null, "questionB?"));
        this.questionDao.insertQuestion(new Question(null, "questionC?"));
        this.questionDao.insertQuestion(new Question(null, "questionD?"));
        this.questionDao.insertQuestion(new Question(null, "questionE?"));
    }
}
