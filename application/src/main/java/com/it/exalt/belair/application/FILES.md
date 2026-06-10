Application - core files

Files in `application/src/main/java/com/it/exalt/belair/application`:

- `Application.java` — Spring Boot application entry point.
- `ApplicationConfig.java` — bean wiring and configuration.
- `OrderController.java` — REST controller for order endpoints.
- `UnauthorizedException.java` — exception used by controllers/tests.
- `.gitkeep` — placeholder file.

Notes:
- Controllers depend on `domain` ports and DTOs; update imports when domain refactors occur.
