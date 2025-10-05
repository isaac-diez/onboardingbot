package com.hackaton.onboardingbot.controller;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.service.KnowledgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KnowledgeController {

    private KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/knowledge")
    public List<KnowledgeDTO> getEntries() {
        return knowledgeService.getAllEntries();
    }

    @GetMapping("/knowledge/question")
    public List<KnowledgeDTO> getEntryByQuestion(@RequestBody String question) {
        return knowledgeService.searchByQuestionKeywords(question);
    }

    @PostMapping("/knowledge")
    public KnowledgeEntry create(@RequestBody KnowledgeCreateDTO queryDTO) {

        return knowledgeService.createKnowledgeEntry(queryDTO);
    }

}
