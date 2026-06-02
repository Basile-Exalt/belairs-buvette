# [Application + Domain] Marquer une commande comme prete

**Contexte**

En tant que bartender, je veux marquer une commande comme prete uniquement si tous les items necessaires sont prepares. Le festivalier doit etre notifie quand la commande est disponible.

**Critères d'acceptation**

```gherkin
Feature: Passage d'une commande a l'etat prete

  Scenario: Marquer une commande prete avec stock prepare suffisant
    Given une commande acknowledgee
    And le stock d'items prepares couvre tous les items de la commande
    When le bartender marque la commande comme prete
    Then le statut de la commande devient READY
    And le festivalier recoit une notification de retrait

  Scenario: Refuser le passage a READY si des items manquent
    Given une commande acknowledgee
    And au moins un item n'est pas prepare en quantite suffisante
    When le bartender tente de marquer la commande comme prete
    Then l'action est refusee
    And le statut de la commande reste inchange
```

**Notes**

- Le controle doit porter sur la totalite des items de la commande.
