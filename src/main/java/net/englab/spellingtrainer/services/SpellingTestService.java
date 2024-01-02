package net.englab.spellingtrainer.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.englab.spellingtrainer.models.entities.SpellingTest;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.repositories.SpellingTestRepository;
import net.englab.spellingtrainer.repositories.WordRepository;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * A service that is responsible for spelling tests.
 */
@Service
@RequiredArgsConstructor
public class SpellingTestService {

    public static final String HASHING_ALGORITHM = "MD5";

    private final WordRepository wordRepository;
    private final SpellingTestRepository testRepository;

    /**
     * Generates a new spelling test with the given list of words.
     *
     * @param wordIds a list of word IDs
     * @return the ID of the new test
     */
    public String generate(List<Integer> wordIds) {
        List<Word> words = wordRepository.findAllById(wordIds);

        String id = generateHash(wordIds);

        SpellingTest spellingTest = new SpellingTest(id, new HashSet<>(words), Instant.now());
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

        return Base64.getUrlEncoder().encodeToString(hash);
    }

    public Optional<SpellingTest> find(String id) {
        return testRepository.findById(id);
    }
}
