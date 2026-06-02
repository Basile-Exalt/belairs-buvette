# Domain Testing Guideline

This document defines how to test the `domain` module.

The domain module is the primary home of business behavior. Most feature confidence should come from tests here.

## Core Principle

Drive tests through domain use cases or the smallest public domain entry point that expresses the behavior.

Tests should prove business outcomes, not internal implementation steps.

## What Domain Tests Must Cover

Domain tests are the default place to prove:
- token allocation and balance rules
- pricing rules for food and drink items
- group contribution and transfer rules
- order lifecycle constraints
- acknowledgment and readiness calculations
- time-based domain decisions expressed independently of scheduling technology

If a rule appears in `FEATURES.md`, assume the domain should own it unless it is purely transport-related or purely technical.

## Preferred Test Style

Use Given-When-Then structure.

Example structure:

```java
@Test
void givenSufficientTokens_whenPlacingAnOrder_thenTokensAreDebited() {
    // Given
    var fixture = new OrderFixture();

    // When
    var result = fixture.placeOrder(/* command */);

    // Then
    assertThat(result).isSuccess();
}
```

Test names should describe behavior, not methods.

## Dependency Strategy

Domain tests should depend on:
- domain entry points
- domain model types
- small in-memory fakes implementing domain ports

Domain tests should not depend on:
- HTTP
- SQL
- concrete infrastructure adapters
- mocking frameworks when a simple fake is clearer

## Fakes Over Mocks

Prefer lightweight in-memory fakes for secondary ports such as:
- repositories
- clocks
- ID generators
- notification senders
- transaction boundaries when they must be represented

Fakes should be:
- deterministic
- easy to seed
- easy to inspect after the action
- limited to the capability required by the test

If several tests need the same fake, extract it. If only one test needs it, keep it local and simple.

## Fixture Guidance

Use fixtures to reduce noise, not to hide behavior.

A good fixture makes it obvious:
- what initial state matters
- which use case is exercised
- what outcome is asserted

Avoid giant object mothers that create irrelevant defaults for every scenario.

## What Good Domain Assertions Look Like

Prefer asserting domain-observable outcomes such as:
- returned result objects
- emitted domain events if the domain uses them
- state stored through fake repositories
- tokens debited or refunded
- estimated preparation time values

Avoid asserting internal call sequences when the resulting domain state already proves correctness.

## Boundary Modeling Guidance

When a business rule depends on time, randomness, or external systems:
- introduce a domain port for that concern
- fake that port in tests
- keep the business rule deterministic

Examples:
- `ClockPort` for time-based token reset or reminder eligibility
- `OrderRepository` for persistence lookup
- `NotificationPort` for outbound notifications

## Test Portfolio Expectation

For each non-trivial use case, aim to cover:
- a happy path
- the main rule violation paths
- important edge cases
- one or more regression scenarios when a bug is fixed

Overlap between domain tests is acceptable when it protects important business rules from regressions.