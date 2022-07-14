package model;

import DAO.CountryDBImpl;
import DAO.FirstLvlDivDBImpl;

/**
 * This class creates and manipulates Customers.
 * @author Jacob Boyd
 */
public class Customer {

    private int customerID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNum;
    private String createdBy;
    private String lastUpdatedBy;
    private int divisionId;
    private String divisionName;
    private String countryName;

    /**
     * This constructor initializes the customer attributes.
     * @param customerID The customer's ID
     * @param name The customer's name
     * @param address The customer's address
     * @param postalCode The customer's postal code
     * @param phoneNum The customer's phone number
     * @param createdBy The user who created the customer
     * @param lastUpdatedBy The user who last update this customer
     * @param divisionId The customer's division ID
     */
    public Customer(int customerID, String name, String address, String postalCode, String phoneNum, String createdBy, String lastUpdatedBy, int divisionId) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNum = phoneNum;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.divisionName = FirstLvlDivDBImpl.getFirstLvlDiv(divisionId).getDivision();
        this.countryName = CountryDBImpl.getCountry(FirstLvlDivDBImpl.getFirstLvlDiv(divisionId).getCountryID()).getCountryName();
    }

    /**
     * getCustomerID is a getter for the customerID field.
     * @return The customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * getName is a getter for the name field.
     * @return The customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * setName is a setter for the name field.
     * @param name The customer's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getAddress is a getter for the address field.
     * @return The customer's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * setAddress is a setter for the address field.
     * @param address The customer's address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getPostalCode is a getter for the postalCode field.
     * @return The customer's postal code.
     */
    public String getPostalCode() { return postalCode; }

    /**
     * setPostalCode is a setter for the postalCode field.
     * @param postalCode The customer's postal code.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * getPhoneNum is a getter for the phoneNum field.
     * @return The customer's phone number.
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * setPhoneNum is a setter for the phoneNum field.
     * @param phoneNum The customer's phone number.
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * getCreatedBy is a getter for the createdBy field.
     * @return The creator of the customer.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * setCreatedBy is a setter for the createdBy field.
     * @param createdBy The creator of the customer.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * getLastUpdatedBy is a getter for the lastUpdatedField.
     * @return The last updater of the customer.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * setLastUpdatedBy is a setter for the lastUpdatedBy field.
     * @param lastUpdatedBy The last updater of the customer.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * getDivisionId is a getter for the divisionId field.
     * @return The divisionId.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * setDivisonId is a setter for the divisonId field.
     * @param divisionId The divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * getDivisionName is a getter for the divisionName field.
     * @return The divisionName.
     */
    public String getDivisionName() { return divisionName; }

    /**
     * setDivisionName is a setter for the divisionName field.
     * @param divisionName The division name.
     */
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    /**
     * getCountryName is a getter for the countryName field.
     * @return The country name.
     */
    public String getCountryName() { return countryName; }

    /**
     * setCountryName is a setter for the countryName field.
     * @param countryName The country name.
     */
    public void setCountryName(String countryName) { this.countryName = countryName; }
}
