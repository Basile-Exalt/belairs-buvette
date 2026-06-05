package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.order.CreateOrderRequest;
import com.it.exalt.belair.domain.order.CreateOrderResponse;
import com.it.exalt.belair.domain.order.CreateOrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping(path = "/commandes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            CreateOrderResponse resp = createOrderUseCase.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (com.it.exalt.belair.domain.order.InvalidOrderException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
