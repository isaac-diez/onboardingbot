package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.exception.InvalidRequestException;
import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.model.KnowledgeMapper;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

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

        if(queryDTO.getQuestion().isEmpty() || queryDTO.getQuestion()==null){
            throw new InvalidRequestException("La pregunta no pot estar buida");
        }

        if(queryDTO.getAnswer().isEmpty() || queryDTO.getAnswer()==null){
            throw new InvalidRequestException("La resposta no pot estar buida");
        }

        if(queryDTO.getKeyword().isEmpty() || queryDTO.getKeyword()==null){
            throw new InvalidRequestException("La keyword no pot estar buida");
        }

        KnowledgeEntry query = mapper.toQuery(queryDTO);
        return knowledgeRepo.save(query);
    }

    @Override
    public List<KnowledgeDTO> searchByQuestionKeywords(String userQuestion) {
        if (userQuestion == null || userQuestion.isBlank()) {
            throw  new InvalidRequestException("La pregunta no pot estar buida");
        }

        String normalizedQuestion = normalizeAndCleanText(userQuestion);

        String[] words = normalizedQuestion.split("\\s+");

        List<String> keywordsToSearch = Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .toList();

        if (keywordsToSearch.isEmpty()) {
            return List.of();
        }

        List<KnowledgeEntry> foundEntries = knowledgeRepo.findByKeywordIn(keywordsToSearch);

        return foundEntries.stream()
                .map(mapper::toDto)
                .toList();
    }

    private String normalizeAndCleanText(String text) {
        String normalizedText = text.toLowerCase();

        normalizedText = normalizedText.replace("'", " ");

        normalizedText = Normalizer.normalize(normalizedText, Normalizer.Form.NFD);

        normalizedText = normalizedText.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalizedText.replaceAll("[^a-z0-9\\s]", "");
    }
}
