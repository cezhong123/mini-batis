package com.shop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderService {
    // 订单服务的业务逻辑
    public static int placeOrder(int userId, int productId, int quantity, BigDecimal unitPrice) throws Exception {
        Connection conn =null;
        try {
            conn =Database.openDefault();
            conn.setAutoCommit(false);

            BigDecimal total =unitPrice.multiply(BigDecimal.valueOf(quantity));
            int orderId = insertOrder(conn, userId, total);
            insertItem(conn, orderId, productId, quantity, unitPrice);
            conn.commit();
            return orderId;
        } catch (Exception e) {
            if (conn!=null) {
                try {
                    conn.rollback();
                } catch (Exception ignore) {}
            }
            throw e;
        }finally {
            if(conn!=null){
                try{conn.close();} catch(Exception ignore) {}
            }
        }
    }
    private static int insertOrder(Connection conn, int userId, BigDecimal total) throws Exception {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, '已支付')";
        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, userId);
            ps.setBigDecimal(2, total);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new IllegalStateException("Failed to insert order");
    }
    private static void insertItem(Connection conn, int orderId, int productId, int quantity, BigDecimal unitPrice) throws Exception {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, unitPrice);
            ps.executeUpdate();
        }
    }
}
