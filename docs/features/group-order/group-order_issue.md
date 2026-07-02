---
title: "Group token pooling for group orders"
mcp_issue_number: 4
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/4"
---

# Group token pooling for group orders

## Context
Groups of festival goers may pool tokens to place a single order paid collectively.

## Success criteria
- Support creating a group order with contributions from multiple users.
- Validate pooled tokens cover total cost.

## Description
Implement group orders where participants contribute tokens; lock contributions until order completes or is cancelled and enforce pooled validation.

## Gherkin Scenarios
```gherkin
Scenario: Successful group order
  Given two festival goers contributing tokens that sum to order cost
  When they submit a group order
  Then the order is accepted and tokens are deducted from contributors

Scenario: Group order insufficient pooled tokens
  Given contributors whose pooled tokens do not cover cost
  When they submit a group order
  Then the order is rejected
```
