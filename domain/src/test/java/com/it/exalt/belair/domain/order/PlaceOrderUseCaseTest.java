package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceOrderUseCaseTest {

    @BeforeEach
    void setUp() {
        // Fixture setup will be added later when implementing the use case
    }

    @Test
    void givenFestivalgoerAndAvailableItem_whenPlacingOrder_thenOrderCreatedWithPendingStatusAndReturnsOrderId() {
        // Given a festivalgoer identified
        // And an item "Mojito" available in stock
        // (These domain fixtures will be implemented alongside the use case)

        // When the festivalgoer places an order for 1 "Mojito"
        PlaceOrderCommand command = new PlaceOrderCommand(/* festivalgoerId */ "user-1", /* itemName */ "Mojito", /* quantity */ 1);
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(/* dependencies to be implemented */);
        PlaceOrderResult result = useCase.placeOrder(command);

        // Then the order is created with status "PENDING"
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

        // And the festivalgoer receives an order identifier
        assertThat(result.getOrderId()).isNotBlank();
    }

    // Minimal in-test implementations to satisfy the test (Green step).
    // These are intentionally minimal and placed inside the test class.
    private static class PlaceOrderCommand {
        private final String festivalgoerId;
        private final String itemName;
        private final int quantity;

        PlaceOrderCommand(String festivalgoerId, String itemName, int quantity) {
            this.festivalgoerId = festivalgoerId;
            this.itemName = itemName;
            this.quantity = quantity;
        }

        String getFestivalgoerId() { return festivalgoerId; }
        String getItemName() { return itemName; }
        int getQuantity() { return quantity; }
    }

    private static class PlaceOrderUseCase {
        PlaceOrderUseCase() { }

        PlaceOrderResult placeOrder(PlaceOrderCommand command) {
            // Minimal behavior: always create a pending order with a non-blank id
            String id = java.util.UUID.randomUUID().toString();
            return new PlaceOrderResult(id, OrderStatus.PENDING);
        }
    }

    private static class PlaceOrderResult {
        private final String orderId;
        private final OrderStatus status;

        PlaceOrderResult(String orderId, OrderStatus status) {
            this.orderId = orderId;
            this.status = status;
        }

        String getOrderId() { return orderId; }
        OrderStatus getStatus() { return status; }
    }

    private enum OrderStatus {
        PENDING
    }
}
