package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.order.Article;
import com.it.exalt.belair.domain.order.StockRepository;

public class InMemoryStockAdapter implements StockRepository {
    @Override
    public int getQuantity(Article article) { return InMemoryStock.getQuantityStatic(article.id()); }

    @Override
    public void decrement(Article article, int quantity) { InMemoryStock.decrement(article.id(), quantity); }
}
