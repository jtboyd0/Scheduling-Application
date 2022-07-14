package DAO;

import javafx.scene.control.Alert;
import model.Customer;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface retrieves from and manipulates the customers table in a database.
 * @author Jacob Boyd
 */
public interface CustomerDAO {

    /**
     * getAllCustomers retrieves all of the rows from the customers table in a database and stores the customers
     * in the customerList.
     */
    static void getAllCustomers() {
        try(Connection conn = DBConnection.startConnection()){

            String statement = "SELECT * FROM customers";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();
            while(result.next()){
                int customerID = result.getInt("Customer_ID");
                String name = result.getString("Customer_Name");
                String address = result.getString("Address");
                String postalCode = result.getString("Postal_Code");
                String phoneNum = result.getString("Phone");
                String createdBy = result.getString("Created_By");
                String lastUpdatedBy = result.getString("Last_Updated_By");
                int divisionId = result.getInt("Division_ID");
                Customer customer = new Customer(customerID, name, address, postalCode, phoneNum, createdBy, lastUpdatedBy, divisionId);
                CustomerDBImpl.addCustomer(customer);
            }
            DBConnection.closeConnection();

        } catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Problem retrieving Customer information.");
            alert.show();
            DBConnection.closeConnection();
        }
    }

    /**
     * deleteCustomerDB deletes a customer from the customer table in the database.
     * @param customer The customer to be deleted.
     */
    static void deleteCustomerDB(Customer customer){
        try(Connection conn = DBConnection.startConnection()){

            int customerID = customer.getCustomerID();
            String statement = "DELETE FROM customers WHERE Customer_ID = ?";
            DBQuery.setPreparedStatementKeys(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setInt(1, customerID);
            ps.execute();

            CustomerDBImpl.deleteCustomer(customer);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Customer Deleted");
            alert.setContentText("Customer " + customerID + ": " + customer.getName() + ", has been deleted.");
            alert.show();

        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Construction in Progress...");
            alert.setContentText("Please delete all of the Customers appointments before attempting to delete.");
            alert.show();
            DBConnection.closeConnection();
        }
    }

    /**
     * addCustomerDB adds a customer to the customer table in a database.
     * @param customerID The customerID
     * @param name The customer's name
     * @param address The customer's address
     * @param postalCode The customer's postal code
     * @param phone The customer's phone number
     * @param divisionID The customer's division ID
     */
    static void addCustomerDB(int customerID, String name, String address, String postalCode, String phone, int divisionID) {
        try(Connection conn = DBConnection.startConnection()){
            String statement = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?)";
            DBQuery.setPreparedStatementKeys(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            String currentUsername = UserDB.getCurrentUser().getUserName();
            ps.setInt(1, customerID);
            ps.setString(2,name);
            ps.setString(3,address);
            ps.setString(4,postalCode);
            ps.setString(5,phone);
            ps.setString(6,currentUsername);
            ps.setString(7,currentUsername);
            ps.setInt(8,divisionID);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                Customer customer = new Customer(rs.getInt(1), name, address, postalCode, phone, currentUsername, currentUsername, divisionID);
                CustomerDBImpl.addCustomer(customer);
            }
            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

    /**
     * updateCustomer updates a customer's attributes in the customer table.
     * @param name The name
     * @param address The address
     * @param postalCode The postal code
     * @param phone The phone
     * @param divisionID The division ID
     * @param customerID The customer ID
     */
    static void updateCustomer(String name, String address, String postalCode, String phone, int divisionID, int customerID){
        try(Connection conn = DBConnection.startConnection()){

            String statement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, Last_Update = current_timestamp() WHERE Customer_ID = ?";
            DBQuery.setPreparedStatementKeys(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionID);
            ps.setInt(6, customerID);
            ps.execute();
            CustomerDBImpl.updateCustomer(name, address, postalCode, phone, divisionID, customerID);

            DBConnection.closeConnection();

        }catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }
}
