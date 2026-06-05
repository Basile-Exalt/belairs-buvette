package com.it.exalt.belair.domain.order;

public class PlaceOrderUseCase {

    public PlaceOrderUseCase() { }

    public PlaceOrderResult placeOrder(PlaceOrderCommand command) {
        String id = java.util.UUID.randomUUID().toString();
        return new PlaceOrderResult(id, OrderStatus.PENDING);
    }
}
