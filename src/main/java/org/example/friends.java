package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.Model.getConnection;

public class friends {
    private int id;
    private int userID;
    private int friendUserID;

    public static void makeFriends(String civicnumber) throws SQLException {
        Connection connection = getConnection();
        String friendIDQuery = "SELECT id,firstname FROM users WHERE civicnumber = ?";
        PreparedStatement friendIDStatement = connection.prepareStatement(friendIDQuery);
        friendIDStatement.setString(1, civicnumber);
        String idQuery = "SELECT id,firstname FROM users WHERE isOnline = true";
        try {
            PreparedStatement idStatement = connection.prepareStatement(idQuery);
            ResultSet rs = idStatement.executeQuery();
            if (rs.next()) {
                String insertQuery = "INSERT INTO friends(UserID, FriendUserID, name) VALUES (?, ?, ?)";
                PreparedStatement firstFriend = connection.prepareStatement(insertQuery);
                firstFriend.setString(1, rs.getString("id"));

                ResultSet friendIDResultSet = friendIDStatement.executeQuery();
                if (friendIDResultSet.next()) {
                    firstFriend.setString(2, friendIDResultSet.getString("id"));
                    firstFriend.setString(3, friendIDResultSet.getString("firstname"));
                    firstFriend.executeUpdate();
                }

                String insertQuery2 = "INSERT INTO friends(UserID, FriendUserID, name) VALUES (?, ?, ?)";
                PreparedStatement secondFriend = connection.prepareStatement(insertQuery2);
                secondFriend.setString(1, friendIDResultSet.getString("id"));
                secondFriend.setString(2, rs.getString("id"));
                secondFriend.setString(3,rs.getString("firstname"));
                secondFriend.executeUpdate();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void showFriends(String civic){
        Connection connection = getConnection();
        String query = "SELECT FriendUserID, name FROM friends WHERE userID IN (SELECT id FROM users WHERE civicnumber = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,civic);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("FriendUserID");
                String name = resultSet.getString("name");
                System.out.println(userID + ": "+ name);
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFriendUserID() {
        return friendUserID;
    }

    public void setFriendUserID(int friendUserID) {
        this.friendUserID = friendUserID;
    }
}
