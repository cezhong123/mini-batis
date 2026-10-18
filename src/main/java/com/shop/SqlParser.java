package com.shop;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser {
    public static String parserSql(String tableName, List<String> params) throws Exception {
        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(tableName);
        while (matcher.find()){
            params.add(matcher.group(1));
        }
        return pattern.matcher(tableName).replaceAll("?");
    }
}
