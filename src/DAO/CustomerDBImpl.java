package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

/**
 * This class is the implementaion of the customerDAO interface. It stores a list of all customers and is capable
 * of updating, deleting from. adding to, and getting that list.
 */
public class CustomerDBImpl implements CustomerDAO{

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /**
     * addCustomer adds a customer to the customeList.
     * @param customer The customer to be added.
     */
    public static void addCustomer(Customer customer){
        customerList.add(customer);
    }

    /**
     * updateCustomer updates a customer in the customerList.
     * @param name The name
     * @param address The address
     * @param postalCode The postal code
     * @param phone The phone number
     * @param divisionID The division ID
     * @param customerID The customer ID
     */
    public static void updateCustomer(String name, String address, String postalCode, String phone, int divisionID, int customerID){
        for(Customer c : customerList){
            if(customerID == c.getCustomerID()){
                c.setName(name);
                c.setAddress(address);
                c.setPostalCode(postalCode);
                c.setPhoneNum(phone);
                c.setDivisionId(divisionID);
                c.setLastUpdatedBy(UserDB.getCurrentUser().getUserName());
                c.setDivisionName(FirstLvlDivDBImpl.getFirstLvlDiv(divisionID).getDivision());
                c.setCountryName(CountryDBImpl.getCountry(FirstLvlDivDBImpl.getFirstLvlDiv(divisionID).getCountryID()).getCountryName());
            }
        }
    }

    /**
     * deleteCustomer deletes a customer from the customerList.
     * @param customer The customer to be deleted.
     */
    public static void deleteCustomer(Customer customer){
        customerList.remove(customer);
    }

    /**
     * getCustomerList is a getter for the customerList.
     * @return The customerList.
     */
    public static ObservableList<Customer> getCustomerList(){
        return customerList;
    }

    /**
     * customerExists returns whether or not a customer exists in the customerList.
     * @param customerID The customer ID
     * @return True if the customer with the given ID exists and false if it does not.
     */
    public static boolean customerExists(int customerID){
        for(Customer c : customerList){
            if(customerID == c.getCustomerID()){
                return true;
            }
        }
        return false;
    }

}
