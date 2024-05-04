package net.englab.spellingtrainer.models.entities;

import jakarta.persistence.*;
import lombok.*;
import net.englab.spellingtrainer.models.EnglishVariety;

/**
 * Represents a pronunciation track entity stored in the database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PronunciationTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The variety of English which is a part of the composite key.
     */
    @Enumerated(value = EnumType.STRING)
    private EnglishVariety variety;

    /**
     * The path to the audio file.
     */
    private String filepath;
}
