package com.shop;

import java.time.LocalDateTime;
//定义user类
//写普通类而不是record
//如果写record的话，之后通过反射获取对象时会遇到没有无参构造可调的问题
public class User {
    int id;
    String name, phone;
    LocalDateTime createdAt;

    public User() {
    }

    public User(int id, String name, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User[" + id +" "+ name +" "+ phone +" "+ createdAt + "]";
    }
}
