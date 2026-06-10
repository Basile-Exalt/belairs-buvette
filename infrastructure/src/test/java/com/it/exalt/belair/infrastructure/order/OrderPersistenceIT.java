package com.it.exalt.belair.infrastructure.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import com.it.exalt.belair.domain.order.entity.Commande;

@DataJpaTest
@Import(OrderRepositoryImpl.class)
class OrderPersistenceIT {

    @Autowired
    private OrderRepositoryImpl repository;

    @Test
    void shouldSaveAndRetrieveOrderById() {
        // Given a new order with status "EN_ATTENTE" and two lines
        Commande order = new Commande("order-1", "festivalier-42", "EN_ATTENTE");
        order.addLine("Mojito", 2);
        order.addLine("Eau plate", 1);

        // When saving the order
        repository.save(order);

        // Then the order can be retrieved by id and has expected status and lines
        Optional<Commande> loaded = repository.findById("order-1");
        assertTrue(loaded.isPresent());
        assertEquals("EN_ATTENTE", loaded.get().status());
        assertEquals(2, loaded.get().lines().size());
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Given a saved order with status "EN_ATTENTE"
        Commande order = new Commande("order-2", "festivalier-42", "EN_ATTENTE");
        repository.save(order);

        // When updating its status to "PRÊTE"
        repository.updateStatus("order-2", "PRÊTE");

        // Then retrieving by id returns status "PRÊTE"
        Optional<Commande> loaded = repository.findById("order-2");
        assertTrue(loaded.isPresent());
        assertEquals("PRÊTE", loaded.get().status());
    }

    @Test
    void shouldFindPendingOrdersForFestivalier() {
        // Given 3 orders for festivalier-42 with statuses EN_ATTENTE, EN_ATTENTE, PRÊTE
        Commande o1 = new Commande("o1", "festivalier-42", "EN_ATTENTE");
        Commande o2 = new Commande("o2", "festivalier-42", "EN_ATTENTE");
        Commande o3 = new Commande("o3", "festivalier-42", "PRÊTE");
        repository.save(o1);
        repository.save(o2);
        repository.save(o3);

        // When searching for orders with status EN_ATTENTE for festivalier-42
        List<Commande> pending = repository.findByFestivalierAndStatus("festivalier-42", "EN_ATTENTE");

        // Then we get exactly 2 orders
        assertEquals(2, pending.size());
    }
}
