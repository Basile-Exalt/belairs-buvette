---
title: "Mark order as ready"
mcp_issue_number: 6
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/6"
---

# Mark order as ready

## Context
Bartenders mark orders ready when prepared items are available.

## Success criteria
- Only mark ready when sufficient prepared items exist.
- Notify festival goer that order is ready.

## Description
Provide endpoint for marking an order ready, validate prepared inventory, and send pickup notification.

## Gherkin Scenarios
```gherkin
Scenario: Mark ready with enough items
  Given an order with prepared items available
  When bartender marks it as ready
  Then the festival goer is notified

Scenario: Mark ready without enough items
  Given an order without enough prepared items
  When bartender attempts to mark ready
  Then the action is rejected
```
