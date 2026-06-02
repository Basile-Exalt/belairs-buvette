# Infrastructure Testing Guideline

This document defines how to test the `infrastructure` module.

Infrastructure tests should prove that technical adapters correctly implement the domain ports they claim to satisfy.

## Core Principle

Test the adapter against the real technical boundary whenever practical.

Language: Tests and any test-support code in the `infrastructure` module must be written in English.

Examples:
- a repository against a real database
- an HTTP client against a controllable test server
- a messaging adapter against a test broker or a realistic substitute

The goal is not to re-test the domain. The goal is to verify translation, persistence, configuration, and failure semantics.

## What Infrastructure Tests Should Cover

Infrastructure tests should verify:
- the adapter fulfills the domain port contract
- mappings between technical and domain models are correct
- persistence round-trips preserve the expected information
- configuration assumptions required by the adapter are valid
- technical failures are surfaced or translated appropriately

## Preferred Test Style

Default to integration tests.

Prefer:
- Testcontainers for databases and stateful services
- small test-specific configuration assembled as close as possible to production wiring
- explicit setup and cleanup inside the test fixture or container lifecycle

Avoid testing repository or client classes purely with mocks unless the technical library is impossible to run realistically.

## Repository Adapter Guidance

For persistence adapters, verify at least:
- save then load returns an equivalent domain object
- updates replace or merge state as intended
- missing data returns the correct empty result
- constraints or uniqueness behavior are handled cleanly

If the project adds migrations later, validate them against a clean containerized database.

## External Client Guidance

For HTTP or messaging clients, verify at least:
- request shape and required headers when they matter
- success response mapping
- error response mapping
- timeout or connectivity failure behavior when the port contract cares about it

Use deterministic stubs or local test servers instead of broad mocks when possible.

## Scheduling and Time-Based Adapters

If infrastructure introduces schedulers or timers:
- test the adapter behavior separately from the domain rule
- keep the time-based business decision in the domain behind a port
- only verify that the scheduler triggers the expected boundary call at the correct integration level

## What Not to Test Here

Avoid using infrastructure tests as the primary place to validate:
- token business rules
- pricing rules
- order domain invariants
- use-case branching that can be proven faster in domain tests

If a failure is caused by a business rule, fix or extend the domain test suite first.
# Testing Guidelines

## Domain Module Testing Guidelines

Entry point: tests must drive the system through a use-case handler.

### High-level Principles

- Entry point: tests must drive the system through a use-case handler (the "primary" port implementation).
- Dependency Inversion: tests must depend only on abstraction (interfaces/ports), never on concrete technical implementations.
- No mocking frameworks: provide lightweight, in-process fakes for secondary ports.
- Fakes must implement a `TestState<T>` interface so tests can seed initial state.
- Tests follow Given-When-Then (Behavior Driven) structure.

### TestState contract

```java
public interface TestState<T, ID> {
    void add(T item);
    Optional<T> find(ID id);
    List<T> findAll();
}
```

### Fake repository example

```java
public class FakeOrderRepository implements OrderRepository, TestState<Order, String> {
    private final List<Order> store = new ArrayList<>();

    @Override
    public void add(Order item) {
        store.removeIf(o -> o.id().equals(item.id()));
        store.add(item);
    }

    @Override
    public Optional<Order> find(String id) {
        return store.stream().filter(o -> o.id().equals(id)).findFirst();
    }

    @Override
    public List<Order> findAll() { return List.copyOf(store); }
}
```

### Test structure (Given-When-Then)

```java
@Test
void givenValidBasket_whenCreateOrder_thenOrderCreatedAndPaymentRequested() {
    // Given
    fixture.state1().add(/* pre-existing entity */);

    // When
    var result = fixture.useCase().create(/* command */);

    // Then
    assertThat(result).isSuccessful();
    assertThat(fixture.state1().findAll()).hasSize(1);
}
```

## API Testing Guidelines (Application Module)

- Contract-driven: assert response shape and status against the documented API contract.
- Use mocked use-cases and deterministic fakes (WireMock) for fast, deterministic tests.
- Use RestAssured or lightweight HTTP clients as the test driver.

## Infrastructure Testing Guidelines

- Use Testcontainers for all driven adapters.
- Validate Flyway/Liquibase migrations against a clean DB started by Testcontainers.

```java
@Testcontainers
class UserRepositoryIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("buvette_test")
        .withUsername("test")
        .withPassword("test");

    @Test
    void savesAndLoadsUser() {
        // your test
    }
}
```