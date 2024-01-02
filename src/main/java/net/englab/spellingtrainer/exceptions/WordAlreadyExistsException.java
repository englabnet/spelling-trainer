package net.englab.spellingtrainer.exceptions;

/**
 * The exception is thrown when the word cannot be added because it already exists.
 */
public class WordAlreadyExistsException extends RuntimeException {
    public WordAlreadyExistsException(String message) {
        super(message);
    }
}
