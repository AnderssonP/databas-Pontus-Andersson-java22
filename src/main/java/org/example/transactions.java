package org.example;

import java.sql.*;
import java.time.LocalDate;

import static org.example.Model.getConnection;

public class transactions {
    protected int id;
    protected int fromUserId;
    protected int toUserID;
    protected String currency;
    protected int amount;
    protected LocalDate date;

    public static void sendMoney(int fromAccount, int sendAmount, String toAccount) {
        Connection connection = getConnection();
        try {
            String querySelect = "SELECT amount FROM accounts WHERE id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(querySelect);
            selectStmt.setInt(1, fromAccount);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int balance = resultSet.getInt("amount");
                if (balance >= sendAmount) {
                    String queryMinus = "UPDATE accounts SET amount = amount - ? WHERE id = ?";
                    String queryPlus = "UPDATE accounts SET amount = amount + ? WHERE account_number = ?";

                    connection.setAutoCommit(false);

                    PreparedStatement updateFromStmt = connection.prepareStatement(queryMinus);
                    updateFromStmt.setInt(1, sendAmount);
                    updateFromStmt.setInt(2, fromAccount);
                    updateFromStmt.executeUpdate();

                    PreparedStatement updateToStmt = connection.prepareStatement(queryPlus);
                    updateToStmt.setInt(1, sendAmount);
                    updateToStmt.setString(2, toAccount);
                    updateToStmt.executeUpdate();

                    String insertQuery = "INSERT INTO transaction (from_account_id, to_account_number, amount) VALUES (?, ?, ?)";
                    PreparedStatement transactionStmt = connection.prepareStatement(insertQuery);
                    transactionStmt.setInt(1, fromAccount);
                    transactionStmt.setString(2, toAccount);
                    transactionStmt.setInt(3, sendAmount);
                    transactionStmt.executeUpdate();

                    System.out.println("Överföring genomförd!");

                    connection.commit();
                    connection.close();
                } else {
                    System.out.println("Otillräckligt saldo på kontot.");
                    connection.close();
                }
            } else {
                System.out.println("Kontot hittades inte.");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static void showTransactionsSent(String civic, Date startDate, Date endDate) {
        Connection connection = getConnection();
        String query = "SELECT amount, date FROM transaction WHERE from_account_id IN (SELECT id FROM accounts WHERE user_id IN(SELECT id FROM users WHERE civicnumber = ?)) AND date BETWEEN ? AND ? ORDER BY date";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, civic);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                Timestamp date = resultSet.getTimestamp("date");
                System.out.println("Amount: " + amount + ", Date: " + date);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void showTransactionsReceived(String civic, Date startDate, Date endDate) {
        Connection connection = getConnection();
        String query = "SELECT amount, date FROM transaction WHERE to_account_number IN (SELECT account_number FROM accounts WHERE user_id IN (SELECT id FROM users WHERE civicnumber = ?)) AND date BETWEEN ? AND ? ORDER BY date";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, civic);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int amount = resultSet.getInt("amount");
                Timestamp date = resultSet.getTimestamp("date");
                System.out.println("Amount: " + amount + ", Date: " + date);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
