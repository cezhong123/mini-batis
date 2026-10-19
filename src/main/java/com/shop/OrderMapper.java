package com.shop;

import java.util.List;

public interface OrderMapper {
        // 只查订单头。items 会是 null —— 明细要另外查
        @Select("SELECT * FROM orders WHERE id = #{id}")
        Order findById(int id);

        // 查这张订单的明细
        @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
        List<OrderItem> findItems(int orderId);

        // 写订单头
        @Insert("INSERT INTO orders (user_id, total_amount, status) VALUES (#{userId}, #{totalAmount}, #{status})")
        int insert(Order order);
}
