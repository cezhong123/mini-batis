package com.shop;

import java.util.List;

//一个接口，在代理时就传这个接口，
//根据接口里的每个方法上的注解，代理就能拿到sql语句，然后执行
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(int id);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Insert("INSERT INTO users (name,phone) VALUES (#{name}, #{phone})")
    int insertUser(User user);
}
