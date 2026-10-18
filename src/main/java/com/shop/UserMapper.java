package com.shop;


import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(int id);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Insert("INSERT INTO users (id,name,phone,created_at) VALUES (#{id}, #{name}, #{phone}, #{created_at})")
    int insertUser(User user);
}
