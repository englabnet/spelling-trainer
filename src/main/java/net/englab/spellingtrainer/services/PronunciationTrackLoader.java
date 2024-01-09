package net.englab.spellingtrainer.services;

import lombok.RequiredArgsConstructor;
import net.englab.spellingtrainer.models.EnglishVariety;
import net.englab.spellingtrainer.models.entities.PronunciationTrack;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.repositories.WordRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * A small service that loads pronunciation tracks if necessary.
 */
@Service
@RequiredArgsConstructor
public class PronunciationTrackLoader {

    private final TextToSpeechService textToSpeechService;
    private final WordRepository wordRepository;

    /**
     * Loads pronunciation tracks for the given words if they don't have them.
     * This method is async.
     *
     * @param words a list of the words
     */
    @Async
    public void loadPronunciationTracks(List<Word> words) {
        List<Word> wordsWithoutAudio = words.stream()
                .filter(word -> word.getPronunciationTracks().isEmpty())
                .toList();

        for (Word word : wordsWithoutAudio) {
            Set<PronunciationTrack> pronunciationTracks = word.getPronunciationTracks();

            String ukFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.UK);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.UK, ukFile));
            String usFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.US);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.US, usFile));
        }
        wordRepository.saveAll(wordsWithoutAudio);
    }
}
