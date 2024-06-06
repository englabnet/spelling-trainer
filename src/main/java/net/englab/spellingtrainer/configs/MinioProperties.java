package net.englab.spellingtrainer.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spelling-trainer.minio")
public record MinioProperties(
        String url,
        String user,
        String password
) {
}
