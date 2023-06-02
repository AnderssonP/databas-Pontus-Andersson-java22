package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Model.getConnection;


public class tables {
    public static void makeTable() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        String users = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, firstname VARCHAR(25), lastname VARCHAR(50), email VARCHAR(100), UNIQUE (email), password VARCHAR(300), address VARCHAR(200), age INT, civicnumber VARCHAR(50), UNIQUE (civicnumber), created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, isOnline BOOLEAN);";
        String accounts = "CREATE TABLE IF NOT EXISTS accounts(id INT PRIMARY KEY AUTO_INCREMENT, amount INT, user_id INT, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, account_number BIGINT, UNIQUE(account_number), name_of_account VARCHAR(50));";
        String transactions = "CREATE TABLE IF NOT EXISTS transaction(id INT PRIMARY KEY AUTO_INCREMENT, from_account_id INT, to_account_number BIGINT, amount INT, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        String friends = "CREATE TABLE IF NOT EXISTS friends (id INT PRIMARY KEY AUTO_INCREMENT, UserID INT, Foreign Key(UserID) References Users(ID) ON DELETE CASCADE, FriendUserID INT, Foreign Key(FriendUserID) References Users(ID) ON DELETE CASCADE, name VARCHAR(50))";

        int result = statement.executeUpdate(users);
        int result2 = statement.executeUpdate(accounts);
        int result3 = statement.executeUpdate(transactions);
        int result4 = statement.executeUpdate(friends);
        //System.out.println("Result: " + result + result2 + result3);
        connection.close();
    }
}
