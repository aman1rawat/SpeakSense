package com.major.speaksense.service;

import com.major.speaksense.dto.Analysis;
import com.major.speaksense.dto.Transcript;
import com.major.speaksense.dto.WordAnalysisDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrchestrationService {

    private final TranscriptionService transcriptionService;
    private final AnalysisService analysisService;
    private final AgentService agentService;
    private final BlobStorageService blobStorageService;

    public OrchestrationService(TranscriptionService transcriptionService,
                                AnalysisService analysisService,
                                AgentService agentService,
                                BlobStorageService blobStorageService){
        this.transcriptionService = transcriptionService;
        this.analysisService = analysisService;
        this.agentService = agentService;
        this.blobStorageService = blobStorageService;
    }

    public ResponseEntity<?> process(MultipartFile audioFile){
        try{
            Transcript transcript = transcriptionService.generateTranscript(audioFile);
            Analysis analysis = analysisService.analyse(audioFile, transcript);

            analysis.word_details().forEach(wordData -> {
                if (wordData.audio_base64() != null) {
                    blobStorageService.uploadAudioAsync(wordData.word(), wordData.audio_base64());
                }
            });

            List<WordAnalysisDTO> cleanWords = analysis.word_details().stream()
                    .map(w -> new WordAnalysisDTO(w.word(), w.word_score(), w.phonemes(), null))
                    .toList();

            Analysis cleanAnalysis = new Analysis(analysis.overall_pronunciation_score(), cleanWords);

            String feedback = agentService.getFeedback(transcript, cleanAnalysis);
            Map<String, Object> response = new HashMap<>();
            response.put("overall_score", cleanAnalysis.overall_pronunciation_score());
            response.put("feedback", feedback);

            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}