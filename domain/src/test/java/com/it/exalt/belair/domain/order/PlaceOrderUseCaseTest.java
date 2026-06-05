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
        PlaceOrderUseCase useCase = new PlaceOrderUseCase();
        PlaceOrderResult result = useCase.placeOrder(command);

        // Then the order is created with status "PENDING"
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

        // And the festivalgoer receives an order identifier
        assertThat(result.getOrderId()).isNotBlank();
    }

}
