package net.englab.spellingtrainer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.englab.spellingtrainer.exceptions.WordAlreadyExistsException;
import net.englab.spellingtrainer.exceptions.WordNotFoundException;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.repositories.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A word storage service that provides basic operations
 * such as saving, deleting, and searching words.
 */
@Service
@RequiredArgsConstructor
public class WordStorage {

    private final WordRepository wordRepository;

    /**
     * Finds a word by the given text.
     *
     * @param word the word
     * @return a list of the words that have the specified prefix
     */
    public Optional<Word> find(String word) {
        if (!StringUtils.hasText(word)) {
            return Optional.empty();
        }
        return wordRepository.findByText(word.trim().toLowerCase());
    }

    /**
     * Saves a list of words to the storage.
     *
     * @param words a list of the words that needs to be saved
     * @throws WordAlreadyExistsException if some of the words already exist
     */
    @Transactional
    public void save(List<String> words) {
        List<Word> presentWords = wordRepository.findByTextIn(words);
        if (!presentWords.isEmpty()) {
            throw new WordAlreadyExistsException(
                    "The words cannot be added because some of them already exist: " + presentWords
            );
        }
        List<Word> list = words.stream()
                .map(s -> new Word(null, s, Set.of()))
                .toList();
        wordRepository.saveAll(list);
    }

    /**
     * Deletes a word by its ID.
     *
     * @param id the ID of the word that needs to be deleted
     * @throws WordNotFoundException if the word has not been found
     */
    @Transactional
    public void delete(Integer id) {
        wordRepository.findById(id)
                .ifPresentOrElse(word -> wordRepository.deleteById(id),
                        () -> {
                            throw new WordNotFoundException("The word has not been found and cannot be deleted.");
                        });
    }

    /**
     * Finds words by the given prefix.
     *
     * @param prefix the prefix of the words
     * @return a list of the words that have the specified prefix
     */
    public List<Word> findByPrefix(String prefix) {
        return wordRepository.findByPrefix(prefix.trim().toLowerCase());
    }
}
