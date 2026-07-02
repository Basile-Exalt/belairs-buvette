package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.order.entity.Commande;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCommandeRepository implements CommandeRepository {

    private final Map<String, Commande> store = new HashMap<>();

    @Override
    public void save(Commande commande) {
        store.put(commande.id(), commande);
    }

    @Override
    public Optional<Commande> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void updateStatus(String id, String status) {
        Commande existing = store.get(id);
        if (existing != null) {
            existing.setStatus(status);
        }
    }

    @Override
    public List<Commande> findByFestivalierAndStatus(String festivalierId, String status) {
        List<Commande> result = new ArrayList<>();
        for (Commande c : store.values()) {
            if (festivalierId.equals(c.festivalierId()) && status.equals(c.status())) {
                result.add(c);
            }
        }
        return result;
    }
}
