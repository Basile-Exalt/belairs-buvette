package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.entity.Commande;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OrderRepositoryImpl implements CommandeRepository {

    private final JpaOrderRepository crud;

    @Autowired
    public OrderRepositoryImpl(JpaOrderRepository crud) {
        this.crud = crud;
    }

    @Override
    public void save(Commande commande) {
        OrderEntity entity = toEntity(commande);
        crud.save(entity);
    }

    @Override
    public Optional<Commande> findById(String id) {
        return crud.findById(id).map(this::toDomain);
    }

    @Override
    public void updateStatus(String id, String status) {
        crud.findById(id).ifPresent(e -> {
            e.setStatus(status);
            crud.save(e);
        });
    }

    @Override
    public List<Commande> findByFestivalierAndStatus(String festivalierId, String status) {
        return crud.findByFestivalierIdAndStatus(festivalierId, status).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    // Convert domain Commande -> entity
    private OrderEntity toEntity(Commande o) {
        OrderEntity e = new OrderEntity(o.id(), o.festivalierId(), o.status());
        for (var l : o.lines()) {
            OrderLineEntity le = new OrderLineEntity(l.article(), l.quantity());
            e.addLine(le);
        }
        return e;
    }

    // Convert entity -> domain Commande
    private Commande toDomain(OrderEntity e) {
        Commande o = new Commande(e.getId(), e.getFestivalierId(), e.getStatus());
        for (OrderLineEntity le : e.getLines()) {
            o.addLine(le.getArticle(), le.getQuantity());
        }
        return o;
    }
}
