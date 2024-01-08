package net.englab.spellingtrainer.services;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.englab.spellingtrainer.models.EnglishVariety;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A service which is responsible for converting text to speech.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextToSpeechService {
    private static final Map<EnglishVariety, List<String>> VOICES = Map.of(
            EnglishVariety.UK, List.of("en-GB-Standard-A", "en-GB-Standard-B"),
            EnglishVariety.US, List.of("en-US-Standard-G", "en-US-Standard-I")
    );
    private static final Map<EnglishVariety, String> LANGUAGE_CODE = Map.of(
            EnglishVariety.UK, "en-GB",
            EnglishVariety.US, "en-US"
    );
    private static final Random RANDOM = new Random();

    private final TextToSpeechClient textToSpeechClient;

    @Value("${spelling-trainer.pronunciation-dir}")
    private String outputDir;

    /**
     * Synthesizes an audio file. The file is saved to the file system
     * and the filepath is returned as the result.
     *
     * @param text the text that needs to be converted to speech
     * @param englishVariety the accent of the speech
     * @return the filepath
     */
    @SneakyThrows
    public String synthesize(String text, EnglishVariety englishVariety) {
        SynthesisInput input = SynthesisInput.newBuilder()
                .setText(text)
                .build();

        String voiceName = pickVoice(englishVariety);
        VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                .setName(voiceName)
                .setLanguageCode(LANGUAGE_CODE.get(englishVariety))
                .build();

        AudioConfig audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)
                .build();

        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
        ByteString audioContents = response.getAudioContent();

        Files.createDirectories(Path.of(outputDir));

        String filepath = outputDir + voiceName + "-" + text  + ".mp3";
        try (OutputStream out = new FileOutputStream(filepath)) {
            out.write(audioContents.toByteArray());
            log.info("A new audio file has been created: " + filepath);
        }

        return filepath;
    }

    private String pickVoice(EnglishVariety englishVariety) {
        List<String> voices = VOICES.get(englishVariety);
        return voices.get(RANDOM.nextInt(2));
    }
}