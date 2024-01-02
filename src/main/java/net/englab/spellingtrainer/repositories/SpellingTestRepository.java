package net.englab.spellingtrainer.repositories;

import net.englab.spellingtrainer.models.entities.SpellingTest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides methods for querying spelling test objects
 * from the database. It extends JpaRepository for standard CRUD operations
 * on word entities.
 */
public interface SpellingTestRepository extends JpaRepository<SpellingTest, String> {
}
