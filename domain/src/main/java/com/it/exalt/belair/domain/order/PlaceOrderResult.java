package com.it.exalt.belair.domain.order;

public final class PlaceOrderResult {
    private final String orderId;
    private final OrderStatus status;

    public PlaceOrderResult(String orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
}
