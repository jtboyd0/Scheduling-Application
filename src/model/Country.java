package model;

/**
 * This class creates and manipulates Countries.
 * @author Jacob Boyd
 */
public class Country {

    private int countryID;
    private String countryName;

    /**
     * This constructor initializes the Country attributes.
     * @param countryID The countryID
     * @param countryName The countryName
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * getCountryID is a getter for the countryID field.
     * @return The countryID.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * getCountryName is a getter for the countryName field.
     * @return The countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Overrides the toString method for this class for display purposes.
     * @return The countryName.
     */
    @Override
    public String toString(){
        return countryName;
    }
}
