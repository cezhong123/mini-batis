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
            Field f = clazz.getDeclaredField(rm.getColumnName(i));
            f.setAccessible(true);
            f.set(obj, rs.getObject(i, f.getType()));
        }
        return obj;
    }
}
