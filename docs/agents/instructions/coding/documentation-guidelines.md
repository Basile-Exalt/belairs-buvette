# Documentation Guidelines

This repository uses documentation to reduce ambiguity for both humans and AI agents.

Documentation should help the next contributor act faster, not merely satisfy a template.

## General Rules

Write documentation when it clarifies one of these things:
- business behavior
- architectural intent
- repository conventions
- setup or execution steps
- stable testing expectations

Do not write documentation that only repeats obvious code.

## Preferred Documentation Targets

Use the right file for the right purpose:
- `FEATURES.md` for product behavior, rules, and acceptance criteria
- `README.md` for setup, build, execution, and repository overview
- `docs/agents/instructions/...` for durable implementation rules and conventions

## Style Rules

Documentation should be:
- specific to this repository
- short enough to scan quickly
- explicit about decisions and constraints
- consistent with the architecture and module boundaries

Prefer:
- concrete rules
- short sections
- bullet lists for operational guidance
- examples only when they remove ambiguity

Avoid:
- vague aspirations without an action rule
- duplicated guidance across many files without a clear source of truth
- placeholders such as `...`, `TBD`, or empty headings unless absolutely necessary

## When Updating Feature Documentation

When a task clarifies product behavior:
- update `FEATURES.md` if the rule itself changed or was clarified
- use the vocabulary of the business domain
- state constraints explicitly
- keep rules testable

Good feature rules are:
- observable
- unambiguous
- phrased so a test can be derived from them

## When Updating Agent Instructions

When updating files under `docs/agents/instructions/`:
- optimize for quick decision support
- state where code should go
- state how it should be tested
- state what to avoid
- reference other documents only when the target adds real detail

If the most important instruction lives only in a linked file, consider summarizing it where the agent first needs it.

## Architecture and Design Docs

When documenting architecture:
- describe module responsibilities
- describe dependency direction
- describe important boundaries and ports
- include tradeoffs only when they affect future decisions

If an architectural choice is new and likely to be reused, record it in a stable doc rather than leaving it implicit in a single pull request.

## Quality Check Before Finishing

Before considering documentation complete, verify:
- the document answers a concrete question a future contributor will likely have
- the guidance matches the current repository state
- examples and commands are still valid
- referenced files actually exist