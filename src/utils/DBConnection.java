package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class starts and closes the connection to a database.
 * @author Jacob Boyd
 */
public class DBConnection {

    //DB URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ07PXW";

    //Full DB URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver and connection interface reference
    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //Username
    private static final String username = "U07PXW";

    //Password
    private static final String password = "53689093686";

    /**
     * startConnection starts the connection to the database given the correct login information. (And that the database is operational).
     * @return The connection.
     */
    public static Connection startConnection() {
        try {
            Class.forName(mySQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful");
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * closeConnection closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        } catch (SQLException e){
            System.out.println("ERROR: " + e.getMessage() );
        }
    }
}
