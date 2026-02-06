package com.major.speaksense.service;

import com.major.speaksense.dto.Analysis;
import com.major.speaksense.dto.Transcript;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrchestrationService {
    private final TranscriptionService transcriptionService;
    private final AnalysisService analysisService;
    private final AgentService agentService;

    public OrchestrationService(TranscriptionService transcriptionService,
                                AnalysisService analysisService,
                                AgentService agentService
    ){
        this.transcriptionService = transcriptionService;
        this.analysisService = analysisService;
        this.agentService = agentService;
    }

    public ResponseEntity<String> process(MultipartFile audioFile){
        try{
            Transcript transcript = transcriptionService.generateTranscript(audioFile);
            Analysis analysis = analysisService.analyse(audioFile, transcript);
            String feedback = agentService.getFeedback(transcript, analysis);

            return ResponseEntity.ok(feedback);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
