package com.major.speaksense.controller;

import com.major.speaksense.service.AgentService;
import com.major.speaksense.service.OrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/speaksense")
public class SpeakSenseController {
    private final OrchestrationService orchestrationService;
    private final AgentService agentService;

    public SpeakSenseController(AgentService agentService, OrchestrationService orchestrationService){
        this.agentService = agentService;
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processAudio(MultipartFile audioFile){
        try{
            return orchestrationService.process(audioFile);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/test")
//    public String ask(){
//        return agentService.ask("Hello");
//    }

}
