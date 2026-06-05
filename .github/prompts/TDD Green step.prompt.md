---
agent: agent
name: TDD Green step
description: This prompt is used to implement the minimal code changes so that one previously failing test (Red step) now passes — following the "Green" phase of TDD.
argument-hint: Implement the minimal change required to make the test pass. Input must specify the test file path and the test method name to make pass: {"testFilePath": "path/to/TestFile.java", "testMethod": "testMethodName"}
tools: ['execute/getTerminalOutput', 'execute/runInTerminal', 'read/problems', 'read/readFile', 'read/terminalSelection', 'read/terminalLastCommand', 'edit/createDirectory', 'edit/createFile', 'edit/editFiles', 'search', 'upstash/context7/*', 'todo']
model: GPT-5 mini (copilot)
---

# TDD Green step — Instructions

1. Analysez l'input JSON pour récupérer :
   - `testFilePath`: chemin relatif du fichier de test créé à l'étape Red.
   - `testMethod`: nom exact de la méthode de test qui doit passer.

2. Localisez et ouvrez le fichier de test spécifié. Vérifiez que la méthode de test existe et qu'elle échoue actuellement.

3. Objectif principal : implémenter le minimum de code nécessaire POUR FAIRE PASSER LA MÉTHODE DE TEST.
   - Implémentez le code strictement DANS la classe de test elle-même (par exemple des classes privées, méthodes helper, ou petites implémentations inline) — ne touchez PAS au code de production.
   - N'ajoutez pas de fonctionnalités non demandées par le test.
   - Ne modifiez pas la méthode de test elle-même, ni d'autres méthodes de test présentes dans la même classe.

