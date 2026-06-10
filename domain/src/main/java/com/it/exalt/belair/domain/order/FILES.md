Domain - order package files

This file lists the actual files currently present under:
`domain/src/main/java/com/it/exalt/belair/domain/order` and its immediate subpackages.

Files in this package:

- `Article.java` — value object representing an article/item.
- `OrderStatus.java` — enum for order statuses.
- `StockRepository.java` — domain port for stock operations.

Subpackages and key files:

- `entity/`
	- `Commande.java` — domain aggregate (order) implementation.
- `repository/`
	- `CatalogueRepository.java` — domain port for catalog existence checks.
	- `CommandeRepository.java` — domain port for order persistence.
- `usecase/`
	- `CreerCommandeUseCase.java` — use-case interface for creating orders.
	- `CreerCommandeUseCaseImpl.java` — implementation of create-order use case.
	- `PasserCommandeUseCase.java` — use-case for placing orders.
- `dto/`
	- `CreerCommandeRequest.java`
	- `CreerCommandeResponse.java`
	- `PasserCommandeCommand.java`
	- `PasserCommandeResult.java`
- `exception/`
	- `ArticleInconnuException.java`
	- `StockInsuffisantException.java`

Notes:
- Keep domain ports in `domain` and implementations in `infrastructure`.
