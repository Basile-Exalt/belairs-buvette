# [Application + Domain] Acknowledger une commande et calculer le temps de preparation

**Contexte**

En tant que bartender, je veux acknowledge une commande pour informer le festivalier qu'elle est en preparation, avec un temps estime de readiness calcule selon la composition de la commande.

**Critères d'acceptation**

```gherkin
Feature: Acknowledge de commande et estimation de readiness

  Scenario: Calculer le temps pour boissons sans alcool uniquement
    Given une commande contenant 3 types differents de boissons sans alcool
    When le bartender acknowledge la commande
    Then le temps estime est de 3 minutes
    And le festivalier est notifie que la commande est en preparation

  Scenario: Calculer le temps pour commande mixte de boissons
    Given une commande contenant 1 boisson alcoolisee normale et 2 boissons alcoolisees premium
    When le bartender acknowledge la commande
    Then le temps estime est de 8 minutes

  Scenario: Calculer le temps avec meals et boissons en parallele
    Given une commande contenant 2 types de meals et des boissons dont la preparation la plus longue dure 6 minutes
    When le bartender acknowledge la commande
    Then le temps estime est de 26 minutes
    And le calcul applique 10 minutes par type de meal plus la preparation boisson la plus longue
```

**Notes**

- Regle metier: les items de meme type sont prepares ensemble, on raisonne en nombre de types pour certains items.
