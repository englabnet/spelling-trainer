package net.englab.spellingtrainer.services;

import lombok.RequiredArgsConstructor;
import net.englab.spellingtrainer.models.EnglishVariety;
import net.englab.spellingtrainer.models.entities.PronunciationTrack;
import net.englab.spellingtrainer.models.entities.Word;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * A small service that loads pronunciation tracks for words.
 */
@Service
@RequiredArgsConstructor
public class PronunciationTrackLoader {

    private final TextToSpeechService textToSpeechService;

    /**
     * Loads pronunciation tracks for the given words.
     * This method is async.
     *
     * @param words a list of words
     * @return  a completable future with a map that contains a word ID as a key
     *          and the corresponding set of pronunciation tracks as a value
     */
    @Async
    public CompletableFuture<Map<Integer, Set<PronunciationTrack>>> loadPronunciationTracks(List<Word> words) {
        Map<Integer, Set<PronunciationTrack>> result = new HashMap<>();
        for (Word word : words) {
            Set<PronunciationTrack> pronunciationTracks = new HashSet<>();

            String ukFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.UK);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.UK, ukFile));
            String usFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.US);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.US, usFile));

            result.put(word.getId(), pronunciationTracks);
        }
        return CompletableFuture.completedFuture(result);
    }
}
