---
agent: agent
name: TDD Refactor step
description: This prompt automates the Refactor phase of TDD by taking the Green-step JSON output as input and moving minimal production code out of the test into appropriate production classes, cleaning and renaming for clarity while keeping behavior unchanged and tests green.
argument-hint: Input must be the JSON output produced by the TDD Green step. Example: {"testFilePath":"...","testMethod":"...","changes":[...],"recommendedRefactor":{...},...}
tools: ['execute/getTerminalOutput','execute/runInTerminal','read/readFile','edit/createFile','edit/editFiles','search','todo']
model: GPT-5 mini (copilot)
---

# TDD Refactor step — Instructions

1. Parse the input JSON produced by the TDD Green step. Required fields you may rely on:
   - `testFilePath` (relative path to the test modified in Green)
   - `testMethod` (method name targeted in Green)
   - `changes` (list of in-test changes that introduced minimal production-like code)
   - `recommendedRefactor` (optional guidance: `targetProductionFiles`, `suggestedChange`, `riskLevel`)

2. Goal: Move production-like code OUT of the test class into proper production classes under `src/main/java`, perform small cleanups and renames so code follows project conventions, and keep all tests green at every micro-step.

3. Constraints and rules (must follow exactly):
   - Do not change the external behavior of the code (tests must pass unchanged).
   - Each modification must be a minimal, atomic refactor (one small change per micro-step).
   - After each micro-step, run only the affected test(s) or the single targeted test. If failing, revert that micro-step and report the failure.
   - Prefer adding small production files (classes, records, interfaces) under `domain/src/main/java/...` (or appropriate module) following repository package conventions. Do NOT add production code in test packages.
   - Use the coding conventions found in the repository: `AGENTS.md`, `docs/agents/instructions/coding/java-coding-guidelines.md`, and testing guidelines in `docs/agents/instructions/testing/*.md`.
   - Keep changes minimal: avoid large refactors or cross-cutting moves in a single step.

4. Micro-step workflow (repeat until refactor complete):
   a) Identify a small piece of production-like code inside the test (class, method, enum) from `changes` in the input JSON.
   b) Create the smallest production class that preserves behavior (use `record` for DTO/command when appropriate).
   c) Update the test to reference the new production class (only imports and type references inside the test). Do not alter test assertions or logic.
   d) Run the single targeted test (or minimal test set). If it passes, record the micro-step as successful and proceed. If it fails, undo and produce a diagnostic.

5. Naming, packaging, and placement rules:
   - Place domain use-case classes and DTOs under `domain/src/main/java/com/it/exalt/belair/domain/...` following feature package used by the test (e.g., `order`).
   - Use English-only identifiers and tests as per repository rules.
   - Prefer `record` for simple immutable command/DTO types; prefer `final` classes for small immutable value objects.
   - Keep visibility package-private or public only when required by tests or cross-module wiring.

6. Tests and verification:
   - After each micro-step run the single test named in `testMethod` using Gradle command for the domain module. If Gradle execution is impossible in the environment, report `manual-check-required` with a `reason` and the exact shell command to run locally.
   - Do not modify other tests. If other tests break, rollback last micro-step and report the failure with Gradle output.

7. Output requirements (final response):
   - Provide a JSON object wrapped in a triple-backtick `json` code block (so the JSON can be copied independently). The JSON must contain:
     - `greenInput`: the original Green-step JSON input (unchanged)
     - `steps`: an ordered array of micro-steps applied. Each micro-step is an object with:
         - `stepNumber` (int)
         - `description` (short string)
         - `fileChanges`: array of `{ "file": "path", "description": "what changed", "patch": "unified diff or snippet" }`
         - `junitSummary` for test(s) run at this step (`testsRun`, `failures`, `errors`, `skipped`)
         - `result`: `passed` or `failed`
     - `finalResult`: `passed` | `failed` | `manual-check-required`
     - `recommendedRefactor`: the refactor suggestions you used or decided on (targets, suggestedChange, riskLevel)
     - `manualStepsNeeded`: boolean
     - `reason`: optional string when `manual-check-required` or `failed`

   - The JSON must be the only content inside the ` ```json ... ``` ` block. Outside of that block, a single-line concise human summary is allowed.

8. Safety and limits:
   - Never change behavior to make tests pass (no altering assertions or test logic).
   - Avoid touching unrelated files. If a cross-module change is necessary, break it into micro-steps and run tests after each step.

9. Example micro-step (illustrative):
   - Move `private static class PlaceOrderCommand` from the test into `domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderCommand.java` as a `public record PlaceOrderCommand(String festivalgoerId, String itemName, int quantity);`
   - Update the test imports to use the new class.
   - Run the targeted test.

10. When finished, produce the final JSON as specified and include `recommendedRefactor` that identifies any production files worth further cleanup.

References: see `AGENTS.md`, `docs/agents/instructions/coding/java-coding-guidelines.md`, and testing guidelines under `docs/agents/instructions/testing/` for coding and testing conventions to follow.

