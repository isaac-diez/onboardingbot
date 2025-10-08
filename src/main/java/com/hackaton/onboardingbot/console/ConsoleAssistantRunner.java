package com.hackaton.onboardingbot.console;

import com.hackaton.onboardingbot.model.KnowledgeCreateDTO;
import com.hackaton.onboardingbot.model.KnowledgeDTO;
import com.hackaton.onboardingbot.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ConsoleAssistantRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsoleAssistantRunner.class);
    private final KnowledgeService knowledgeService;

    public ConsoleAssistantRunner(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciant l'assistent de consola...");
        System.out.println("=============================================");
        System.out.println("🤖 Benvingut/da a l'Assistent d'Onboarding!");
        System.out.println("=============================================");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                displayMenu();
                System.out.print("Tria una opció: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        listAllEntries();
                        break;
                    case "2":
                        askQuestion(scanner);
                        break;
                    case "3":
                        createNewEntry(scanner);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("❌ Opció no vàlida. Si us plau, tria un número de l'1 al 4.");
                        break;
                }
            }
        }
        System.out.println("👋 Gràcies per utilitzar l'assistent. Fins aviat!");
    }

    private void displayMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Llistar totes les preguntes");
        System.out.println("2. Fer una pregunta");
        System.out.println("3. Crear una nova entrada");
        System.out.println("4. Sortir");
        System.out.println("----------------------");
    }

    private void listAllEntries() {
        System.out.println("\n--- Base de Coneixement Actual ---");
        List<KnowledgeDTO> entries = knowledgeService.getAllEntries();
        if (entries.isEmpty()) {
            System.out.println("No hi ha cap entrada a la base de coneixement.");
        } else {
            entries.forEach(entry ->
                    System.out.printf("❓ PREGUNTA: %s\n💬 RESPOSTA: %s\n\n", entry.getQuestion(), entry.getAnswer())
            );
        }
    }

    private void askQuestion(Scanner scanner) {
        System.out.print("\nEscriu la teva pregunta: ");
        String userQuestion = scanner.nextLine();

        List<KnowledgeDTO> results = knowledgeService.searchByQuestionKeywords(userQuestion);

        if (!results.isEmpty()) {
            System.out.println("\n💬 RESPOSTA(ES) TROBADA(ES):");
            // Itera sobre tots els resultats i els mostra
            results.forEach(entry ->
                    System.out.printf("❓ PREGUNTA: %s\n💬 RESPOSTA: %s\n\n", entry.getQuestion(), entry.getAnswer())
            );
        } else {
            System.out.println("\n🚫 Ho sento, no he trobat cap resposta per a la teva pregunta. Pots fer-ne una altra.");
        }
    }

    private void createNewEntry(Scanner scanner) {
        System.out.println("\n--- Creació de Nova Entrada ---");
        System.out.print("Introdueix la nova pregunta: ");
        String newQuestion = scanner.nextLine();

        System.out.print("Introdueix la resposta corresponent: ");
        String newAnswer = scanner.nextLine();

        System.out.print("Introdueix una keyword que defineixi la pregunta: ");
        String newKeyword = scanner.nextLine();

        if (newQuestion.isBlank() || newAnswer.isBlank()) {
            System.out.println("❌ La pregunta i la resposta no poden estar buides.");
            return;
        }

        KnowledgeCreateDTO newEntryDTO = new KnowledgeCreateDTO(newQuestion, newAnswer, newKeyword);
        knowledgeService.createKnowledgeEntry(newEntryDTO);

        System.out.println("\n✅ Entrada creada amb èxit!");
    }
}
