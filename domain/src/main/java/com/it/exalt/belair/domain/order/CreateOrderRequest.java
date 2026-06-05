package com.it.exalt.belair.domain.order;

import java.util.List;

public record CreateOrderRequest(String festivalgoerId, List<Article> articles) {
    public static record Article(String id, int quantity) { }
}
