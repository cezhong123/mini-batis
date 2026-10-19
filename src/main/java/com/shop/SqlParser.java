package com.shop;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//SQL解析类,把占位符换成jdbc的？
//这里还要记录下每个占位符的名字，之后要匹配对象里的字段
public class SqlParser {
    public static String sqlParser(String sql, List<String> params) throws Exception {
        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()){
            params.add(matcher.group(1));
        }
        return pattern.matcher(sql).replaceAll("?");
    }
}
