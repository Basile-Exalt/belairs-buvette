# [Application + Domain] Transferer des tokens entre festivaliers

**Contexte**

En tant que festivalier, je veux transferer des tokens a un autre festivalier dans une limite definie, avec confirmation du destinataire. Cette fonctionnalite evite les transferts non consentis et protege les soldes contre les valeurs negatives.

**Critères d'acceptation**

```gherkin
Feature: Transfert de tokens entre festivaliers

  Scenario: Transferer des tokens dans la limite autorisee
    Given Alice dispose de 5 drink tokens et 5 food tokens
    And Bob est destinataire du transfert
    When Alice initie un transfert de 3 drink tokens et 2 food tokens vers Bob
    And Bob confirme le transfert
    Then le transfert est execute
    And le solde d'Alice est reduit de 3 drink tokens et 2 food tokens
    And le solde de Bob est augmente des memes montants

  Scenario: Refuser un transfert depassant la limite par type
    Given Alice dispose de 10 drink tokens
    When Alice initie un transfert de 4 drink tokens vers Bob
    Then le transfert est refuse pour depassement de limite

  Scenario: Refuser un transfert non confirme
    Given Alice initie un transfert valide vers Bob
    When Bob ne confirme pas le transfert
    Then aucun token n'est deplace
```

**Notes**

- Limite metier: maximum 3 tokens par type et par transfert.
