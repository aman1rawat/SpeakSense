package com.major.speaksense.controller;

import com.major.speaksense.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/speaksense")
public class SpeakSenseController {
    private final OrchestrationService orchestrationService;

    public SpeakSenseController(OrchestrationService orchestrationService){
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processAudio(MultipartFile audioFile){
        try{
            return orchestrationService.process(audioFile);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
