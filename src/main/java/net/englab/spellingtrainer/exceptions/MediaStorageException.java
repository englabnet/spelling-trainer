package net.englab.spellingtrainer.exceptions;

/**
 * The exception is thrown when an error occurs during accessing the media storage.
 */
public class MediaStorageException extends RuntimeException {
    public MediaStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
