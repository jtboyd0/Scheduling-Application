package DAO;

import model.Contact;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface retrieves from the contacts table in a database.
 * @author Jacob Boyd
 */
public interface ContactDAO {

    /**
     * getAllContacts retrieves all contacts from the contacts table in a database and stores them in the contacts list.
     */
    static void getAllContacts(){
        try(Connection conn = DBConnection.startConnection()){

            String statement = "SELECT * FROM contacts";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();
            while(result.next()){
                int contactID = result.getInt("Contact_ID");
                String contactName = result.getString("Contact_Name");
                String contactEmail = result.getString("Email");
                Contact contact = new Contact(contactID, contactName, contactEmail);
                ContactDBImpl.addContact(contact);
            }
            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

}
