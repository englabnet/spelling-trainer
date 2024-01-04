package net.englab.spellingtrainer.models;

import net.englab.spellingtrainer.models.entities.PronunciationTrack;

import java.util.Set;

/**
 * Represents a step of a spelling test.
 *
 * @param step                  the current step
 * @param total                 the total number of steps
 * @param wordId                the current word ID
 * @param pronunciationTracks   the pronunciation tracks of the current word
 */
public record TestStep(int step, int total, int wordId, Set<PronunciationTrack> pronunciationTracks) {
}
