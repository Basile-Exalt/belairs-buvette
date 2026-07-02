---
title: "Place an order for food"
mcp_issue_number: 10
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/10"
---

# Place an order for food

## Context
Festival goers can order snacks and meals; snacks cost 1 food token, meals cost 3 food tokens.

## Success criteria
- API to create food order and compute food token cost.
- Reject orders that exceed food token balance.

## Description
Implement order creation for food items with token accounting and validation.

## Gherkin Scenarios
```gherkin
Scenario: Order snack
  Given a festival goer with 1 food token
  When they order a snack
  Then the order is accepted and 1 token is deducted

Scenario: Order meal without tokens
  Given a festival goer with 2 food tokens
  When they order a meal
  Then the order is rejected due to insufficient food tokens
```
