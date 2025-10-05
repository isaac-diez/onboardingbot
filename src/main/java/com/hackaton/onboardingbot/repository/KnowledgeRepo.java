package com.hackaton.onboardingbot.repository;

import com.hackaton.onboardingbot.model.KnowledgeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeRepo extends JpaRepository <KnowledgeEntry, Integer>{
    List<KnowledgeEntry> findByKeywordIn(List<String> keywords);
}
