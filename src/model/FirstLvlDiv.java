package model;

/**
 * This class creates and manipulates first level divisions.
 * @author Jacob Boyd
 */
public class FirstLvlDiv {
    private int divisionID;
    private String division;
    private int countryID;

    /**
     * This constructor initializes the FirstLvlDiv attributes.
     * @param divisionID The divisionID
     * @param division The division
     * @param countryID The countryID
     */
    public FirstLvlDiv(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    /**
     * getDivisonID is a getter for the divisionID field.
     * @return The divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * getDivison is a getter for the divison field.
     * @return The division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * getCountryID is a getter for the countryID.
     * @return The countryID.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * toString overrides the toString method for this class.
     * @return The division.
     */
    @Override
    public String toString(){
        return division;
    }
}

