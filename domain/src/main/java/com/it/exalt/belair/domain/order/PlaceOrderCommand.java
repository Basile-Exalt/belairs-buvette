package com.it.exalt.belair.domain.order;

public record PlaceOrderCommand(String festivalgoerId, String itemName, int quantity) { }
