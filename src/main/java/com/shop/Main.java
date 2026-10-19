package com.shop;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            int id = OrderService.placeOrder(101, 104, 2, new java.math.BigDecimal("49.00"));
            System.out.println("成功，订单号：" + id);
        } catch (Exception e) {
            System.out.println("失败了：" + e.getMessage());
        }
    }
}
