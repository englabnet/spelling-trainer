package net.englab.spellingtrainer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.englab.spellingtrainer.models.TestStep;
import net.englab.spellingtrainer.models.entities.SpellingTest;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.repositories.SpellingTestRepository;
import org.springframework.stereotype.Service;

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
        pronunciationTrackLoader.loadPronunciationTracks(words);

        String id = generateHash(wordIds);
        SpellingTest spellingTest = new SpellingTest(id, new ArrayList<>(words), Instant.now());
        testRepository.save(spellingTest);
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

        return Base64.getUrlEncoder().encodeToString(hash).substring(0, 23);
    }

    /**
     * Finds a test by its ID.
     *
     * @param testId the ID of the test that needs to be found
     * @return an Optional containing the found spelling test
     */
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
    public Optional<TestStep> findStep(String testId, int step) {
        return testRepository.findById(testId)
                .map(SpellingTest::getWords)
                .filter(words -> words.size() > step)
                .map(words -> {
                    Word currentWord = words.get(step);
                    return new TestStep(step, words.size(), currentWord.getId(), currentWord.getPronunciationTracks());
                });
    }

    /**
     * Checks user's answer.
     *
     * @param testId    the ID of the test
     * @param step      the number of the step
     * @param answer    the answer of the user
     * @return true if the answer correct and false otherwise
     */
    public boolean checkAnswer(String testId, int step, String answer) {
        if (answer == null) return false;
        return testRepository.findById(testId)
                .map(SpellingTest::getWords)
                .filter(words -> words.size() > step)
                .map(words -> words.get(step).getText().trim().toLowerCase())
                .stream()
                .anyMatch(word -> Objects.equals(word, answer.trim().toLowerCase()));
    }
}
