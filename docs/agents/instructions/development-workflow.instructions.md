# Development Workflow Instructions

This document defines the default working method for agents contributing code in this repository.

The goal is to keep changes small, behavior-driven, and aligned with the project architecture.

Language: All production code, tests, identifiers, and public APIs created as part of this workflow must be written in English.

## 1. Start From Behavior, Not From Frameworks

Before writing code:
- identify the user-facing behavior being changed
- map that behavior to the relevant story in `FEATURES.md` when possible
- determine which module owns the decision

Default ownership:
- business rule or invariant: `domain`
- transport contract or request validation: `application`
- external system interaction or persistence: `infrastructure`

Do not begin by adding frameworks, annotations, or technical layers unless the behavior actually requires them.

## 2. Prefer Vertical Slices

When implementing a feature, favor one small end-to-end slice over broad scaffolding.

A good slice usually includes:
- one use case or business capability
- the minimal model needed to express it
- one or more focused tests
- only the adapters required to expose or persist that capability

Avoid creating empty placeholder classes across all modules just to "prepare" future work.
sett
## 3. Default Implementation Sequence

Unless the task explicitly requires another path, use this order:
1. refine the rule from `FEATURES.md` or the user request
2. add or update domain tests
3. implement domain behavior
4. add or adapt secondary ports
5. implement infrastructure adapters as needed
6. expose the behavior through the application layer
7. add the narrowest integration test that proves the boundary

This keeps business logic anchored in the domain.

## 4. Testing Strategy by Layer

Use the cheapest test that can prove the behavior.

Default order:
1. domain test for business rules
2. application integration test for HTTP or API behavior
3. infrastructure integration test for adapter semantics

Do not push every rule into slow end-to-end tests.
Do not use controller tests to compensate for missing domain tests.

Detailed rules live in:
- `docs/agents/instructions/testing/domain-testing-guideline.md`
- `docs/agents/instructions/testing/application-testing-guideline.md`
- `docs/agents/instructions/testing/infrastructure-testing-guideline.md`

## 5. Keep Diffs Reviewable

Agents should prefer:
- one behavior change at a time
- minimal public API changes
- explicit names over clever abstractions
- additive refactoring only when it directly supports the task at hand

Avoid mixing these in the same change unless necessary:
- feature work
- large renames
- architectural refactors
- formatting-only edits
- unrelated cleanup

## 6. Dependencies and Libraries

Before adding a dependency:
- confirm the repository does not already provide an adequate built-in option
- confirm the dependency solves a current problem, not a hypothetical one
- keep versions centralized in `gradle/libs.versions.toml` when versions are introduced

If the codebase has no established framework for a concern yet, prefer plain Java and a clean port until a framework becomes necessary.

## 7. Error Handling and Domain Boundaries

When modeling failures:
- keep business failures explicit in the domain
- distinguish domain rule violations from technical failures
- avoid leaking technical exception types across module boundaries

Use ports and adapter translation so the domain is not forced to understand HTTP, SQL, messaging, or vendor-specific concerns.

## 8. Documentation During Delivery

Update documentation when a change does one of these things:
- clarifies a domain rule
- establishes a new package or naming convention
- introduces a new external dependency or integration pattern
- adds a reusable agent instruction or repository convention

Preferred targets:
- `FEATURES.md` for feature rule clarifications
- `README.md` for setup or execution changes
- `docs/agents/instructions/...` for stable implementation guidance

## 9. Validation Before Completion

Before considering work done, run the narrowest relevant verification:
- module tests for touched behavior
- full `./gradlew test` if the change spans several modules
- `./gradlew build` when build wiring or dependencies changed

If you cannot run validation, state that explicitly in the completion message.

## 10. Decision Rules for Agents

If multiple valid designs exist:
- choose the one that keeps the domain easiest to test
- prefer fewer concepts over more concepts
- preserve future flexibility through ports, not through speculative indirection everywhere

If the task is too large or ambiguous:
- propose a smaller slice
- state the main architectural risk plainly
- ask for clarification only when the ambiguity blocks a real design choice
