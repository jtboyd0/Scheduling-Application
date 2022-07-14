package DAO;

import model.Country;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface retrieves all countries from the country table in a database.
 * @author Jacob Boyd
 */
public interface CountryDAO{

    /**
     * getAllCountries retrieves all countries from the country table in a database and stores them in the country list.
     */
    static void getAllCountries() {
        try(Connection conn = DBConnection.startConnection()){

            String statement = "SELECT * FROM countries";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();
            while(result.next()){
                int countryID = result.getInt("Country_ID");
                String countryName = result.getString("Country");
                Country country = new Country(countryID, countryName);
                CountryDBImpl.addCountry(country);
            }
            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }
}
