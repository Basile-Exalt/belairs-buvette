---
title: "Modify an order"
mcp_issue_number: 7
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/7"
---

# Modify an order

## Context
Festival goers must be able to modify an order before it is acknowledged by the bartender.

## Success criteria
- Allow adding/removing items before acknowledgment.
- Reject modifications that would exceed balances.
- Notify bartender when changes are requested after acknowledgment.

## Description
Implement order modification with state checks; if order acknowledged, create a change request workflow notifying bartender.

## Gherkin Scenarios
```gherkin
Scenario: Modify before acknowledgment
  Given an order not yet acknowledged
  When the festival goer adds an item within balance
  Then the order is updated

Scenario: Modify after acknowledgment
  Given an acknowledged order
  When the festival goer requests a change
  Then the bartender is notified and the change is subject to approval
```
