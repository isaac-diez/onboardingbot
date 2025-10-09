package com.hackaton.onboardingbot.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@Order(1)
public class JsonDBEntriesInitializer implements CommandLineRunner {

    private final KnowledgeRepo knowledgeRepo;
    private final ObjectMapper objectMapper;
    TypeReference<List<KnowledgeEntry>> typeReference = new TypeReference<List<KnowledgeEntry>>(){};

    public JsonDBEntriesInitializer(KnowledgeRepo knowledgeRepo, ObjectMapper objectMapper) {

        this.knowledgeRepo = knowledgeRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (knowledgeRepo.count() == 0) {
            log.info("Empty database. Loading entries from entries.json...");

            try (InputStream inputStream = getClass().getResourceAsStream("/json/entries.json")) {
                if (inputStream == null) {
                    log.error("entries.json not found");
                }

                List<KnowledgeEntry> entries = objectMapper.readValue(inputStream, typeReference);
                knowledgeRepo.saveAll(entries);
                log.info("All {} entries from entries.json have been successfully uploaded to the database", entries.size());
            } catch (Exception ex) {
                log.error("Upload of entries from entries.json to the database FAILED", ex);
            }
        } else {
            log.info("Database already has entries. Initial upload from entries.json not done");
        }
    }
}
