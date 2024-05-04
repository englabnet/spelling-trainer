--liquibase formatted sql

--changeset nikitakuchur:1
CREATE TABLE IF NOT EXISTS word (
    id SERIAL PRIMARY KEY,
    text VARCHAR NOT NULL UNIQUE
)

--changeset nikitakuchur:2
CREATE TABLE IF NOT EXISTS pronunciation_track (
    id SERIAL PRIMARY KEY,
    word_id INT REFERENCES word(id),
    variety VARCHAR NOT NULL,
    filepath VARCHAR NOT NULL
)

--changeset nikitakuchur:3
CREATE TABLE IF NOT EXISTS spelling_test (
    id VARCHAR PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL
)

--changeset nikitakuchur:4
CREATE TABLE IF NOT EXISTS spelling_test_word (
    spelling_test_id VARCHAR REFERENCES spelling_test(id),
    word_id INT REFERENCES word(id),
    PRIMARY KEY(spelling_test_id, word_id)
)
