package com.hackaton.onboardingbot.controller;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.model.KnowledgeMapper;
import com.hackaton.onboardingbot.service.KnowledgeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class KnowledgeController {

    private KnowledgeService knowledgeService;
    private KnowledgeMapper mapper;

    @GetMapping
    public List<KnowledgeDTO> getEntries() {
        return knowledgeService.getAllEntries()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public KnowledgeEntry create(@RequestBody KnowledgeCreateDTO queryDTO) {

        KnowledgeEntry query = mapper.toQuery(queryDTO);

        return knowledgeService.createKnowledgeEntry(query);
    }

}
