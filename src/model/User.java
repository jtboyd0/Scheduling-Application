package model;

/**
 * This class creates and manipulates users.
 * @author Jacob Boyd
 */
public class User {
    private int userID;
    private String userName;
    private String password;

    /**
     * This constructor initializes the User attributes.
     * @param userId The user's ID
     * @param userName The userName
     * @param password The password
     */
    public User(int userId, String userName, String password){
        this.userID = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * getUserID is a getter for the userID field.
     * @return The userID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * setUserID is a setter for the userID field.
     * @param userID The userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * getUserName is a getter of the userName field.
     * @return The userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName is a setter for the userName field.
     * @param userName The userName.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getPassword is a getter for the password field.
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword is a setter for the password field.
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
