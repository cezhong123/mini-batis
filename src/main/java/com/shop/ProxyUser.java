package com.shop;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.shop.RowMapper.mapRow;
import static com.shop.SqlParser.parserSql;

public class ProxyUser {
    @SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> type) {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[]{ type },
                (proxy, method, args) -> {

                    try (Connection conn = Database.openDefault()) {
                    {
                        if (method.isAnnotationPresent(Select.class)) {
                            try (PreparedStatement pStmt = conn.prepareStatement(parserSql(method.getAnnotation(Select.class).value(),new ArrayList<>()))) {
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
                            try (PreparedStatement pStmt = conn.prepareStatement(parserSql(method.getAnnotation(Insert.class).value(),name))) {
                                if (args != null) {
                                    Object user = args[0];
                                    Class<?> clazz =user.getClass();
                                    for (int i = 0; i < name.size(); i++) {
                                        for (int j = 0; j <clazz.getDeclaredFields().length; j++) {
                                            if (clazz.getDeclaredFields()[j].getName().equals(name.get(i)))
                                                pStmt.setObject(i+1,clazz.getDeclaredFields()[j].get(user));
                                        }
                                    }
                                    return pStmt.executeUpdate();
                                }
                            }
                        }
                        return null;
                    }
                }
            });
        }
}
