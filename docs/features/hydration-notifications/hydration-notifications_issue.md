---
title: "Hydration reminder notifications"
mcp_issue_number: 5
mcp_issue_url: "https://github.com/Basile-Exalt/belairs-buvette/issues/5"
---

# Hydration reminder notifications

## Context
Bartenders want regular notifications sent to festival goers reminding them to drink water between 11:00 and 19:00, with increased frequency if >3 alcoholic drinks in the past hour.

## Success criteria
- Scheduled job sends notifications hourly between 11:00 and 19:00.
- For users with >3 alcoholic drinks in past hour, send every 30 minutes.

## Description
Implement scheduled notification service with time window and per-user frequency rules; ensure idempotence and opt-out capability.

## Gherkin Scenarios
```gherkin
Scenario: Hourly reminder
  Given current time 12:00
  When scheduled job runs
  Then all users receive hydration reminder

Scenario: Increased frequency for heavy drinkers
  Given a user drank 4 alcoholic drinks in the last hour
  When scheduled job runs at 12:30
  Then that user receives a reminder (30-minute cadence)
```
