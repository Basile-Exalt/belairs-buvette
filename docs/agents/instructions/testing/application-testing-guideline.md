# Application Testing Guideline

This document defines how to test the `application` module.

The application layer exists to expose domain behavior cleanly to callers. Its tests should prove the incoming boundary, not re-implement the domain test suite.

Language: Tests and test-related code in the `application` module (including test names and fixtures) must be written in English.

## Purpose of Application Tests

Application tests should verify:
- request-to-use-case wiring
- input validation and error mapping
- response status codes and payload shape
- serialization and deserialization behavior
- application-level configuration that affects the public contract

Application tests should not become the main place where business rules are proven.

## Preferred Test Style

Default to integration tests at the API boundary exposed by the application module.

Good application tests:
- call the application through HTTP or the closest real incoming adapter
- assert status, headers, and response payloads
- use deterministic test doubles only for dependencies outside the application boundary

Avoid overly isolated controller unit tests unless they are the only practical way to validate a narrow mapping concern.

## Dependency Strategy

When testing the application layer:
- prefer using real request mapping and validation configuration
- stub or fake downstream ports only when needed to isolate the boundary under test
- keep domain behavior deterministic and minimal in the test setup

If Spring, Micronaut, Quarkus, or another framework is introduced later, keep tests centered on the contract rather than on framework internals.

## What to Assert

At minimum, assert the parts of the public contract that matter to a client:
- status code
- content type when relevant
- response body fields and error payloads
- validation messages or error codes when they are part of the contract

Examples of good scenarios:
- valid request returns the expected success response
- malformed request is rejected with the correct client error
- domain rejection is translated into the agreed API error contract
- unknown resource yields the correct not-found behavior

## What Not to Assert

Avoid asserting:
- internal method calls when the HTTP result already proves the behavior
- private mapper details unless they surface in the contract
- business calculations already covered in domain tests

## Test Data Guidance

Keep request payloads small and focused.
Use names and values from the business language in `FEATURES.md` so the test reads like a user scenario.

Avoid bloated fixtures that hide what the scenario actually depends on.

## Recommended Scope Split

Use application tests for:
- endpoint routing
- request validation
- API schema behavior
- error translation
- happy-path integration from request to use-case call

Use domain tests for:
- token balance rules
- pricing rules
- order lifecycle rules
- readiness time calculations
- any other core business invariant

## When to Add Application Tests

Add or update application tests when a change affects:
- a public endpoint or contract
- input validation
- output mapping
- transport-level error semantics
- request authentication or authorization behavior once introduced

If a change only affects pure business rules, the main test should usually live in `domain`.
