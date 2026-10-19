package com.shop;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    int id;
    int userId;
    BigDecimal totalAmount;
    String status;
    LocalDateTime createdAt;
    List<OrderItem> items;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Order(int id, LocalDateTime createdAt, List<OrderItem> items, String status, BigDecimal totalAmount, int userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.items = items;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userId = userId;
    }

    public Order() {
    }

    public String toString() {
        return "Order[id=" + id + ", status=" + status + ", total=" + totalAmount
                + ", items=" + (items == null ? "null" : items.size() + " 行") + "]";
    }
}
