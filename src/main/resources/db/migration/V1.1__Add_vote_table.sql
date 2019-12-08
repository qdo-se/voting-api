CREATE TABLE vote (
    id UUID NOT NULL PRIMARY KEY,
    question_id UUID NOT NULL,
    answer BOOLEAN NOT NULL,
    created_at DATE NOT NULL
);