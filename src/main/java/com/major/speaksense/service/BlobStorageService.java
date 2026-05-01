package com.major.speaksense.service;

import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class BlobStorageService {

    @Value("${AZURE_STORAGE_CONNECTION_STRING}")
    private String connectionString;

    @Value("${AZURE_STORAGE_CONTAINER_NAME}")
    private String containerName;


    @Async
    public void uploadAudioAsync(String word, String base64Audio) {
        if(base64Audio==null || base64Audio.isEmpty()) {return;}

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Audio);

            String cleanWord = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            String fileName = cleanWord + "/" + UUID.randomUUID() + ".wav";

            new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .blobName(fileName)
                    .buildClient()
                    .upload(new ByteArrayInputStream(decodedBytes), decodedBytes.length, true);

            System.out.println("Successfully uploaded to folder: " + fileName);

        } catch (Exception e) {
            System.err.println("Upload failed for word '" + word + "': " + e.getMessage());
        }
    }
}