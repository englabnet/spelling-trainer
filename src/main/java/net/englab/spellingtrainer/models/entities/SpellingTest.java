package net.englab.spellingtrainer.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

/**
 * Represents a spelling test entity stored in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SpellingTest {

    /**
     * The unique identifier of the spelling test.
     */
    @Id
    private String id;

    /**
     * A set of words that are in the test.
     */
    @ManyToMany
    private Set<Word> words;

    /**
     * The time when the test was created.
     */
    private Instant timestamp;
}