## Examples

Below are concrete example micro-steps that illustrate the intended workflow and the JSON structure expected for each micro-step. These are examples only — adapt names and packages to the specific test you are refactoring.

Example 1 — Extract a DTO/Command from test into production as a `record`:

- Micro-step description: Move `private static class PlaceOrderCommand` from test into `domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderCommand.java` as a `public record`.
- Test changes: replace the in-test class reference with an import to the new production `PlaceOrderCommand`.
- Run: targeted test only.

Sample step JSON (one micro-step):

```json
{
   "stepNumber": 1,
   "description": "Extract PlaceOrderCommand into production as record",
   "fileChanges": [
      {
         "file": "domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderCommand.java",
         "description": "Add public record PlaceOrderCommand",
         "patch": "+ public record PlaceOrderCommand(String festivalgoerId, String itemName, int quantity) { }"
      },
      {
         "file": "domain/src/test/java/com/it/exalt/belair/domain/order/PlaceOrderUseCaseTest.java",
         "description": "Replace test-local PlaceOrderCommand with import of production record",
         "patch": "+ import com.it.exalt.belair.domain.order.PlaceOrderCommand;\n- private static class PlaceOrderCommand { ... }"
      }
   ],
   "junitSummary": { "testsRun": 1, "failures": 0, "errors": 0, "skipped": 0 },
   "result": "passed"
}
```

Example 2 — Move a result/value object and enum into production:

- Micro-step description: Move `PlaceOrderResult` and `OrderStatus` enum into `domain/src/main/java/com/it/exalt/belair/domain/order/` as production classes, preserving getters and values.
- Test changes: import and reference the production types.
- Run: targeted test only.

Sample step JSON:

```json
{
   "stepNumber": 2,
   "description": "Extract PlaceOrderResult and OrderStatus into production",
   "fileChanges": [
      {
         "file": "domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderResult.java",
         "description": "Add immutable PlaceOrderResult with orderId and status",
         "patch": "+ public final class PlaceOrderResult { private final String orderId; private final OrderStatus status; public PlaceOrderResult(String orderId, OrderStatus status) { this.orderId = orderId; this.status = status; } public String getOrderId() { return orderId; } public OrderStatus getStatus() { return status; } }"
      },
      {
         "file": "domain/src/main/java/com/it/exalt/belair/domain/order/OrderStatus.java",
         "description": "Add OrderStatus enum",
         "patch": "+ public enum OrderStatus { PENDING }"
      },
      {
         "file": "domain/src/test/java/com/it/exalt/belair/domain/order/PlaceOrderUseCaseTest.java",
         "description": "Replace in-test result and enum with imports",
         "patch": "+ import com.it.exalt.belair.domain.order.PlaceOrderResult;\n+ import com.it.exalt.belair.domain.order.OrderStatus;\n- private static class PlaceOrderResult { ... }\n- private enum OrderStatus { PENDING }"
      }
   ],
   "junitSummary": { "testsRun": 1, "failures": 0, "errors": 0, "skipped": 0 },
   "result": "passed"
}
```

Example 3 — Extract a minimal use-case implementation to production and wire the test to it:

- Micro-step description: Move `PlaceOrderUseCase` behavior into `domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderUseCase.java` with minimal deterministic behavior (e.g., use injected `IdGenerator` and `OrderRepository` ports). Update test to instantiate the production use case with simple test fakes for ports.
- Break this into sub-steps if necessary: first create the class skeleton, run tests; then implement a constructor that accepts test fakes, run tests; finally move logic from test into production, run tests.

Sample step JSON (skeleton):

```json
{
   "stepNumber": 3,
   "description": "Move PlaceOrderUseCase into production and wire with test fakes",
   "fileChanges": [
      {
         "file": "domain/src/main/java/com/it/exalt/belair/domain/order/PlaceOrderUseCase.java",
         "description": "Add production PlaceOrderUseCase with minimal API",
         "patch": "+ public class PlaceOrderUseCase { public PlaceOrderResult placeOrder(PlaceOrderCommand cmd) { return new PlaceOrderResult(java.util.UUID.randomUUID().toString(), OrderStatus.PENDING); } }"
      },
      {
         "file": "domain/src/test/java/com/it/exalt/belair/domain/order/PlaceOrderUseCaseTest.java",
         "description": "Instantiate production PlaceOrderUseCase instead of test-local one",
         "patch": "+ PlaceOrderUseCase useCase = new PlaceOrderUseCase();\n- PlaceOrderUseCase useCase = new PlaceOrderUseCase(/* dependencies to be implemented */);"
      }
   ],
   "junitSummary": { "testsRun": 1, "failures": 0, "errors": 0, "skipped": 0 },
   "result": "passed"
}
```

Notes on examples:
- Keep each example micro-step small and verifiable. If a planned micro-step would touch many files, split it into smaller steps.
- Use the `recommendedRefactor` in the original Green-step JSON to prioritize which test-local artifacts to extract first.


*** End of prompt file ***
