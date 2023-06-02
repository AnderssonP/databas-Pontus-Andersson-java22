package org.example;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public abstract class Model {
    private static MysqlDataSource dataSource;
    static String url;
    static int port;
    static String database;
    static String username;
    static String password;

    public static void initializeDataSource() {
        try {
            System.out.println("Configuring data source...");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/main/java/org/example/db.json"));
            JSONObject db = (JSONObject) obj;

            url = (String) db.get("url");
            port = ((Long) db.get("port")).intValue();
            database = (String) db.get("database");
            username = (String) db.get("username");
            password = (String) db.get("password");

            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database +
                    "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
            System.out.println("Done!");
        } catch (Exception e) {
            System.out.println("Failed!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            System.out.println("Failed!");
            e.printStackTrace();
            return null;
        }
    }
}
