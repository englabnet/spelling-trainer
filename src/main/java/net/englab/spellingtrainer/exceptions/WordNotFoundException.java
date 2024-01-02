package net.englab.spellingtrainer.exceptions;

/**
 * The exception is thrown when the required word has not been found.
 */
public class WordNotFoundException extends RuntimeException {
    public WordNotFoundException(String message) {
        super(message);
    }
}
