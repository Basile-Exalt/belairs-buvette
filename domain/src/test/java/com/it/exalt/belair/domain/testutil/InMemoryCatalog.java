package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.order.repository.CatalogueRepository;

public class InMemoryCatalog implements CatalogueRepository {
    // existence is derived from whether stock is registered for the item
    // (keeps test-local setup simple: setting stock implies the item exists)
    @Override
    public boolean exists(String item) { return InMemoryStock.getQuantityStatic(item) > 0; }

    public static boolean existsStatic(String item) { return InMemoryStock.getQuantityStatic(item) > 0; }
}
