package net.englab.spellingtrainer.services;

import jakarta.transaction.Transactional;
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
 * A small service that loads pronunciation tracks for words.
 */
@Service
@RequiredArgsConstructor
public class PronunciationTrackLoader {

    private final TextToSpeechService textToSpeechService;
    private final WordRepository wordRepository;

    /**
     * Loads pronunciation tracks for the given words.
     * This method is async.
     *
     * @param wordIds a list of the word IDs
     */
    @Async
    @Transactional
    public void loadPronunciationTracks(List<Integer> wordIds) {
        List<Word> words = wordRepository.findAllById(wordIds);
        for (Word word : words) {
            Set<PronunciationTrack> pronunciationTracks = word.getPronunciationTracks();

            String ukFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.UK);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.UK, ukFile));
            String usFile = textToSpeechService.synthesize(word.getText(), EnglishVariety.US);
            pronunciationTracks.add(new PronunciationTrack(word.getId(), EnglishVariety.US, usFile));
        }
        wordRepository.saveAll(words);
    }
}
