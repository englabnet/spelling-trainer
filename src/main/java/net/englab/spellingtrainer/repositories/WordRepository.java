package net.englab.spellingtrainer.repositories;

import net.englab.spellingtrainer.models.entities.Word;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides methods for querying word objects
 * from the database. It extends JpaRepository for standard CRUD operations
 * on word entities.
 */
public interface WordRepository extends JpaRepository<Word, Integer> {

    /**
     * Finds a word entity by the specified text.
     *
     * @param word the word
     * @return an Optional containing the found word
     */
    Optional<Word> findByText(String word);

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
     * The result is sorted by word length, so the shortest word will be the first one.
     *
     * @param prefix the prefix of the words
     * @return a list of the words that have the specified prefix
     */
    @Query("select w from Word w where w.text like ?1% order by length(w.text)")
    List<Word> findByPrefix(String prefix);
}
