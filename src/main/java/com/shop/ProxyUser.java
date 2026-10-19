package com.shop;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.shop.RowMapper.mapRow;
import static com.shop.SqlParser.sqlParser;

// 代理类
// 传入接口类，返回接口类的代理对象
//在invoke里根据方法上的注解执行对应操作
//invoke里的对象用的是代理类对象本身，因为不需要对象去调用方法，这个代理只是执行了sql语句
//所以这个代理本质还是抽取dao里面重复的部分
public class ProxyUser {
    @SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> type) {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[]{ type },
                (proxy, method, args) -> {

                    try (Connection conn = Database.openDefault()) {
                    {
                        //是select注解就执行查询的操作,否则执行插入操作
                        if (method.isAnnotationPresent(Select.class)) {
                            try (PreparedStatement pStmt = conn.prepareStatement(sqlParser(method.getAnnotation(Select.class).value(),new ArrayList<>()))) {
                                if (args != null) {
                                    for (int i = 0; i < args.length; i++) {
                                        pStmt.setObject(i + 1, args[i]);
                                    }
                                }
                                try (ResultSet res = pStmt.executeQuery()) {
                                    Type returnType = method.getGenericReturnType();
                                    if (returnType instanceof ParameterizedType) {
                                        ParameterizedType parameterizedType = (ParameterizedType) returnType;
                                        if (method.getReturnType() == List.class) {
                                            List<Object> list = new ArrayList<>();
                                            while (res.next()) {
                                                list.add(mapRow(res, (Class<T>) parameterizedType.getActualTypeArguments()[0]));
                                            }
                                            return list;
                                    }
                                    } else if (returnType instanceof Class) {
                                        if (res.next()) return mapRow(res, (Class<T>) returnType);
                                    }
                                }
                            }
                        } else{
                            List<String> name =new ArrayList<>();
                            try (PreparedStatement pStmt = conn.prepareStatement(sqlParser(method.getAnnotation(Insert.class).value(),name), Statement.RETURN_GENERATED_KEYS)) {
                                if (args != null) {
                                    Object user = args[0];
                                    Class<?> clazz = user.getClass();
                                    for (int i = 0; i < name.size(); i++) {
                                        for (int j = 0; j < clazz.getDeclaredFields().length; j++) {
                                            if (clazz.getDeclaredFields()[j].getName().equals(name.get(i)))
                                                pStmt.setObject(i + 1, clazz.getDeclaredFields()[j].get(user));
                                        }
                                    }
                                    int rows = pStmt.executeUpdate();
                                    try (ResultSet keys = pStmt.getGeneratedKeys()) {
                                        if (keys.next()) {
                                            clazz.getDeclaredField("id").set(user, keys.getInt(1));
                                        }
                                    }
                                    return rows;
                                }
                            }
                        }
                        return null;
                    }
                }
            });
        }
}