4. Écrivez des modifications claires et minimales :
   - Si le test vérifie un comportement simple, fournissez une implémentation minimale (ex : une méthode privée qui retourne une valeur attendue, une classe interne qui satisfait l'API utilisée par le test).
   - Commentez brièvement (1-2 lignes) pourquoi la modification permet au test de passer.

5. Exécutez uniquement le test ciblé pour vérifier qu'il passe.
   - Confirmez que : le test ciblé passe et qu'aucun autre test de la même classe n'a été modifié ou cassé.

6. Livrez une sortie structurée au format JSON (voir section "Sortie JSON" ci-dessous).


## Règles strictes
- Implémentation : uniquement dans la classe de test (pas de nouveaux fichiers de production).
- Aucun changement de signature ou de logique dans la méthode de test elle-même.
- Ne modifiez PAS d'autres tests dans la même classe.
- N'ajoutez PAS d'imports ou de dépendances externes non nécessaires.
- La solution doit être minimale et ciblée.


## Exemples d'input / output

### Exemple 1 — cas simple
Input:
{
  "testFilePath": "domain/src/test/java/com/example/domain/user/UserBalanceTest.java",
  "testMethod": "shouldReturnZeroWhenNoTokens"
}

Action attendue (output résumé) :
- Ouvrir `domain/src/test/java/com/example/domain/user/UserBalanceTest.java`.
- Ajouter une petite classe privée `FakeTokenRepository` ou une méthode helper dans la classe de test qui retourne 0 pour le solde.
- Exécuter la méthode de test ciblée, vérifier qu'elle passe.

Sortie JSON attendue (exemple):
{
  "testFilePath": "domain/src/test/java/com/example/domain/user/UserBalanceTest.java",
  "testMethod": "shouldReturnZeroWhenNoTokens",
  "changes": [
    {
      "file": "domain/src/test/java/com/example/domain/user/UserBalanceTest.java",
      "description": "Added private FakeTokenRepository class and helper method to return zero balance"
    }
  ],
  "result": "passed",
  "junitSummary": {
    "testsRun": 1,
    "failures": 0,
    "errors": 0,
    "skipped": 0
  }
}


### Exemple 2 — cas nécessitant une méthode utilitaire
Input:
{
  "testFilePath": "application/src/test/java/com/example/app/OrderHandlerTest.java",
  "testMethod": "shouldMarkOrderReady"
}

Action attendue :
- Ajouter une méthode privée `createReadyOrder()` dans la classe de test qui construit l'objet attendu.
- Modifier uniquement la classe de test pour appeler cette méthode depuis le scénario d'arrangement du test.
- Exécuter la méthode de test ciblée et vérifier le passage.


## Sortie structurée au format JSON (schéma)

La sortie JSON finale doit comporter précisément ces champs :

- `testFilePath` (string) : chemin relatif vers le fichier de test modifié.
- `testMethod` (string) : nom de la méthode de test ciblée.
- `changes` (array of objects) : liste des changements appliqués. Chaque objet contient :
  - `file` (string) : chemin relatif du fichier modifié.
  - `description` (string) : courte description textuelle du changement (1 phrase).
  - `patch` (optional string) : diff ou extrait de code ajouté (si possible).
- `result` (string) : `passed` ou `failed`.
- `junitSummary` (object) : résumé d'exécution JUnit comme dans les exemples (`testsRun`, `failures`, `errors`, `skipped`).


## Contraintes d'exécution
- NE PAS implémenter de code de production dans des packages `main/`.
- NE PAS modifier d'autres fichiers de test que celui ciblé.
- Le but est d'obtenir un test `green` minimal et reproductible.


## Consignes de rendu
- Répondez par une unique sortie JSON valide conforme au schéma ci-dessus.
- Incluez des descriptions concises et un `patch` si la modification est simple à afficher inline.
- Si l'exécution du test est impossible dans l'environnement actuel, précisez l'étape qui a été faite et fournissez la sortie JSON avec `result: "manual-check-required"` et un champ `reason`.

IMPORTANT: La réponse peut contenir une brève explication ou un résumé en texte libre, mais le JSON final DOIT être placé dans un bloc de code séparé et copiable. Ce bloc doit utiliser une balise de code triple backticks avec le langage `json` :

```json
{ ... }
```

Le bloc ` ```json ` doit contenir uniquement l'objet JSON (aucun texte, commentaire ou métadonnée avant ou après dans ce bloc). Cela permet de copier facilement le JSON indépendamment du reste de la réponse.

### Exemple concret — implémentation minimale IN-TEST

Ci-dessous un exemple concret basé sur le scénario "shouldReturnZeroWhenNoTokens". Le test lui-même reste inchangé ; toutes les implémentations minimales nécessaires pour qu'il passe sont ajoutées DANS la classe de test (classes internes, helpers, etc.).

Fichier ciblé : domain/src/test/java/com/example/domain/user/UserBalanceTest.java

Exemple (à placer dans la même classe de test) :

```java
package com.example.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserBalanceTest {

    private FakeTokenRepository tokenRepo;
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        tokenRepo = new FakeTokenRepository();
        balanceService = new BalanceService(tokenRepo);
    }

    @Test
    void shouldReturnZeroWhenNoTokens() {
        // Given a user with no tokens
        User user = new User("user1");

        // When computing balance
        long balance = balanceService.getBalance(user.getId());

        // Then the balance is zero
        assertThat(balance).isZero();
    }

    // -------------------------
    // Minimal test-local implementations (placed INSIDE the test class)
    // These are intentionally minimal and only satisfy the test.
    // -------------------------

    private static class User {
        private final String id;
        User(String id) { this.id = id; }
        String getId() { return id; }
    }

    private interface TokenRepository {
        long countTokensForUser(String userId);
    }

    private static class FakeTokenRepository implements TokenRepository {
        @Override
        public long countTokensForUser(String userId) {
            // Minimal behavior for this test: no tokens for any user.
            return 0L;
        }
    }

    private static class BalanceService {
        private final TokenRepository repo;
        BalanceService(TokenRepository repo) { this.repo = repo; }

        long getBalance(String userId) {
            // Minimal production-like logic implemented inside test
            return repo.countTokensForUser(userId);
        }
    }
}
```

Pourquoi ça passe : la `FakeTokenRepository` renvoie 0 et `BalanceService#getBalance` retourne cette valeur — le test observe donc 0 comme attendu.

