package com.it.exalt.belair.domain.order.usecase;

import com.it.exalt.belair.domain.order.dto.PasserCommandeCommand;
import com.it.exalt.belair.domain.order.dto.PasserCommandeResult;
import com.it.exalt.belair.domain.order.exception.ArticleInconnuException;
import com.it.exalt.belair.domain.order.exception.StockInsuffisantException;
import com.it.exalt.belair.domain.order.OrderStatus;
import com.it.exalt.belair.domain.order.Article;
import com.it.exalt.belair.domain.order.StockRepository;
import com.it.exalt.belair.domain.order.repository.CatalogueRepository;

public class PasserCommandeUseCase {

    private final StockRepository stockRepository;
    private final CatalogueRepository catalogueRepository;

    public PasserCommandeUseCase(StockRepository stockRepository,
                                 CatalogueRepository catalogueRepository) {
        this.stockRepository = stockRepository;
        this.catalogueRepository = catalogueRepository;
    }

    public PasserCommandeResult placeOrder(PasserCommandeCommand command) {
        Article article = command.article();
        String item = article.id();
        int qty = article.quantity();

        if (!catalogueRepository.exists(item)) {
            throw new ArticleInconnuException("ARTICLE_INCONNU");
        }

        int available = stockRepository.getQuantity(article);
        if (available < qty) {
            throw new StockInsuffisantException("STOCK_INSUFFISANT");
        }

        stockRepository.decrement(article, qty);

        String id = java.util.UUID.randomUUID().toString();
        return new PasserCommandeResult(id, OrderStatus.PENDING);
    }
}
