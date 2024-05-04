package net.englab.spellingtrainer.repositories;

import net.englab.spellingtrainer.models.entities.SpellingTest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface provides methods for querying spelling test objects
 * from the database. It extends JpaRepository for standard CRUD operations
 * on word entities.
 */
public interface SpellingTestRepository extends JpaRepository<SpellingTest, String> {

    @EntityGraph("spelling-test-with-words-and-pronunciation-tracks")
    @Override
    Optional<SpellingTest> findById(String id);
}
