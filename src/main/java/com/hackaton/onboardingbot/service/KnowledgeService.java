package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;

import java.util.List;

public interface KnowledgeService {

    List<KnowledgeDTO> getAllEntries();
    KnowledgeEntry createKnowledgeEntry(KnowledgeCreateDTO newEntryDto);


}
