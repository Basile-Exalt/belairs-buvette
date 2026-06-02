# [Application + Domain] Commander plusieurs articles dans une seule commande

**Contexte**

En tant que festivalier, je veux pouvoir commander plusieurs articles boisson et nourriture en une seule fois. Le systeme doit verifier que le cout total respecte simultanement les soldes drink tokens et food tokens.

**Critères d'acceptation**

```gherkin
Feature: Commande multi-articles

  Scenario: Commander plusieurs articles avec soldes suffisants
    Given un festivalier dispose de 3 drink tokens et 5 food tokens
    When il commande 1 boisson alcoolisee normale et 2 snacks
    Then la commande est acceptee
    And son solde drink tokens passe a 2
    And son solde food tokens passe a 3

  Scenario: Refuser une commande depassant le solde drink tokens
    Given un festivalier dispose de 1 drink token et 5 food tokens
    When il commande 1 boisson alcoolisee premium et 1 snack
    Then la commande est refusee pour solde drink tokens insuffisant
    And aucun token n'est debite

  Scenario: Refuser une commande depassant le solde food tokens
    Given un festivalier dispose de 3 drink tokens et 2 food tokens
    When il commande 1 boisson alcoolisee normale et 1 meal
    Then la commande est refusee pour solde food tokens insuffisant
    And aucun token n'est debite
```

**Notes**

- Le debit de tokens doit etre atomique sur l'ensemble de la commande.
