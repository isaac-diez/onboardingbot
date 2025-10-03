package com.hackaton.onboardingbot.model;

import org.springframework.stereotype.Component;

@Component
public class KnowledgeMapper {
    public KnowledgeDTO toDto (KnowledgeEntry knowledgeEntry) {

        String question = knowledgeEntry.getQuestion();
        String answer = knowledgeEntry.getAnswer();

        return new KnowledgeDTO(question, answer);

    }

    public KnowledgeEntry toQuery(KnowledgeCreateDTO knowledgeDTO) {

        return new KnowledgeEntry(knowledgeDTO.getQuestion(), knowledgeDTO.getAnswer(), knowledgeDTO.getKeyword());

    }

}
