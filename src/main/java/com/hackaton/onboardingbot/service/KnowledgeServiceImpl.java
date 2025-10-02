package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService{

    @Autowired
    KnowledgeRepo knowledgeRepo;

    @Override
    public List<KnowledgeEntry> getAllEntries() {
        return List.of();
    }

    @Override
    public KnowledgeEntry createKnowledgeEntry(KnowledgeEntry knowledgeEntry) {
        return null;
    }
}
