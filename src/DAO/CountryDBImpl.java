package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

/**
 * This class holds a list of countries and is capable of adding to and getting that list.
 * @author Jacob Boyd
 */
public class CountryDBImpl {

    private static final ObservableList<Country> countries = FXCollections.observableArrayList();

    /**
     * addCountry adds a country to the countries list.
     * @param country The country
     */
    public static void addCountry(Country country){
        countries.add(country);
    }

    /**
     * getCountryList is a getter for the countries list.
     * @return The countries list.
     */
    public static ObservableList<Country> getCountryList(){ return countries; }

    /**
     * getCountry is a getter for a country with a given countryID.
     * @param countryID The country ID
     * @return The country with the given country ID.
     */
    public static Country getCountry(int countryID){
        for(Country c : countries){
            if(c.getCountryID() == countryID)
                return c;
        }
        return null;
    }

}
