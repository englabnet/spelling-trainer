--liquibase formatted sql

--changeset nikitakuchur:1
CREATE TABLE IF NOT EXISTS word (
    id SERIAL NOT NULL PRIMARY KEY,
    text VARCHAR NOT NULL UNIQUE
)

--changeset nikitakuchur:2
CREATE TABLE IF NOT EXISTS pronunciation_track (
    word_id SERIAL NOT NULL,
    variety VARCHAR NOT NULL,
    filepath VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY(word_id, variety),
    CONSTRAINT fk_word FOREIGN KEY(word_id) REFERENCES word(id)
)

--changeset nikitakuchur:3
CREATE TABLE IF NOT EXISTS spelling_test (
    id VARCHAR NOT NULL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL
)

--changeset nikitakuchur:4
CREATE TABLE IF NOT EXISTS spelling_test_words (
    spelling_test_id VARCHAR NOT NULL,
    words_id SERIAL NOT NULL,
    PRIMARY KEY(spelling_test_id, words_id),
    CONSTRAINT fk_spelling_test FOREIGN KEY(spelling_test_id) REFERENCES spelling_test(id),
    CONSTRAINT fk_word FOREIGN KEY(words_id) REFERENCES word(id)
)
