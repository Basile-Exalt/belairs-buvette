---
title: "Place an order for a drink"
mcp_issue_number: 9
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/9"
---

# Place an order for a drink

## Context
Festival goers must be able to order drinks (non-alcoholic, normal alcoholic, premium alcoholic) with token costs applied.

## Success criteria
- API to create drink order with item types and token cost calculation.
- Non-alcoholic drinks do not consume drink tokens; normal = 1 token, premium = 2 tokens.
- Validation rejects orders exceeding drink token balance.

## Description
Implement order creation for drinks, including token cost calculation and validation against user's drink token balance.

## Gherkin Scenarios
```gherkin
Scenario: Order non-alcoholic drink
  Given a festival goer with 0 drink tokens
  When they order a non-alcoholic drink
  Then the order is accepted and no drink tokens are deducted

Scenario: Order premium alcoholic drink without enough tokens
  Given a festival goer with 1 drink token
  When they order a premium alcoholic drink
  Then the order is rejected due to insufficient drink tokens
```
