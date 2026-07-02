---
title: "Consult token balance"
mcp_issue_number: 3
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/3"
---

# Consult token balance

## Context
Festival goers have two token types: drink and snack. They need an API to view current balances and ensure no negative balances.

## Success criteria
- API endpoint returns drink and snack token balances for a user.
- Balances never negative.
- Daily allocation (9 food, 6 drink) is applied per festival day.

## Description
Expose a REST API to fetch a festival goer's current token balances. Include current day allocation logic and prevent negative balances.

## Gherkin Scenarios
```gherkin
Scenario: Get token balances
  Given a festival goer with 5 drink tokens and 3 food tokens
  When they request their token balances
  Then the API returns drinkTokens = 5 and foodTokens = 3

Scenario: No negative balance
  Given a festival goer with 0 drink tokens
  When they request their token balances
  Then the API returns drinkTokens = 0 and not negative
```
