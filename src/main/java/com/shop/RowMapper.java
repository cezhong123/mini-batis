package com.shop;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

//映射方法，将结果集映射为对象
//用泛型的话就可以兼容不同类型的对象
public class RowMapper {
    public static <T> T mapRow(ResultSet rs, Class<T> clazz) throws Exception {
        ResultSetMetaData rm = rs.getMetaData();
        T obj = clazz.getDeclaredConstructor().newInstance();
        for (int i = 1; i <= rm.getColumnCount(); i++) {
            Field f = findField(clazz, toCamel(rm.getColumnName(i)));
            if (f == null) f = findField(clazz,rm.getColumnName(i));
            if (f != null){
                f.setAccessible(true);
                f.set(obj, rs.getObject(i, f.getType()));
            }
        }
        return obj;
    }
    private static String toCamel(String s) {
        StringBuilder sb=new StringBuilder();
        boolean nextUpper =false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_') {
                nextUpper = true;
            } else if(nextUpper){
                    sb.append(Character.toUpperCase(s.charAt(i)));
                    nextUpper = false;
                }
            else {
                    sb.append(s.charAt(i));
                }
        }
        return sb.toString();
    }
    private static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
