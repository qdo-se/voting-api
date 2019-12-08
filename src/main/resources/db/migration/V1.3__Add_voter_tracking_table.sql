CREATE TABLE voter_tracking (
    id UUID NOT NULL PRIMARY KEY,
    person_id UUID NOT NULL,
    question_id UUID NOT NULL,

    UNIQUE(person_id, question_id)
);