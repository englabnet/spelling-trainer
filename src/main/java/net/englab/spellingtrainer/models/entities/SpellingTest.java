package net.englab.spellingtrainer.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

/**
 * Represents a spelling test entity stored in the database.
 */
@NamedEntityGraph(
        name = "spelling-test-with-words-and-pronunciation-tracks",
        attributeNodes = @NamedAttributeNode(value = "words", subgraph = "word-with-pronunciation-tracks"),
        subgraphs = @NamedSubgraph(name = "word-with-pronunciation-tracks", attributeNodes = @NamedAttributeNode("pronunciationTracks"))
)
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "spelling_test_word",
            joinColumns = @JoinColumn(name = "spelling_test_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    @OrderBy("text ASC")
    private Set<Word> words;

    /**
     * The time when the test was created.
     */
    private Instant timestamp;
}
