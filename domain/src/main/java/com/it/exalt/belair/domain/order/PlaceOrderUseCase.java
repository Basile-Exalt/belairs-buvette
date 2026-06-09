package com.it.exalt.belair.domain.order;

public class PlaceOrderUseCase {

    private final StockRepository stockRepository;
    private final CatalogRepository catalogRepository;

    public PlaceOrderUseCase(StockRepository stockRepository, CatalogRepository catalogRepository) {
        this.stockRepository = stockRepository;
        this.catalogRepository = catalogRepository;
    }

    public PlaceOrderResult placeOrder(PlaceOrderCommand command) {
        Article article = command.article();
        String item = article.id();
        int qty = article.quantity();

        if (!catalogRepository.exists(item)) {
            throw new ArticleInconnuException("ARTICLE_INCONNU");
        }

        int available = stockRepository.getQuantity(article);
        if (available < qty) {
            throw new StockInsuffisantException("STOCK_INSUFFISANT");
        }

        stockRepository.decrement(article, qty);

        String id = java.util.UUID.randomUUID().toString();
        return new PlaceOrderResult(id, OrderStatus.PENDING);
    }
}
