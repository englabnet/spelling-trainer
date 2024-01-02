package net.englab.spellingtrainer.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Represents a word entity stored in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @OneToMany(mappedBy = "wordId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PronunciationTrack> pronunciationTracks;
}
