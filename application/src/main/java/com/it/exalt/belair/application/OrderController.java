package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.order.exception.ArticleInconnuException;
import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import com.it.exalt.belair.domain.order.exception.StockInsuffisantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final CreerCommandeUseCase createOrderUseCase;

    public OrderController(CreerCommandeUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping(path = "/commandes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreerCommandeResponse> createOrder(@RequestBody CreerCommandeRequest request) {
        try {
            CreerCommandeResponse resp = createOrderUseCase.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (ArticleInconnuException | StockInsuffisantException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
