package com.hackaton.onboardingbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.onboardingbot.exception.InvalidRequestException;
import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.service.KnowledgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KnowledgeController.class)
class KnowledgeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KnowledgeService knowledgeService;

    private final KnowledgeDTO sampleDto = new KnowledgeDTO("Q1", "A1");
    private final KnowledgeCreateDTO createDto = new KnowledgeCreateDTO("New Q", "New A", "NewKW");
    private final KnowledgeEntry sampleEntry = new KnowledgeEntry(); // Assuming a basic entry for return type

    public KnowledgeControllerTest() {
        sampleEntry.setQuestion("New Q");
        sampleEntry.setAnswer("New A");
        sampleEntry.setKeyword("NewKW");
    }

    @Test
    void getEntries_shouldReturn200AndList_whenEntriesExist() throws Exception {
        List<KnowledgeDTO> mockList = List.of(sampleDto);
        when(knowledgeService.getAllEntries()).thenReturn(mockList);

        mockMvc.perform(get("/api/knowledge"))
                .andExpect(status().isOk()) // Espera el codi 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].question").value(sampleDto.getQuestion()));
    }

    @Test
    void getEntries_shouldReturn204_whenNoEntriesExist() throws Exception {
        when(knowledgeService.getAllEntries()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/knowledge"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
    }

    @Test
    void getEntryByQuestion_shouldReturn200AndEntries_whenFound() throws Exception {
        String question = "com puc fer un test?";
        List<KnowledgeDTO> mockList = List.of(sampleDto);

        when(knowledgeService.searchByQuestionKeywords(any(String.class))).thenReturn(mockList);

        mockMvc.perform(get("/api/knowledge/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(question))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value(sampleDto.getQuestion()));
    }

    @Test
    void getEntryByQuestion_shouldReturn204_whenNotFound() throws Exception {
        String question = "pregunta que no troba";

        when(knowledgeService.searchByQuestionKeywords(any(String.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/knowledge/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(question))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));
    }

    @Test
    void create_shouldReturn200AndCreatedEntry_whenSuccessful() throws Exception {

        when(knowledgeService.createKnowledgeEntry(any(KnowledgeCreateDTO.class))).thenReturn(sampleEntry);

        String requestBodyJson = objectMapper.writeValueAsString(createDto);

        mockMvc.perform(post("/api/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.question").value(sampleEntry.getQuestion()));
    }

    @Test
    void create_shouldReturn400AndErrorBody_whenInvalidRequestExceptionThrown() throws Exception {
        String errorMessage = "La pregunta no pot estar buida";

        when(knowledgeService.createKnowledgeEntry(any(KnowledgeCreateDTO.class)))
                .thenThrow(new InvalidRequestException(errorMessage));

        String requestBodyJson = objectMapper.writeValueAsString(new KnowledgeCreateDTO("Q", "A", "kw"));

        mockMvc.perform(post("/api/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/knowledge"));
    }

}