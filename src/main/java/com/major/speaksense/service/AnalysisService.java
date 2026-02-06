package com.major.speaksense.service;

import com.major.speaksense.dto.Analysis;
import com.major.speaksense.dto.Transcript;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AnalysisService {

    private final WebClient webClient;

    public AnalysisService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://word2vec2.onrender.com").build();
    }

    public Analysis analyse(MultipartFile audioFile, Transcript transcript) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", audioFile.getResource());
        builder.part("transcript", transcript, MediaType.APPLICATION_JSON);

        return webClient.post()
                .uri("/analyse")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Analysis.class)
                .block();
    }
}
