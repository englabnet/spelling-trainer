package net.englab.spellingtrainer.rest;

import lombok.RequiredArgsConstructor;
import net.englab.spellingtrainer.exceptions.WordAlreadyExistsException;
import net.englab.spellingtrainer.exceptions.WordNotFoundException;
import net.englab.spellingtrainer.models.entities.Word;
import net.englab.spellingtrainer.services.WordStorage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * A REST controller that allows us to add new words, remove them, etc.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/words")
public class WordController {

    private final WordStorage wordService;

    /**
     * Adds new words to the system.
     *
     * @param words a list of words that needs to be added
     * @return a status message after adding the words
     */
    @PostMapping
    public String add(@RequestBody List<String> words) {
        try {
            wordService.save(words);
        } catch (WordAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return "The words have been added successfully";
    }

    /**
     * Removes a word from the system.
     *
     * @param id the ID of the word that needs to be removed
     */
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {
        try {
            wordService.delete(id);
        } catch (WordNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "The word has been removed successfully";
    }

    /**
     * Finds words by the given prefix.
     *
     * @param prefix the prefix of the words
     * @return a list of the words that have the specified prefix
     */
    @GetMapping
    public List<Word> find(String prefix) {
        if (prefix.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the prefix must contain at least 2 characters");
        }
        return wordService.findByPrefix(prefix);
    }
}
