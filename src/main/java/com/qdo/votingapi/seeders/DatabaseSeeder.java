package com.qdo.votingapi.seeders;

import com.qdo.votingapi.dao.PersonDao;
import com.qdo.votingapi.dao.QuestionDao;
import com.qdo.votingapi.models.Person;
import com.qdo.votingapi.models.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class DatabaseSeeder {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final PersonDao personDao;
    private final QuestionDao questionDao;

    @Autowired
    public DatabaseSeeder(PersonDao personDao, QuestionDao questionDao) {
        this.personDao = personDao;
        this.questionDao = questionDao;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedPersonTable();
        seedQuestionTable();
    }

    private void seedPersonTable() {
        int size = this.personDao.findAllPeople().size();

        if (size == 0) {
            this.personDao.insertPerson(new Person(null, "James Bones"));
            this.personDao.insertPerson(new Person(null, "Max Payne"));
            this.personDao.insertPerson(new Person(null, "Ainz Ooal Gown"));
            this.personDao.insertPerson(new Person(null, "Link"));
            this.personDao.insertPerson(new Person(null, "Quang Do"));

            logger.info("User Seeding is completed.");
        } else {
            logger.info("User Seeding is not needed.");
        }
    }

    private void seedQuestionTable() {
        int size = this.questionDao.findAllQuestions().size();

        if (size == 0) {
            this.questionDao.insertQuestion(new Question(null, "Do you have a car?"));
            this.questionDao.insertQuestion(new Question(null, "Do you own a house?"));
            this.questionDao.insertQuestion(new Question(null, "Do you know how to code?"));
            this.questionDao.insertQuestion(new Question(null, "Do you like durian?"));
            this.questionDao.insertQuestion(new Question(null, "Do you like board games?"));

            logger.info("Question Seeding is completed.");
        } else {
            logger.info("Question Seeding is not needed.");
        }
    }
}
