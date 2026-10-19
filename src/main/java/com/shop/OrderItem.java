package com.shop;

import java.math.BigDecimal;

public class OrderItem {
    int id;
    int orderId;
    int productId;
    int quantity;
    BigDecimal unitPrice;

    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String toString() {
        return "order_items[" + id +" "+ orderId +" "+ productId +" "+ quantity +" "+ unitPrice + "]";
    }
}
