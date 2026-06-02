# [Application + Domain] Passer une commande de groupe avec pooling de tokens

**Contexte**

En tant que groupe de festivaliers, nous voulons mutualiser nos tokens pour passer une commande unique. Le systeme doit accepter des contributions heterogenes et verifier la couverture complete du cout de la commande.

**Critères d'acceptation**

```gherkin
Feature: Commande de groupe

  Scenario: Accepter une commande de groupe avec pooling suffisant
    Given Alice dispose de 2 drink tokens et 1 food token
    And Bob dispose de 1 drink token et 3 food tokens
    When le groupe commande 1 boisson alcoolisee premium et 1 meal
    And Alice contribue 2 drink tokens et 1 food token
    And Bob contribue 0 drink token et 2 food tokens
    Then la commande de groupe est acceptee
    And les soldes sont debites selon les contributions declarees

  Scenario: Refuser une commande de groupe avec pooling insuffisant
    Given Alice dispose de 1 drink token et 0 food token
    And Bob dispose de 0 drink token et 1 food token
    When le groupe commande 1 boisson alcoolisee premium et 1 meal
    Then la commande de groupe est refusee pour tokens insuffisants
    And aucun token n'est debite

  Scenario: Accepter des contributions partielles
    Given trois festivaliers participent a une commande de groupe
    When chacun contribue n'importe quelle quantite dans la limite de son solde
    Then la commande est validee uniquement si la somme des contributions couvre le cout total
```

**Notes**

- La commande de groupe suit les memes regles de pricing que les commandes individuelles.
