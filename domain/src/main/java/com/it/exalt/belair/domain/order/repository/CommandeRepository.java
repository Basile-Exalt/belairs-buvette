package com.it.exalt.belair.domain.order.repository;

import com.it.exalt.belair.domain.order.entity.Commande;
import java.util.List;
import java.util.Optional;

public interface CommandeRepository {
    void save(Commande commande);
    Optional<Commande> findById(String id);
    void updateStatus(String id, String status);
    List<Commande> findByFestivalierAndStatus(String festivalierId, String status);
}
