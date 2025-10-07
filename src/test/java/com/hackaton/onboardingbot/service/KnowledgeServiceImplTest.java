package com.hackaton.onboardingbot.service;

import com.hackaton.onboardingbot.exception.InvalidRequestException;
import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.model.KnowledgeMapper;
import com.hackaton.onboardingbot.repository.KnowledgeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeServiceImplTest {

    @Mock
    private KnowledgeRepo knowledgeRepo;

    @Mock
    private KnowledgeMapper mapper;

    @InjectMocks
    private KnowledgeServiceImpl knowledgeService;

    private KnowledgeEntry testEntry;
    private KnowledgeDTO testDto;
    private KnowledgeCreateDTO createDto;

    @BeforeEach
    void setUp() {
        testEntry = new KnowledgeEntry();
        testEntry.setQuestion("Què és una prova?");
        testEntry.setAnswer("És un test d'unitat.");
        testEntry.setKeyword("prova");

        testDto = new KnowledgeDTO("Què és una prova?", "És un test d'unitat.");

        createDto = new KnowledgeCreateDTO("Nova Q", "Nova A", "novaKW");
    }

    @Test
    void getAllEntries_shouldReturnAllMappedDtos() {
        List<KnowledgeEntry> entryList = List.of(testEntry);
        when(knowledgeRepo.findAll()).thenReturn(entryList);
        when(mapper.toDto(testEntry)).thenReturn(testDto);

        List<KnowledgeDTO> result = knowledgeService.getAllEntries();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testDto.getQuestion(), result.get(0).getQuestion());
        verify(knowledgeRepo, times(1)).findAll();
        verify(mapper, times(1)).toDto(testEntry);
    }

    @Test
    void getAllEntries_shouldReturnEmptyList_whenRepoIsEmpty() {
        when(knowledgeRepo.findAll()).thenReturn(Collections.emptyList());

        List<KnowledgeDTO> result = knowledgeService.getAllEntries();

        assertTrue(result.isEmpty());
        verify(knowledgeRepo, times(1)).findAll();
        verify(mapper, never()).toDto(any()); // El mapper no s'ha d'executar mai
    }

    @Test
    void createKnowledgeEntry_shouldSaveAndReturnEntry_whenValid() {
        KnowledgeEntry mappedEntry = new KnowledgeEntry();
        when(mapper.toQuery(createDto)).thenReturn(mappedEntry);
        when(knowledgeRepo.save(mappedEntry)).thenReturn(testEntry);

        KnowledgeEntry result = knowledgeService.createKnowledgeEntry(createDto);

        assertNotNull(result);
        assertEquals(testEntry.getQuestion(), result.getQuestion());
        verify(knowledgeRepo, times(1)).save(mappedEntry);
    }

    @Test
    void createKnowledgeEntry_shouldThrowException_whenQuestionIsEmpty() {
        KnowledgeCreateDTO createDtoEmptyQuestion = new KnowledgeCreateDTO("", "", "");

        assertThrows(InvalidRequestException.class, () -> {
            knowledgeService.createKnowledgeEntry(createDtoEmptyQuestion);
        }, "La pregunta no pot estar buida");

        verify(knowledgeRepo, never()).save(any());
    }

    @Test
    void searchByQuestionKeywords_shouldReturnMappedResults_whenKeywordsFound() {
        String userQuestion = "Quina és la Prova d'unitat?";
        List<KnowledgeEntry> foundEntries = List.of(testEntry);

        when(knowledgeRepo.findByKeywordIn(anyList())).thenReturn(foundEntries);
        when(mapper.toDto(testEntry)).thenReturn(testDto);

        List<KnowledgeDTO> result = knowledgeService.searchByQuestionKeywords(userQuestion);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(knowledgeRepo, times(1)).findByKeywordIn(argThat(keywords ->
                keywords.contains("quina") && keywords.contains("prova")
        ));
    }

    @Test
    void searchByQuestionKeywords_shouldReturnEmptyList_whenNoEntriesFound() {
        String userQuestion = "Cerca inexistent";
        when(knowledgeRepo.findByKeywordIn(anyList())).thenReturn(Collections.emptyList());

        List<KnowledgeDTO> result = knowledgeService.searchByQuestionKeywords(userQuestion);

        assertTrue(result.isEmpty());
        verify(knowledgeRepo, times(1)).findByKeywordIn(anyList());
    }

    @Test
    void searchByQuestionKeywords_shouldReturnEmptyList_whenQuestionIsNull() {
        List<KnowledgeDTO> result = knowledgeService.searchByQuestionKeywords(null);

        assertTrue(result.isEmpty());
        verify(knowledgeRepo, never()).findByKeywordIn(anyList());
    }

    @Test
    void searchByQuestionKeywords_shouldReturnEmptyList_whenQuestionIsBlank() {
        List<KnowledgeDTO> result = knowledgeService.searchByQuestionKeywords("   ");

        assertTrue(result.isEmpty());
        verify(knowledgeRepo, never()).findByKeywordIn(anyList());
    }

}