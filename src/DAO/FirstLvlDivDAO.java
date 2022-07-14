package DAO;

import model.FirstLvlDiv;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface retrieves all of the First Level Divisions from the first_level_divisions table in a database.
 * @author Jacob Boyd
 */
public interface FirstLvlDivDAO {

    /**
     * getAllFirstLvlDivs retrieves all of the first level divisions from the first_level_divisions table in a database
     * and stores them in the FirstLvlDiv list.
     */
    static void getAllFirstLvlDivs(){
        try(Connection conn = DBConnection.startConnection()){

            String statement = "SELECT * FROM first_level_divisions";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();
            while(result.next()){
                int divisionID = result.getInt("Division_ID");
                String division = result.getString("Division");
                int countryID = result.getInt("Country_ID");
                FirstLvlDiv firstLvlDiv = new FirstLvlDiv(divisionID, division, countryID);
                FirstLvlDivDBImpl.addFirstLvlDiv(firstLvlDiv);
            }
            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }
}
