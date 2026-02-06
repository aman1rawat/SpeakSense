package com.major.speaksense.service;

import com.major.speaksense.dto.Transcript;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TranscriptionService {
    private final WebClient webClient;

    public TranscriptionService(WebClient.Builder builder){
        this.webClient = builder.baseUrl("").build();
    }

    public Transcript generateTranscript(MultipartFile audioFile){
        return webClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", audioFile))
                .retrieve()
                .bodyToMono(Transcript.class)
                .block();
    }
}
