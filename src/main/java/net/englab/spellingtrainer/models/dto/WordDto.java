package net.englab.spellingtrainer.models.dto;

/**
 * A DTO that represents a word. It should be used when we don't
 * need to transfer pronunciation tracks.
 *
 * @param id    the unique identifier of the word.
 * @param text  the string that holds the word itself
 */
public record WordDto(int id, String text) {
}
