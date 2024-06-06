package net.englab.spellingtrainer.services;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.englab.spellingtrainer.exceptions.MediaStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * This is a media storage service responsible for saving media files into minio/S3.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MediaStorage {

    private final MinioClient minioClient;

    @Value("${spelling-trainer.pronunciations-bucket}")
    private String pronunciationsBucket;

    /**
     * Saves the given file to the media storage.
     *
     * @param filename      the name of the file
     * @param inputStream   the file itself
     * @param objectSize    the size of the file
     * @return the path to the saved file
     */
    public String save(String filename, InputStream inputStream, long objectSize) {
        var args = PutObjectArgs.builder()
                .bucket(pronunciationsBucket)
                .object(filename)
                .stream(inputStream, objectSize, -1)
                .build();
        try {
            minioClient.putObject(args);
            return "/" + pronunciationsBucket + "/" + filename;
        } catch (Exception e) {
            throw new MediaStorageException("An exception occurred during saving a pronunciation track.", e);
        }
    }
}
