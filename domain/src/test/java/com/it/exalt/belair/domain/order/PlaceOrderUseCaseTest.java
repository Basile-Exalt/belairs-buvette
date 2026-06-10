package com.it.exalt.belair.domain.order;

import com.it.exalt.belair.domain.testutil.InMemoryStock;
import com.it.exalt.belair.domain.testutil.InMemoryCatalog;
import com.it.exalt.belair.domain.testutil.InMemoryStockAdapter;
import com.it.exalt.belair.domain.order.dto.PasserCommandeCommand;
import com.it.exalt.belair.domain.order.dto.PasserCommandeResult;
import com.it.exalt.belair.domain.order.usecase.PasserCommandeUseCase;
import com.it.exalt.belair.domain.order.exception.StockInsuffisantException;
import com.it.exalt.belair.domain.order.exception.ArticleInconnuException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlaceOrderUseCaseTest {

    @Test
    void givenFestivalgoerAndAvailableItem_whenPlacingOrder_thenOrderCreatedWithPendingStatusAndReturnsOrderId() {
        // Given a festivalgoer identified
        // And an item "Mojito" available in stock
        // (These domain fixtures will be implemented alongside the use case)

        // When the festivalgoer places an order for 1 "Mojito"
        PasserCommandeCommand command = new PasserCommandeCommand(/* article */ new Article("Mojito", 1));
        // wire test fakes into production use case
        InMemoryStock stock = new InMemoryStock();
        stock.setQuantity("Mojito", 1);
        InMemoryCatalog catalog = new InMemoryCatalog();
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(new InMemoryStockAdapter(), catalog);
        PasserCommandeResult result = useCase.placeOrder(command);

        // Then the order is created with status "PENDING"
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);

        // And the festivalgoer receives an order identifier
        assertThat(result.id()).isNotBlank();
    }

    @Test
    void givenSufficientStock_whenPlacingOrder_thenOrderCreatedAndStockDecremented() {
        // Given an in-memory stock with Mojito = 10
        InMemoryStock stock = new InMemoryStock();
        stock.setQuantity("Mojito", 10);

        // When placing an order for 2 Mojito
        PasserCommandeCommand command = new PasserCommandeCommand(new Article("Mojito", 2));
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(new InMemoryStockAdapter(), new InMemoryCatalog());
        PasserCommandeResult result = useCase.placeOrder(command);

        // Then the order is created with status PENDING
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);

        // And the stock of Mojito is decremented by 2
        // (The current implementation does not interact with stock, so this assertion is expected to fail)
        assertThat(stock.getQuantity("Mojito")).isEqualTo(8);
    }

    @Test
    void givenInsufficientStock_whenPlacingOrder_thenOrderIsRefusedAndStockUnchanged() {
        // Given an in-memory stock with Mojito = 1
        InMemoryStock stock = new InMemoryStock();
        stock.setQuantity("Mojito", 1);

        // When placing an order for 2 Mojito
        PasserCommandeCommand command = new PasserCommandeCommand(new Article("Mojito", 2));
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(new InMemoryStockAdapter(), new InMemoryCatalog());

        // Then the order is refused with STOCK_INSUFFISANT and stock remains unchanged
        assertThatThrownBy(() -> useCase.placeOrder(command))
            .isInstanceOf(StockInsuffisantException.class)
            .hasMessageContaining("STOCK_INSUFFISANT");

        assertThat(stock.getQuantity("Mojito")).isEqualTo(1);
    }

    @Test
    void givenEmptyCatalog_whenPlacingOrder_thenOrderIsRefusedWithArticleUnknown() {
        // Given an empty catalog (no items)
        InMemoryCatalog catalog = new InMemoryCatalog();

        // When placing an order for 1 Champagne
        PasserCommandeCommand command = new PasserCommandeCommand(new Article("Champagne", 1));
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(new InMemoryStockAdapter(), catalog);

        // Then the order is refused with ARTICLE_INCONNU
        assertThatThrownBy(() -> useCase.placeOrder(command))
            .isInstanceOf(ArticleInconnuException.class)
            .hasMessageContaining("ARTICLE_INCONNU");
    }
}
