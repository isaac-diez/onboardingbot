package com.hackaton.onboardingbot.controller;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.model.KnowledgeEntry;
import com.hackaton.onboardingbot.service.KnowledgeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KnowledgeController {

    private KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @Operation(
            summary = "Recupera totes les entrades",
            description = "Retorna una llista amb totes les entrades de pregunta/resposta emmagatzemades a la base de coneixement.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Llista recuperada amb èxit.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = KnowledgeDTO.class)))
                            ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error. An unexpected error occurred while retrieving the user.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/knowledge")
    public ResponseEntity<List<KnowledgeDTO>> getEntries() {
        List<KnowledgeDTO> allEntriesList = knowledgeService.getAllEntries();

        if (allEntriesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(allEntriesList);
        }
    }

    @Operation(
            summary = "Fa la cerca d'una pregunta del client",
            description = "Retorna una entrada o una llista d'entrades amb totes les que corresponen a una keyword.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pregunta/es trobada/es amb èxit.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = KnowledgeDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Petició invàlida. No has fet cap pregunta.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La pregunta no correspon a cap keyword.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error. An unexpected error occurred while retrieving the user.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/knowledge/question")
    public ResponseEntity<List<KnowledgeDTO>> getEntryByQuestion(@RequestBody String question) {
        List<KnowledgeDTO> foundEntriesList = knowledgeService.searchByQuestionKeywords(question);

        if (foundEntriesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(foundEntriesList);
        }
    }

    @Operation(
            summary = "Fa la cerca d'una pregunta del client",
            description = "Retorna una entrada o una llista d'entrades amb totes les que corresponen a una keyword.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Entrada ja existeix a la base de dades.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "201",
                            description = "Entrada creada amb èxit.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error. An unexpected error occurred while retrieving the user.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/knowledge")
    public ResponseEntity<KnowledgeEntry> create(@RequestBody KnowledgeCreateDTO queryDTO) {

        KnowledgeEntry createdEntry = knowledgeService.createKnowledgeEntry(queryDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

}
