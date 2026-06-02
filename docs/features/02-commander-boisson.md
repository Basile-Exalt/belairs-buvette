# [Application + Domain] Passer une commande de boisson

**Contexte**

En tant que festivalier, je veux commander des boissons avec une consommation de tokens qui depend du type de boisson. Cette fonctionnalite permet de distinguer les boissons sans alcool, alcoolisees normales et alcoolisees premium.

**Critères d'acceptation**

```gherkin
Feature: Commande de boisson

  Scenario: Commander une boisson sans alcool
    Given un festivalier dispose de 2 drink tokens
    When il commande une boisson sans alcool
    Then la commande est acceptee
    And son solde drink tokens reste a 2

  Scenario: Commander une boisson alcoolisee normale
    Given un festivalier dispose de 2 drink tokens
    When il commande une boisson alcoolisee normale
    Then la commande est acceptee
    And son solde drink tokens passe a 1

  Scenario: Commander une boisson alcoolisee premium sans solde suffisant
    Given un festivalier dispose de 1 drink token
    When il commande une boisson alcoolisee premium
    Then la commande est refusee pour solde insuffisant
    And son solde drink tokens reste a 1
```

**Notes**

- Cout attendu: sans alcool 0, alcool normal 1, alcool premium 2 drink tokens.
