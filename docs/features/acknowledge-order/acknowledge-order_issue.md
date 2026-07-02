---
title: "Acknowledge an order and provide ETA"
mcp_issue_number: 1
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/1"
---

# Acknowledge an order and provide ETA

## Context
Bartenders acknowledge orders and provide estimated time of readiness based on item types and workload.

## Success criteria
- API for bartender to acknowledge an order and compute ETA per rules.
- Notification sent to festival goer with ETA.

## Description
Calculate ETA per the rules (non-alcoholic: 1 min per type, normal: 2 min, premium: 3 min, snacks: 2 min/type, meals: 10 min/type plus longest drink time) and persist acknowledgement state.

## Gherkin Scenarios
```gherkin
Scenario: Acknowledge mixed order
  Given an order with meals and normal drinks
  When bartender acknowledges it
  Then an ETA is computed and festival goer is notified
```
