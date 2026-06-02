# [Application + Domain + Infrastructure] Envoyer des notifications regulieres d'hydratation

**Contexte**

En tant que bartender, je veux que le systeme envoie des rappels d'hydratation aux festivaliers selon des frequences dependantes de la consommation alcoolisee recente, uniquement dans une plage horaire definie.

**Critères d'acceptation**

```gherkin
Feature: Notifications d'hydratation

  Scenario: Envoyer un rappel horaire standard
    Given il est 14:00 pendant un jour de festival
    And un festivalier a consomme 3 boissons alcoolisees ou moins sur la derniere heure
    When la tache de notification s'execute
    Then le festivalier recoit un rappel d'hydratation toutes les 60 minutes

  Scenario: Augmenter la frequence pour forte consommation
    Given il est 15:00 pendant un jour de festival
    And un festivalier a consomme plus de 3 boissons alcoolisees sur la derniere heure
    When la tache de notification s'execute
    Then le festivalier recoit un rappel d'hydratation toutes les 30 minutes

  Scenario: Ne pas envoyer de notification hors plage horaire
    Given il est 20:00 pendant un jour de festival
    When la tache de notification s'execute
    Then aucune notification d'hydratation n'est envoyee
```

**Notes**

- Fenetre d'envoi: de 11:00 a 19:00 inclus.
- Le message doit rester amical et encourager une consommation responsable.
