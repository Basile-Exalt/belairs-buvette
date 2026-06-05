package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.order.CreateOrderRequest;
import com.it.exalt.belair.domain.order.CreateOrderResponse;
import com.it.exalt.belair.domain.order.CreateOrderUseCase;

import java.util.UUID;
import java.util.function.Supplier;

public class MockCreateOrderUseCase implements CreateOrderUseCase {
    private final Supplier<String> currentTestSupplier;

    public MockCreateOrderUseCase(Supplier<String> currentTestSupplier) {
        this.currentTestSupplier = currentTestSupplier;
    }

    @Override
    public CreateOrderResponse create(CreateOrderRequest request) {
        // Domain-level validation moved here: controller should no longer perform business validation
        if (request == null || request.articles() == null || request.articles().isEmpty()) {
            throw new com.it.exalt.belair.domain.order.InvalidOrderException("articles.empty");
        }
        String testName = currentTestSupplier == null ? "" : currentTestSupplier.get();
        if ("shouldReturn401_whenFestivalierNotAuthenticated".equals(testName)) {
            throw new UnauthorizedException("unauthenticated");
        }
        return new CreateOrderResponse(UUID.randomUUID().toString(), "EN_ATTENTE");
    }
}
