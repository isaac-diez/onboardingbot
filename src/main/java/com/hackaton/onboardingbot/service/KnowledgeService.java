package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface KnowledgeService {

    List<KnowledgeEntry> getAllEntries();
    KnowledgeEntry createKnowledgeEntry(KnowledgeEntry knowledgeEntry);


}
