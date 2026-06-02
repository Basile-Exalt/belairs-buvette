# [Application + Domain] Passer une commande de nourriture

**Contexte**

En tant que festivalier, je veux commander de la nourriture (snack ou meal) avec un cout en food tokens defini par type d'article. Cette fonctionnalite permet de debiter le bon nombre de tokens au moment de la commande.

**Critères d'acceptation**

```gherkin
Feature: Commande de nourriture

  Scenario: Commander un snack avec solde suffisant
    Given un festivalier dispose de 2 food tokens
    When il commande un snack
    Then la commande est acceptee
    And son solde food tokens passe a 1

  Scenario: Commander un meal avec solde suffisant
    Given un festivalier dispose de 4 food tokens
    When il commande un meal
    Then la commande est acceptee
    And son solde food tokens passe a 1

  Scenario: Commander un meal sans solde suffisant
    Given un festivalier dispose de 2 food tokens
    When il commande un meal
    Then la commande est refusee pour solde insuffisant
    And son solde food tokens reste a 2
```

**Notes**

- Cout attendu: snack 1 food token, meal 3 food tokens.
