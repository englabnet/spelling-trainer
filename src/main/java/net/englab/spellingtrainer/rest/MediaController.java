package net.englab.spellingtrainer.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A REST controller that is used to retrieve media files.
 */
@RestController
@RequestMapping(path = "/api/v1/media")
public class MediaController {

    @Value("${spelling-trainer.pronunciation-dir}")
    private String pronunciationDir;

    /**
     * Returns pronunciation tracks by their file paths.
     *
     * @param filename the filepath of the audio file
     * @return an audio file
     */
    @GetMapping(value = "/pronunciations/{filename}", produces = "audio/mpeg")
    public byte[] getPronunciation(@PathVariable String filename) throws IOException {
        return Files.readAllBytes(Path.of(pronunciationDir + filename));
    }
}
