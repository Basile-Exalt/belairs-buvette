package com.it.exalt.belair.infrastructure.order;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String id;

    @Column(name = "festivalier_id", nullable = false)
    private String festivalierId;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineEntity> lines = new ArrayList<>();

    public OrderEntity() { }

    public OrderEntity(String id, String festivalierId, String status) {
        this.id = id;
        this.festivalierId = festivalierId;
        this.status = status;
    }

    public String getId() { return id; }
    public String getFestivalierId() { return festivalierId; }
    public String getStatus() { return status; }
    public List<OrderLineEntity> getLines() { return lines; }

    public void setStatus(String status) { this.status = status; }

    public void addLine(OrderLineEntity line) {
        lines.add(line);
        line.setOrder(this);
    }

    public void clearLines() {
        lines.clear();
    }
}
