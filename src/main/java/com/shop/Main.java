package com.shop;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        UserMapper m = ProxyUser.getMapper(UserMapper.class);

        User u = new User(0, "测试乙", "13900000002", null);
        int n = m.insertUser(u);
        System.out.println("影响行数: " + n);
        System.out.println("新 id: " + u.id);
        System.out.println("总人数: " + m.findAll().size());
    }
}
