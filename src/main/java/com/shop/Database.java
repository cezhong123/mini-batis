package com.shop;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private final String url;
    private final Properties props;
    private static final Database DEFAULT = fromFile("db.properties");

    private Database(String url, Properties props) {
        this.url = url;
        this.props = props;
    }


    public Connection open() throws SQLException {
        return DriverManager.getConnection(url, props);
    }

    public static Database fromFile(String resourcePath)  {
        Properties props = new Properties();
        try (InputStream in = Database.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null)
                throw new IllegalArgumentException("类路径找不到资源: " + resourcePath);
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        catch (java.io.IOException e) {
            throw new IllegalStateException("读不了 " + resourcePath, e);
        }
        return fromProperties(props, resourcePath);
    }

    private static Database fromProperties(Properties props, String source) {
        String url = props.getProperty("url", "").trim();
        if (url.isEmpty()) {
            throw new IllegalArgumentException("数据库URL为空: " + source);
        }
        if (!url.startsWith("jdbc:")) {
            throw new IllegalStateException(source + " 里的 url 不是 JDBC 连接串：" + url);
        }
        Properties p=new Properties();
        String user = props.getProperty("username","").trim();
        String password = props.getProperty("password","");
        p.setProperty("user", user);
        p.setProperty("password", password);
        p.setProperty("useUnicode", "true");
        p.setProperty("characterEncoding", "UTF-8");
        return new Database(url, p);
    }
    public static Database fromUrl(String url, String user, String password) {
        Properties raw = new Properties();
        raw.setProperty("url", url);
        raw.setProperty("username", user);
        raw.setProperty("password", password);
        return fromProperties(raw, "(直接给参数)");
    }
    public static Connection openDefault() throws SQLException {
        return DEFAULT.open();
    }
}