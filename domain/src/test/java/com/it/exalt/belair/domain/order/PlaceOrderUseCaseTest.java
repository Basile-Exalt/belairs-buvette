package com.it.exalt.belair.domain.order;

import com.it.exalt.belair.domain.testutil.InMemoryStock;
import com.it.exalt.belair.domain.testutil.InMemoryCatalog;
import com.it.exalt.belair.domain.testutil.InMemoryStockAdapter;

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
        PlaceOrderCommand command = new PlaceOrderCommand(/* festivalgoerId */ "user-1", /* article */ new Article("Mojito", 1));
        // wire test fakes into production use case
        InMemoryStock stock = new InMemoryStock();
        stock.setQuantity("Mojito", 1);
        InMemoryCatalog catalog = new InMemoryCatalog();
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(new InMemoryStockAdapter(), catalog);
        PlaceOrderResult result = useCase.placeOrder(command);

        // Then the order is created with status "PENDING"
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

        // And the festivalgoer receives an order identifier
        assertThat(result.getOrderId()).isNotBlank();
    }

    @Test
    void givenSufficientStock_whenPlacingOrder_thenOrderCreatedAndStockDecremented() {
        // Given an in-memory stock with Mojito = 10
        InMemoryStock stock = new InMemoryStock();
        stock.setQuantity("Mojito", 10);

        // When placing an order for 2 Mojito
        PlaceOrderCommand command = new PlaceOrderCommand("user-1", new Article("Mojito", 2));
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(new InMemoryStockAdapter(), new InMemoryCatalog());
        PlaceOrderResult result = useCase.placeOrder(command);

        // Then the order is created with status PENDING
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

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
        PlaceOrderCommand command = new PlaceOrderCommand("user-1", new Article("Mojito", 2));
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(new InMemoryStockAdapter(), new InMemoryCatalog());

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
        PlaceOrderCommand command = new PlaceOrderCommand("user-1", new Article("Champagne", 1));
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(new InMemoryStockAdapter(), catalog);

        // Then the order is refused with ARTICLE_INCONNU
        assertThatThrownBy(() -> useCase.placeOrder(command))
            .isInstanceOf(ArticleInconnuException.class)
            .hasMessageContaining("ARTICLE_INCONNU");
    }
}
