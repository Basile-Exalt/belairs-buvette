package com.it.exalt.belair.domain.order.entity;

import java.util.ArrayList;
import java.util.List;

public final class Commande {
    private final String id;
    private final String festivalierId;
    private String status;
    private final List<Line> lines = new ArrayList<>();

    public Commande(String id, String festivalierId, String status) {
        this.id = id;
        this.festivalierId = festivalierId;
        this.status = status;
    }

    public String id() { return id; }
    public String festivalierId() { return festivalierId; }
    public String status() { return status; }
    public List<Line> lines() { return List.copyOf(lines); }

    public void addLine(String article, int qty) { lines.add(new Line(article, qty)); }

    public void setStatus(String status) { this.status = status; }

    public static final class Line {
        private final String article;
        private final int quantity;

        public Line(String article, int quantity) {
            this.article = article;
            this.quantity = quantity;
        }

        public String article() { return article; }
        public int quantity() { return quantity; }
    }
}
