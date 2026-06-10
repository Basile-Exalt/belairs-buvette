package com.it.exalt.belair.infrastructure.order;

import jakarta.persistence.*;

@Entity
@Table(name = "order_lines")
public class OrderLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "article", nullable = false)
    private String article;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public OrderLineEntity() { }

    public OrderLineEntity(String article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getArticle() { return article; }
    public int getQuantity() { return quantity; }
    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }
}
