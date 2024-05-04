package net.englab.spellingtrainer.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Represents a word entity stored in the database.
 */
@NamedEntityGraph(
        name = "word-with-pronunciation-tracks",
        attributeNodes = @NamedAttributeNode("pronunciationTracks")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pronunciationTracks")
@Entity
public class Word {

    /**
     * The unique identifier of the word.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    /**
     * This string holds the word itself.
     */
    private String text;

    /**
     * A set of pronunciation tracks.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "word_id")
    private Set<PronunciationTrack> pronunciationTracks;
}
