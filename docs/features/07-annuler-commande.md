# [Application + Domain] Annuler une commande

**Contexte**

En tant que festivalier, je veux annuler une commande tant qu'elle n'est pas acknowledgee afin de recuperer les tokens reserves. Le systeme doit aussi emettre une confirmation d'annulation.

**Critères d'acceptation**

```gherkin
Feature: Annulation de commande

  Scenario: Annuler une commande non acknowledgee
    Given une commande non acknowledgee avec tokens deja debites
    When le festivalier annule la commande
    Then la commande est annulee
    And tous les tokens de la commande sont rembourses
    And une confirmation est envoyee au festivalier

  Scenario: Refuser l'annulation d'une commande acknowledgee
    Given une commande deja acknowledgee
    When le festivalier tente de l'annuler
    Then l'annulation est refusee
    And aucun remboursement n'est effectue
```

**Notes**

- Le remboursement doit restaurer exactement les soldes avant commande.
