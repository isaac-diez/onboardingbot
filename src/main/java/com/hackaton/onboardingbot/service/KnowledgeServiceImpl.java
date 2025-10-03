package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.model.KnowledgeMapper;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService{

    private KnowledgeRepo knowledgeRepo;
    private KnowledgeMapper mapper;

    public KnowledgeServiceImpl(KnowledgeRepo knowledgeRepo, KnowledgeMapper mapper) {
        this.knowledgeRepo = knowledgeRepo;
        this.mapper = mapper;
    }

    @Override
    public List<KnowledgeDTO> getAllEntries() {
        return knowledgeRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public KnowledgeEntry createKnowledgeEntry(KnowledgeCreateDTO queryDTO) {
        KnowledgeEntry query = mapper.toQuery(queryDTO);
        return knowledgeRepo.save(query);
    }
}
