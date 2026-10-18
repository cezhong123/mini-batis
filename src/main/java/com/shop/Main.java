package com.shop;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection c = Database.fromFile("db.properties").open()) {
            System.out.println("连上了：" + c.getMetaData().getURL());
        }
    }
}
