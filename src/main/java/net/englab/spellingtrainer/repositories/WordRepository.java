package net.englab.spellingtrainer.repositories;

import net.englab.spellingtrainer.models.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface provides methods for querying word objects
 * from the database. It extends JpaRepository for standard CRUD operations
 * on word entities.
 */
public interface WordRepository extends JpaRepository<Word, Long> {

    /**
     * Finds a list of word entities by the specified words.
     * It can be used to check if the words already exist.
     *
     * @param words a list of words
     * @return a list of the word entities
     */
    List<Word> findByTextIn(List<String> words);

    /**
     * Finds a list of word by the specified prefix.
     *
     * @param prefix the prefix of the words
     * @return a list of the words that have the specified prefix
     */
    List<Word> findByTextStartsWith(String prefix);
}
