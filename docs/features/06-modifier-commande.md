# [Application + Domain] Modifier une commande

**Contexte**

En tant que festivalier, je veux ajouter ou retirer des articles de ma commande tant qu'elle n'est pas acknowledgee par le bartender. Si la commande est deja acknowledgee, une demande de changement doit etre transmise au bartender.

**Critères d'acceptation**

```gherkin
Feature: Modification de commande

  Scenario: Modifier une commande non acknowledgee
    Given une commande non acknowledgee contenant 1 snack
    And le festivalier dispose d'un solde suffisant
    When il ajoute 1 boisson alcoolisee normale
    Then la commande est mise a jour
    And les tokens sont recalcules selon le nouveau contenu

  Scenario: Refuser une modification qui depasse le solde
    Given une commande non acknowledgee
    And le festivalier ne dispose pas des tokens necessaires au nouveau contenu
    When il demande une modification
    Then la modification est refusee
    And la commande initiale reste inchangee

  Scenario: Transmettre une demande de changement pour commande acknowledgee
    Given une commande deja acknowledgee par le bartender
    When le festivalier demande une modification
    Then une demande de changement est notifiee au bartender
    And la commande n'est pas modifiee immediatement
```

**Notes**

- La gestion de l'approbation bartender est couverte par une feature dediee.
