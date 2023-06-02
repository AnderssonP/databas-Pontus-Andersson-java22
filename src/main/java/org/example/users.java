package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.example.Model.getConnection;
import static org.example.Model.password;

public class users {
    protected int id;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected String password;
    protected String address;
    protected int age;
    protected String civicnumber;
    protected LocalDate date;
    protected boolean isOnline;
    private static final int SALT_LENGTH = 16;


    public void createUser(String firstname, String lastname, String email, String password, String address, int age, String civicnumber, boolean isOnline) throws SQLException {
        Connection connection = getConnection();
        String query = "INSERT INTO users(firstname, lastname, email, password, address, age, civicnumber, isOnline) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, email);
            statement.setString(4, Encrypt(password));
            statement.setString(5, address);
            statement.setInt(6,age);
            statement.setString(7,civicnumber);
            statement.setBoolean(8, isOnline);
            int resultSet = statement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteUser(String civicnumber) throws SQLException {
        Connection connection = getConnection();
        String query = "DELETE FROM users WHERE civicnumber = ? ";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,civicnumber);

            int resultset = statement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean login(String civicnumber, String password) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT * FROM users WHERE civicnumber = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, civicnumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String hashedPassword = resultSet.getString("password");
            if (Verify(password, hashedPassword)) {
                String updateQuery = "UPDATE users SET isOnline = true WHERE civicnumber = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, civicnumber);
                updateStatement.executeUpdate();

                return true;
            }
        }
        return false;
    }

    public static boolean logout(String civicnumber) throws SQLException {
        Connection connection = getConnection();
        String online = "UPDATE users SET isOnline = false WHERE civicnumber = ?";
        assert connection != null;
        PreparedStatement updateStatus = connection.prepareStatement(online);
        updateStatus.setString(1,civicnumber);
        int resultSet = updateStatus.executeUpdate();
        return true;
    }

    public static void displayOnlineUsers() throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT firstname FROM users WHERE isOnline = true";
        assert connection != null;
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("firstname");
            System.out.println("Välkommen " + name);
        }
    }

    public static void updateUser(String change, String choose) throws SQLException {
        Connection connection = getConnection();
        String query = "UPDATE users SET "+ change+" = ? WHERE isOnline = true";
        assert connection != null;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,choose);
        int resultSet = statement.executeUpdate();
    }

    public static void displayAccount(String civic) {
        Connection connection = getConnection();
        try {
            assert connection != null;
            String query = "SELECT users.firstname, users.lastname, users.email, users.address, users.age, users.civicnumber, accounts.name_of_account, accounts.amount " +
                    "FROM users " +
                    "JOIN accounts ON users.id = accounts.user_id " +
                    "WHERE users.civicnumber = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, civic);
            ResultSet rs = statement.executeQuery();

            String firstname = null;
            String lastname = null;

            while (rs.next()) {
                if (firstname == null && lastname == null) {
                    firstname = rs.getString("firstname");
                    lastname = rs.getString("lastname");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    int age = rs.getInt("age");
                    String civicN = rs.getString("civicnumber");
                    System.out.println(firstname + " " + lastname + "\n" +
                            email + "\n" +
                            address + "\n" +
                            age + " " + civicN);
                }

                String accountName = rs.getString("name_of_account");
                int amount = rs.getInt("amount");
                System.out.println(accountName + ": " + amount);
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static String Encrypt(String password){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashedPassword = digest.digest(password.getBytes());
            return byteArrayToHexString(hashedPassword) + byteArrayToHexString(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean Verify(String password, String hashedPassword) {
        String passwordHash = hashedPassword.substring(0, 64);
        String saltHex = hashedPassword.substring(64);

        byte[] salt = hexStringToByteArray(saltHex);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            digest.update(salt);

            byte[] hashedInputPassword = digest.digest(password.getBytes());

            String hashedInputPasswordHex = byteArrayToHexString(hashedInputPassword);
            return passwordHash.equals(hashedInputPasswordHex);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }


    private static String byteArrayToHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCivicnumber() {
        return civicnumber;
    }

    public void setCivicnumber(String civicnumber) {
        this.civicnumber = civicnumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
