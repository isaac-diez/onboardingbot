package com.hackaton.onboardingbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="questions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KnowledgeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "keyword")
    private String keyword;

    public KnowledgeEntry(String question, String answer, String keyword) {
        this.question = question;
        this.answer = answer;
        this.keyword = keyword;
    }
}
