package com.it.exalt.belair.domain.order;

public interface StockRepository {
    int getQuantity(Article article);
    void decrement(Article article, int quantity);
}
