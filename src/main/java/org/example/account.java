package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.Model.getConnection;
import static org.example.Model.password;

public class account {
    protected int id;
    protected int amount;
    protected long account_number;
    protected String name_of_account;
    protected int user_id;


    public static void createAccount(int amount, String account_number, String name_of_account) {
        Connection connection = getConnection();
        String query = "INSERT INTO accounts(amount, account_number, name_of_account, user_id) VALUES (?, ?, ?, ?)";
        String userID = "SELECT id FROM users WHERE isOnline = true";
        try {
            PreparedStatement userIdStatement = connection.prepareStatement(userID);
            ResultSet userIdResultSet = userIdStatement.executeQuery();
            if (userIdResultSet.next()) {
                int userId = userIdResultSet.getInt("id");
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, amount);
                statement.setString(2, account_number);
                statement.setString(3, name_of_account);
                statement.setInt(4, userId);
                statement.executeUpdate();
                connection.close();
            } else {
                System.out.println("Failed to find user id.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to create account.");
            e.printStackTrace();
        }
    }


    public static void deleteAccount(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "DELETE FROM accounts WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,id);

            int resultset = statement.executeUpdate();
            connection.close();
        }catch (SQLException e){

        }
    }

    public void getAccounts(){
        Connection connection = getConnection();
        String query = "SELECT id, name_of_account, amount FROM accounts WHERE user_id = ?";
        String userIDQuery = "SELECT id FROM users WHERE isOnline = true";
        try {
            PreparedStatement userIDStatement = connection.prepareStatement(userIDQuery);
            ResultSet userIDResultset = userIDStatement.executeQuery();
            if (userIDResultset.next()){
                int userID = userIDResultset.getInt("id");
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                ResultSet resultset = statement.executeQuery();
                while (resultset.next()){
                    int accountID = resultset.getInt("id");
                    String accountName = resultset.getString("name_of_account");
                    int amount = resultset.getInt("amount");
                    setId(accountID);
                    setName_of_account(accountName);
                    setAmount(amount);
                    System.out.println(accountID + ": " + accountName);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getFriendsAccount(int id) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT account_number,amount FROM accounts WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String accountNumber = rs.getString("account_number");
                int amount = rs.getInt("amount");
                System.out.println(accountNumber + ": " + amount + " kr");
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getAccount_number() {
        return account_number;
    }

    public void setAccount_number(long account_number) {
        this.account_number = account_number;
    }

    public String getName_of_account() {
        return name_of_account;
    }

    public void setName_of_account(String name_of_account) {
        this.name_of_account = name_of_account;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
