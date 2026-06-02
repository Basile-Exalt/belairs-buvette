# Agent Instruction Maintenance Guidelines

This document explains how to maintain `AGENTS.md` and other AI-facing instruction files in this repository.

The goal is to keep agent instructions accurate, short enough to be usable, and concrete enough to affect behavior.

## 1. Optimize for Actionability

Good agent instructions help an agent decide:
- where code belongs
- what level of test to add
- which repository rule is non-negotiable
- what tradeoff to prefer when several choices exist

If a sentence does not change agent behavior, it is probably not worth keeping.

## 2. Keep a Clear Layering of Instructions

Use these files for different scopes:
- `AGENTS.md`: global collaboration rules and durable repo-wide agent behavior
- `docs/agents/instructions/...`: focused guidance for one concern such as testing, documentation, workflow, or coding style

Do not duplicate the full content of one file into another.
Do summarize key rules in the place where the agent is most likely to start.

## 3. Prefer Repository Facts Over Aspirational Language

Write what is true or intentionally required in this repository.

Prefer:
- `Domain rules belong in the domain module.`
- `Use Testcontainers for stateful infrastructure adapters.`

Avoid:
- `Try to keep code clean.`
- `Use best practices.`

If a convention is desired but not yet established, state it as a rule or recommendation explicitly.

## 4. Remove Dead Templates and Placeholders

Instruction files should not contain:
- empty headings
- `...`
- generic scaffolding copied from a template without repository-specific meaning
- references to files that do not exist

When a file is intentionally short, make it short because it is complete, not because it is unfinished.

## 5. Resolve Contradictions Quickly

If two instruction files conflict:
- fix the contradiction instead of leaving both versions in place
- prefer the more specific file when behavior is scoped to one concern
- update summaries in higher-level files if the lower-level rule changed materially

An agent cannot follow contradictory rules reliably.

## 6. Keep the Entry Documents Fast to Scan

`AGENTS.md` should be optimized for first-pass reading.

That means:
- short sections
- explicit headings
- operational bullet points
- links to detail documents only when needed

Long reasoning, historical notes, and extended examples belong in supporting documents, not in the entry summary.

## 7. Update Instructions When the Architecture Moves

Any of these changes should trigger an instruction review:
- a new framework is adopted
- a new module or boundary is introduced
- testing strategy changes
- build or validation commands change
- packaging or naming conventions stabilize

If the repository evolves and instructions do not, the instructions become actively harmful.

## 8. Review Checklist

Before finishing changes to an agent instruction file, verify:
- the file reflects the current repository state
- key architectural rules are stated plainly
- the document is actionable without opening five more files first
- linked files exist and are relevant
- no important section is still generic template text
