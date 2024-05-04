package net.englab.spellingtrainer.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.englab.spellingtrainer.models.TestStep;
import net.englab.spellingtrainer.models.entities.SpellingTest;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.repositories.SpellingTestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

/**
 * A service that is responsible for spelling tests.
 */
@Service
@RequiredArgsConstructor
public class SpellingTestService {

    public static final String HASHING_ALGORITHM = "MD5";

    private final WordService wordService;
    private final PronunciationTrackLoader pronunciationTrackLoader;
    private final SpellingTestRepository testRepository;

    /**
     * Generates a new spelling test with the given list of words.
     *
     * @param wordIds a list of word IDs
     * @return the ID of the new test
     */
    @Transactional
    public String generate(List<Integer> wordIds) {
        List<Word> words = wordService.findAllById(wordIds);

        List<Word> wordsWithoutAudio = words.stream()
                .filter(w -> w.getPronunciationTracks().isEmpty())
                .toList();
        if (!wordsWithoutAudio.isEmpty()) {
            // All the audio should be loaded at this point as it's generated in the background during
            // the creation of a test. However, it's better to double-check and upload any missing files.
            // We can't continue if some audio files aren't ready, so we block the thread until everything is loaded.
            var tracks = pronunciationTrackLoader.loadPronunciationTracks(wordsWithoutAudio).join();
            wordsWithoutAudio.forEach(w -> w.getPronunciationTracks().addAll(tracks.get(w.getId())));
        }

        String id = generateHash(wordIds);
        SpellingTest spellingTest = new SpellingTest(id, new LinkedHashSet<>(words), Instant.now());
        testRepository.saveAndFlush(spellingTest);
        return id;
    }

    @SneakyThrows
    private static String generateHash(List<Integer> wordIds) {
        wordIds = wordIds.stream()
                .sorted()
                .toList();

        ByteBuffer buffer = ByteBuffer.allocate(wordIds.size() * Integer.BYTES);
        wordIds.forEach(buffer::putInt);
        byte[] idBuffer = buffer.array();

        MessageDigest md = MessageDigest.getInstance(HASHING_ALGORITHM);
        byte[] hash = md.digest(idBuffer);

        return Base64.getUrlEncoder().encodeToString(hash).substring(0, 22);
    }

    /**
     * Finds a test by its ID.
     *
     * @param testId the ID of the test that needs to be found
     * @return an Optional containing the found spelling test
     */
    @Transactional(readOnly = true)
    public Optional<SpellingTest> find(String testId) {
        return testRepository.findById(testId);
    }

    /**
     * Finds a specified step of the test.
     *
     * @param testId    the ID of the test
     * @param step      the number of the step
     * @return an Optional containing the found test step
     */
    @Transactional(readOnly = true)
    public Optional<TestStep> findStep(String testId, int step) {
        var result = testRepository.findById(testId);
        return result.map(SpellingTest::getWords)
                .filter(words -> words.size() > step)
                .flatMap(words -> words.stream()
                            .skip(step)
                            .findFirst()
                            .map(word -> new TestStep(step, words.size(), word.getId(), word.getPronunciationTracks()))
                );
    }

    /**
     * Checks user's answer.
     *
     * @param testId    the ID of the test
     * @param step      the number of the step
     * @param answer    the answer of the user
     * @return true if the answer correct and false otherwise
     */
    @Transactional(readOnly = true)
    public boolean checkAnswer(String testId, int step, String answer) {
        if (answer == null) return false;
        return testRepository.findById(testId)
                .map(SpellingTest::getWords)
                .filter(words -> words.size() > step)
                .flatMap(words -> words.stream().skip(step).findFirst().map(word -> word.getText().trim().toLowerCase()))
                .stream()
                .anyMatch(word -> Objects.equals(word, answer.trim().toLowerCase()));
    }
}
