Anàlisi Tècnica del Projecte: Assistent d'Onboarding
====================================================

1\. Regles i Comportament del Bot
---------------------------------

El nucli funcional del bot es basa en una lògica de cerca flexible dissenyada per millorar l'experiència de l'usuari.

-   **Lògica de Cerca per Paraula Clau**: En lloc de buscar coincidències exactes de la pregunta, el sistema extreu les paraules clau rellevants.

    1.  El text introduït per l'usuari es **normalitza**: es converteix a minúscules, s'eliminen els accents (`sol-licitut` -> `solicitut`) i els signes de puntuació. Els apòstrofs es tracten com a separadors (`d'oficina` -> `d oficina`).

    2.  El text netejat es divideix en paraules individuals.

    3.  El sistema busca a la base de dades la `keyword` que coincideix amb alguna de les paraules extretes de la pregunta.

-   **Resultats Múltiples**: Si diverses entrades comparteixen una paraula clau, el sistema retorna totes les coincidències, proporcionant a l'usuari tota la informació rellevant.

-   **Comportament si no hi ha resposta**: Si cap paraula clau coincideix, el bot informa a l'usuari que no ha trobat cap resposta, suggerint que reformuli la pregunta.

2\. Estructura de la Base de Coneixement
----------------------------------------

La persistència de les dades es gestiona amb una base de dades en memòria **H2**, que s'inicialitza a l'arrancar l'aplicació a partir d'un fitxer `entries.json` situat a la carpeta de recursos.

#### Entitat `KnowledgeEntry`

La informació s'estructura a la base de dades seguint el model de l'entitat `KnowledgeEntity`, que conté els següents camps:

-   `id` (Integer): Identificador únic autogenerat.
-   `question` (String): La pregunta completa, tal com es mostrarà a l'usuari.
-   `answer` (String): La resposta associada.
-   `keyword` (String): La paraula clau principal. Una única paraula, normalitzada i en minúscules, utilitzada per a la cerca optimitzada.

Aquest disseny compleix amb el format pla requerit (`question`, `answer`) alhora que afegeix un camp `keyword` per a una indexació i cerca eficients.

3\. Arquitectura i Decisions Tècniques
--------------------------------------

S'ha optat per un disseny bassat en una **Arquitectura de N-Capes (Layered Architecture)** seguint el patró **Model-Vista-Controlador (MVC)**, que separa clarament les responsabilitats i promou un codi modular i mantenible.

-   **Capa de Interfície i Adaptadors (`Controller`)**: Responsable exclusivament de la interacció amb l'exterior. El `KnowledgeController` gestiona les peticions HTTP de l'API REST, mentre que el `ConsoleAssistantRunner` gestiona la línia de comandes. Aquesta capa treballa exclusivament amb **DTOs (Data Transfer Objects)** per adaptar el format de les dades externes al model intern de l'aplicació.

-   **Capa de Servei (`Service`)**: Conté tota la lògica de negoci. Orquestra les operacions de manera centralitzada, valida les dades i actua com a pont entre els controladors i la capa de dades. Tradueix DTOs a Entitats i viceversa. Llença les excepcions de negoci.

-   **Capa de Persistència (`Repository`)**: Abstrau l'accés a la base de dades. La interfície `KnowledgeRepo` (basada en Spring Data JPA) defineix les operacions de dades treballant exclusivament amb objectes **Entitat**.

#### Decisions Tècniques Clau:

-   **Framework**: **Spring Boot** per la seva rapidesa de desenvolupament, injecció de dependències, servidor web incrustat i un ecosistema robust.

-   **Persistència**: **H2 Database** per la seva simplicitat d'integració, execució en memòria ideal per a proves i prototipat ràpid, eliminant la necessitat de configuracions externes.

-   **Separació Entitat-DTO**: L'ús de DTOs a la capa de l'API desacobla el contracte públic de l'API de l'estructura interna de la base de dades, la qual cosa aporta seguretat i flexibilitat.

-   **Gestió Centralitzada d'Excepcions**: S'utilitza la classe `KnowledgeGlobalException` amb `@RestControllerAdvice` per capturar totes les excepcions llançades des del servei i traduir-les a respostes HTTP estandarditzades (ex: 400 Bad Request), mantenint els controladors nets.

-   **Documentació d'API**: **`springdoc-openapi`** per generar automàticament una documentació interactiva amb Swagger UI, facilitant les proves i la integració.

4\. Consideracions per a Manteniment Futur
------------------------------------------

L'arquitectura escollida facilita l'evolució i l'escalabilitat del projecte.

-   **Nous Canals d'Interacció**: Gràcies al desacoblament de la lògica de negoci (`KnowledgeService`), afegir nous canals (com un bot de Slack, Telegram o una interfície web) és senzill. Només caldria crear un nou "adaptador" que consumeixi el servei existent.

-   **Suport Multilingüe**: Es podria afegir un camp `locale` (ex: "ca-ES", "en-US") a l'entitat `KnowledgeEntry`. Els mètodes de cerca del servei podrien acceptar un paràmetre d'idioma per filtrar els resultats.

-   **Escalabilitat de la Cerca**: Per a una base de coneixement amb milers d'entrades, la cerca actual podria alentir-se. Una millora futura seria implementar un motor de cerca de text complet com **Elasticsearch** o **Hibernate Search** per a consultes més ràpides i complexes (cerques per rellevància, correcció d'errors tipogràfics, etc.).

-   **Incrementar flexibilitat de la cerca**: possibilitant més d'una paraula clau per entrada facilitaria trobar més coincidències entre les preguntes de l'usuari i les entrades en el repositori.

-   **Aprenentatge Automàtic (NLP)**: Per a una comprensió més avançada de les preguntes de l'usuari, es podrien integrar llibreries de Processament de Llenguatge Natural (NLP) per identificar la intenció real de l'usuari en lloc de basar-se només en paraules clau.
