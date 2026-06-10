package com.it.exalt.belair.domain.order.dto;

import com.it.exalt.belair.domain.order.OrderStatus;

public record PasserCommandeResult(String id, OrderStatus status) { }
