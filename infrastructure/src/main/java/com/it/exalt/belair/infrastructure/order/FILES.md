Infrastructure - order package files

Files in `infrastructure/src/main/java/com/it/exalt/belair/infrastructure/order`:

- `OrderEntity.java` — JPA entity mapping for orders (persistence representation).
- `OrderLineEntity.java` — JPA entity mapping for order lines.
- `JpaOrderCrudRepository.java` — (legacy) Spring Data repository interface.
- `JpaOrderRepository.java` — Spring Data `JpaRepository` interface used by `OrderRepositoryImpl`.
- `OrderRepositoryImpl.java` — implementation of domain `CommandeRepository` mapping between `Commande` and `OrderEntity`.

Notes:
- Implementation/adapters remain in `infrastructure`; keep mapping logic here and update when domain model changes.
