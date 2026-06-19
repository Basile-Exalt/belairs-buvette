# Java Coding Guidelines

These guidelines define how Java code should be written in this repository.

The project is a Java 21 multi-module backend built around a hexagonal architecture. The style should support readability, testability, and clear separation between domain and technical concerns.

## 1. General Principles

Prefer code that is:
- explicit
- immutable by default
- easy to test without frameworks
- named in the language of the business domain

All code, tests, and identifiers must be written in English. Use English for class, method, variable, package names, and test method names so the repository remains consistent and accessible to all contributors.

Avoid cleverness when straightforward code is enough.

## 2. Formatting

- use 4 spaces for indentation
- do not use tabs
- prefer lines under 120 characters
- keep opening braces on the same line

Example:

```java
if (tokens.remaining() < cost) {
    throw new InsufficientTokensException();
}
```

## 3. Naming

- package names: lower case
- classes, interfaces, enums, records: PascalCase
- methods, fields, parameters: camelCase
- constants: `static final` and UPPER_SNAKE_CASE

Choose names that reveal intent.

Prefer:
- `PlaceOrderUseCase`
- `TokenBalance`
- `AcknowledgeOrderCommand`
- `NotificationPort`

Avoid vague names such as:
- `Helper`
- `Manager`
- `Processor`
- `Data`
- `Service` when a more precise name exists

## 4. Package and Module Discipline

Respect module boundaries:
- `domain` contains business logic and port definitions
- `application` contains incoming adapters and API models
- `infrastructure` contains outgoing adapters and technical implementations

Do not place business rules in application controllers or infrastructure adapters.

Inside a module, organize packages so that related behavior stays close together. Prefer feature- or capability-oriented grouping over dumping unrelated classes into a generic package.

## 5. Immutability First

Prefer immutable types unless mutation is clearly necessary.

Use records for:
- commands
- queries
- DTOs
- value objects when a record models them cleanly

Example:

```java
public record TransferTokensCommand(
    FestivalGoerId senderId,
    FestivalGoerId recipientId,
    int drinkTokens,
    int snackTokens
) {
}
```

For classes with invariants, validate them in constructors or factory methods.

## 6. Null and Optional Handling

- do not return `null` from public methods
- use `Optional<T>` for optional results when absence is a valid outcome
- validate required constructor and method arguments explicitly

Example:

```java
this.repository = Objects.requireNonNull(repository, "repository must not be null");
```

## 7. Domain Modeling Rules

The domain should model business meaning, not technical storage details.

Prefer:
- value objects for concepts with invariants
- use cases as clear entry points for behavior
- ports for secondary dependencies

Avoid:
- anemic domain types that only shuttle data between layers
- exposing persistence entities directly to the domain
- leaking framework annotations into the domain unless there is a compelling reason

## 8. Exceptions and Error Signaling

Use error signaling consistently.

Recommended approach:
- domain rule violations should be explicit and meaningful
- technical failures should be translated at module boundaries
- avoid throwing generic `RuntimeException` without a strong reason

Choose one style per feature and keep it consistent:
- domain-specific exceptions
- explicit result types

## 9. Ports and Adapters

Ports should describe required capabilities in business-friendly language.

Examples:
- `OrderRepository`
- `ClockPort`
- `ReminderNotificationSender`

Adapters should describe the implementation mechanism.

Examples:
- `JdbcOrderRepository`
- `PostgresFestivalGoerRepository`
- `HttpNotificationClient`

Keep mapping code explicit when converting between domain and technical models.

## 10. Time, IDs, and Side Effects

When business logic depends on time, IDs, randomness, or outbound notifications:
- hide those concerns behind ports
- inject them into the use case
- fake them in tests

This keeps the domain deterministic and easy to test.

## 11. Testability

Write production code so tests can exercise it through public behavior.

Prefer:
- constructor injection
- explicit collaborators
- small focused classes

Avoid:
- static global state
- hidden singleton access
- constructors that do heavy technical work

## 12. Dependencies

- prefer the Java standard library when it is sufficient
- add third-party dependencies only when they clearly reduce complexity
- keep dependency versions centralized in `gradle/libs.versions.toml` when versions are introduced

Because this repository is still a starter, do not assume frameworks that are not already present.

## 13. Imports and Generated Code

- When agents or contributors generate or patch Java files, prefer adding regular `import` statements at the top of the file and use simple class names in the body and in annotations.
- Avoid leaving fully-qualified class names (FQCNs) inline in annotations or code. FQCNs make diffs noisy and hinder automated refactorings.
- If a FQCN is used as a short-term workaround in a quick patch, follow up with a small refactor commit that adds the proper import and replaces the FQCN with the simple name.

Example (preferred):

```java
import com.it.exalt.belair.infrastructure.order.OrderRepository;

@Import(OrderRepository.class)
public class SomeTest { ... }
```

Example (avoid):

```java
@Import(com.it.exalt.belair.infrastructure.order.OrderRepository.class)
public class SomeTest { ... }
```