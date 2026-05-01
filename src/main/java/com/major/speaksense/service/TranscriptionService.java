package com.major.speaksense.service;

import com.major.speaksense.dto.Transcript;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TranscriptionService {
    private final WebClient webClient;

    public TranscriptionService(WebClient.Builder builder, @Value("${TRANSCRIPTION_SERVICE_URL}") String transcriptionServiceURL){
        this.webClient = builder.baseUrl(transcriptionServiceURL).build();
    }

    public Transcript generateTranscript(MultipartFile audioFile){
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", audioFile.getResource());

        return webClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Transcript.class)
                .block();
    }

    public String check() {
        try {
            return webClient.get()
                    .uri("/health")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "Service Unavailable: " + e.getMessage();
        }
    }
}
