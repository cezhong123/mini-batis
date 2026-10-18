package com.shop;

import java.time.LocalDateTime;
public class User {
    int id;
    String name, phone;
    LocalDateTime created_at;

    public User() {
    }

    public User(int id, String name, String phone, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User[" + id +" "+ name +" "+ phone +" "+ created_at + "]";
    }
}
