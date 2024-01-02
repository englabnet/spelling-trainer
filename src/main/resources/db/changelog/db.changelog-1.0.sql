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
