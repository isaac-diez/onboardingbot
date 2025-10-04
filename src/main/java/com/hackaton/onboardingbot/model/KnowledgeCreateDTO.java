package com.hackaton.onboardingbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KnowledgeCreateDTO {
    private String question;
    private String answer;
    private String keyword;
}
