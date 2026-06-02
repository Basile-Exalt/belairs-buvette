# [Application + Domain] Revoir et approuver ou rejeter une demande de changement sur commande acknowledgee

**Contexte**

En tant que bartender, je veux traiter les demandes de changement sur commandes deja acknowledgees. La demande ne peut etre acceptee que si au moins un item deja prepare peut etre transfere a une autre commande.

**Critères d'acceptation**

```gherkin
Feature: Traitement bartender des demandes de changement

  Scenario: Accepter une demande de changement avec transfert possible
    Given une commande acknowledgee avec une demande de changement en attente
    And au moins un item deja prepare peut etre transfere a une autre commande
    When le bartender approuve la demande
    Then la commande est mise a jour
    And le festivalier recoit un nouveau temps estime de readiness

  Scenario: Rejeter une demande de changement sans transfert possible
    Given une commande acknowledgee avec une demande de changement en attente
    And aucun item deja prepare ne peut etre transfere
    When le bartender rejette la demande
    Then la commande reste inchangee
    And le festivalier est notifie du rejet
```

**Notes**

- Cette feature complete la feature de demande de modification post-acknowledge.
