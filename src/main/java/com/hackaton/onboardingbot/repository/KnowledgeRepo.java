package com.hackaton.onboardingbot.repository;

import com.hackaton.onboardingbot.model.KnowledgeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeRepo extends JpaRepository <KnowledgeEntry, Integer>{
}
