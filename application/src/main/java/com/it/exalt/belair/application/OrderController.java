package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.order.exception.ArticleInconnuException;
import com.it.exalt.belair.domain.order.exception.CommandeNonTrouveeException;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeCommand;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeResult;
import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;
import com.it.exalt.belair.domain.order.usecase.AccuserReceptionCommandeUseCase;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import com.it.exalt.belair.domain.order.exception.StockInsuffisantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final CreerCommandeUseCase createOrderUseCase;
    private final AccuserReceptionCommandeUseCase acknowledgeOrderUseCase;

    public OrderController(CreerCommandeUseCase createOrderUseCase,
                           AccuserReceptionCommandeUseCase acknowledgeOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.acknowledgeOrderUseCase = acknowledgeOrderUseCase;
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

    @PostMapping(path = "/commandes/{id}/accuser-reception", produces = "application/json")
    public ResponseEntity<AccuserReceptionCommandeResult> acknowledgeOrder(@PathVariable("id") String id) {
        try {
            AccuserReceptionCommandeResult result = acknowledgeOrderUseCase.acknowledge(
                    new AccuserReceptionCommandeCommand(id));
            return ResponseEntity.ok(result);
        } catch (CommandeNonTrouveeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
