package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class retrieves and maintains a list of users from a database and is capable of reading from that list and
 * making the user the active use on the application.
 * @author Jacob Boyd
 */
public class UserDB{

    private static User currentUser = new User(-1, null, null);
    private static ObservableList<User> userList = FXCollections.observableArrayList();

    /**
     * getAllUsers retrieves all users from the users table in a database.
     */
    public static void getAllUsers() {
        try(Connection conn = DBConnection.startConnection()){
            int setUserID;
            String setUserName;
            String setUserPassword;

            String statement = "SELECT * FROM users";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();

            while(result.next()){
                setUserID = result.getInt("User_ID");
                setUserName = result.getString("User_Name");
                setUserPassword = result.getString("Password");
                User user = new User(setUserID, setUserName, setUserPassword);
                userList.add(user);
            }

            DBConnection.closeConnection();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

    /**
     * getUser retrieves a user from the users table in a database that matches the criteria.
     * @param username The username
     * @param password The password
     * @return The user whose username and password match the given credentials.
     */
    public static boolean getUser(String username, String password) {
        try(Connection conn = DBConnection.startConnection()){
            int setUserID;
            String setUserName;
            String setUserPassword;

            String statement = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
            ResultSet result = ps.getResultSet();

            while(result.next()){

                if(username.equals(result.getString("User_Name")) && password.equals(result.getString("Password"))){
                    setUserID = result.getInt("User_ID");
                    setUserName = result.getString("User_Name");
                    setUserPassword = result.getString("Password");
                    setCurrentUser(setUserID, setUserName, setUserPassword);
                    DBConnection.closeConnection();
                    return true;
                }
            }

            DBConnection.closeConnection();
            return false;

        } catch(Exception e) {
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * setCurrentUser sets a user to be the user currently using the application
     * @param userID The userID
     * @param userName The userName
     * @param password The password
     */
    public static void setCurrentUser(int userID, String userName, String password){
        currentUser.setUserID(userID);
        currentUser.setUserName(userName);
        currentUser.setPassword(password);
    }

    /**
     * getCurrentUser is a getter for the currentUser.
     * @return The current user.
     */
    public static User getCurrentUser(){
        return currentUser;
    }

    /**
     * userExists returns whether or not a user with a given ID exists in the userList.
     * @param userID The userID
     * @return True if the user exists in the userList and false if it does not.
     */
    public static boolean userExists(int userID){
        for(User u : userList){
            if(userID == u.getUserID()){
                return true;
            }
        }
        return false;
    }
}