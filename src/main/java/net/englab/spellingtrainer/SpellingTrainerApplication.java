package net.englab.spellingtrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
public class SpellingTrainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpellingTrainerApplication.class, args);
    }

}
