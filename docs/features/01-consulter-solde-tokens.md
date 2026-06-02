# [Application + Domain] Consulter le solde restant de tokens

**Contexte**

En tant que festivalier, je dois pouvoir consulter mon solde de tokens boisson et nourriture a tout moment de la journee. Cette fonctionnalite garantit la visibilite du pouvoir d'achat restant et applique la regle de reinitialisation quotidienne des tokens.

**Critères d'acceptation**

```gherkin
Feature: Consultation du solde de tokens

  Scenario: Consulter un solde positif de tokens
    Given un festivalier dispose de 4 drink tokens et 7 snack tokens pour la journee en cours
    When il consulte son solde
    Then le systeme retourne 4 drink tokens et 7 snack tokens

  Scenario: Consulter un solde nul
    Given un festivalier dispose de 0 drink token et 0 snack token pour la journee en cours
    When il consulte son solde
    Then le systeme retourne 0 drink token et 0 snack token

  Scenario: Reinitialisation des tokens le jour suivant
    Given un festivalier avait 2 drink tokens et 3 snack tokens non utilises en fin de journee
    And un nouveau jour de festival commence
    When il consulte son solde
    Then le systeme retourne 6 drink tokens et 9 snack tokens
```

**Notes**

- Regle metier critique: aucun solde negatif n'est autorise.
