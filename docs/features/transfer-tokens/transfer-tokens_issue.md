---
title: "Transfer tokens between festival goers"
mcp_issue_number: 12
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/12"
---

# Transfer tokens between festival goers

## Context
Festival goers can transfer up to three tokens of each type to another user; recipient must confirm.

## Success criteria
- Transfer endpoint with validations (max 3 per type, no negative sender balance).
- Confirmation flow for recipient.

## Description
Implement token transfer with sender-side validation, recipient confirmation, and atomic update of balances.

## Gherkin Scenarios
```gherkin
Scenario: Successful transfer with confirmation
  Given sender with 3 drink tokens
  When sender transfers 2 drink tokens and recipient confirms
  Then sender balance decreases and recipient increases accordingly

Scenario: Transfer exceeding limit
  Given sender tries to transfer 4 tokens
  When they submit the transfer
  Then the transfer is rejected (max 3)
```
