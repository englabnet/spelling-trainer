package net.englab.spellingtrainer.configs;

import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TextToSpeechConfig {

    @Bean
    public TextToSpeechClient textToSpeechClient() throws IOException {
        return TextToSpeechClient.create();
    }
}
