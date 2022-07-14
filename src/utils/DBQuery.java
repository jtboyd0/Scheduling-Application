package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class  sets and gets prepared statements for execution.
 * @author Jacob Boyd
 */
public class DBQuery {

    private static PreparedStatement preparedStatement;

    /**
     * setPreparedStatementKeys sets the prepared statement and returns the generated keys to the result set.
     * @param conn The connection
     * @param sqlStatement The sql statement
     * @throws SQLException if the prepared statement cannot be set.
     */
    public static void setPreparedStatementKeys(Connection conn, String sqlStatement) throws SQLException{
        preparedStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
    }

    /**
     * setPreparedStatement sets the prepared statement.
     * @param conn The connection
     * @param sqlStatement The sql statement
     * @throws SQLException if the prepared statement cannot be set.
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException{
        preparedStatement = conn.prepareStatement(sqlStatement);
    }

    /**
     * getPreparedStatement is a getter for the prepared statement.
     * @return The prepared statement.
     */
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }
}