### Exemple négatif — ce qu'il NE FAUT PAS faire

- Ne pas modifier la méthode de test pour la rendre trivially passing (cheat) :

```java
// Mauvais : modification du test lui-même pour forcer le passage
@Test
void shouldReturnZeroWhenNoTokens() {
    assertThat(true).isTrue(); // NE PAS FAIRE
}
```

- Ne pas ajouter ou modifier du code de production dans `src/main/java` pour faire passer le test dans cette étape Green.
- Ne pas altérer d'autres tests dans la même classe ou ajouter des comportements non requis (ex : ajout de nouvelles dépendances, side-effects, ou logique non demandée).

## Sortie JSON pour l'étape Refactor

L'agent qui réalisera l'étape Refactor a besoin d'une sortie structurée et enrichie pour savoir quelles modifications de production sont pertinentes et sécuritaires. Ajoutez toujours ce bloc JSON dans la sortie finale du Green step.

Schéma requis :

{
  "testFilePath": "string",              // chemin relatif du fichier de test modifié
  "testMethod": "string",                // nom de la méthode de test ciblée
  "changes": [                             // liste des changements appliqués dans la classe de test
    {
      "file": "string",
      "description": "string",
      "patch": "string (optional)"
    }
  ],
  "result": "passed|failed|manual-check-required",
  "junitSummary": {                        // résumé d'exécution JUnit
    "testsRun": number,
    "failures": number,
    "errors": number,
    "skipped": number
  },
  "recommendedRefactor": {                 // suggestions destinées à l'agent Refactor
    "targetProductionFiles": ["string"],  // fichiers dans src/main/java candidats pour refactor
    "suggestedChange": "string",          // courte description de la refactorisation recommandée
    "riskLevel": "low|medium|high"        // estimation du risque
  },
  "testsToUpdate": ["string"],            // autres tests potentiellement concernés (chemins)
  "manualStepsNeeded": boolean,             // true si l'agent humain doit valider ou compléter
  "reason": "string (optional)"           // si manual-check-required ou pour contexte
}

Exemple complet (valeur réelle) :

{
  "testFilePath": "domain/src/test/java/com/example/domain/user/UserBalanceTest.java",
  "testMethod": "shouldReturnZeroWhenNoTokens",
  "changes": [
    {
      "file": "domain/src/test/java/com/example/domain/user/UserBalanceTest.java",
      "description": "Added private FakeTokenRepository and BalanceService minimal impl",
      "patch": "+ private static class FakeTokenRepository implements TokenRepository { ... }"
    }
  ],
  "result": "passed",
  "junitSummary": { "testsRun": 1, "failures": 0, "errors": 0, "skipped": 0 },
  "recommendedRefactor": {
    "targetProductionFiles": ["domain/src/main/java/com/example/domain/user/BalanceService.java"],
    "suggestedChange": "Move minimal BalanceService from test into production service, add TokenRepository impl",
    "riskLevel": "low"
  },
  "testsToUpdate": ["domain/src/test/java/com/example/domain/user/OtherBalanceTest.java"],
  "manualStepsNeeded": false
}

Notes :
- Remplissez `targetProductionFiles` seulement si vous identifiez un emplacement clair et déjà existant pour la logique à extraire. Si aucun fichier n'existe, laissez la liste vide et marquez `manualStepsNeeded: true`.
- `riskLevel` aide l'agent Refactor à prioriser les changements automatisables.
- Si l'environnement ne permet pas d'exécuter les tests, retournez `result: "manual-check-required"` et indiquez `reason`.

