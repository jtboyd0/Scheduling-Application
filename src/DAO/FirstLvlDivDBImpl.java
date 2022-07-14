package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLvlDiv;

/**
 * This class holds a list of all the first level divisions and is capable of adding to and getting from that list.
 * @author Jacob Boyd
 */
public class FirstLvlDivDBImpl {

    private static ObservableList<FirstLvlDiv> firstLvlDivsList = FXCollections.observableArrayList();

    /**
     * filterFirstLvlDivsList gets all of the first level divisions with a given countryID
     * @param countryID The country ID
     * @return All of the First level divisions with the associated countryID.
     */
    public static ObservableList<FirstLvlDiv> filterFirstLvlDivsList(int countryID){
        ObservableList<FirstLvlDiv> filteredList = FXCollections.observableArrayList();
        for(FirstLvlDiv f : firstLvlDivsList){
            if(f.getCountryID() == countryID){
                filteredList.add(f);
            }
        }
        return filteredList;
    }

    /**
     * getFirstLvlDivsList is a getter for the firstLvlDivsList.
     * @return The first level divisions list.
     */
    public static ObservableList<FirstLvlDiv> getFirstLvlDivsList(){
        return firstLvlDivsList;
    }

    /**
     * addFirstLvlDiv adds a first level division to the firstLvlDivsList.
     * @param firstLvlDiv the firstLvlDiv to be added.
     */
    public static void addFirstLvlDiv(FirstLvlDiv firstLvlDiv){
        firstLvlDivsList.add(firstLvlDiv);
    }

    /**
     * getFirstLvlDiv is a getter for a first level division with a given division ID
     * @param divisionID The division ID
     * @return The first level division with the given division ID.
     */
    public static FirstLvlDiv getFirstLvlDiv(int divisionID){
        for(FirstLvlDiv fld : firstLvlDivsList){
            if(fld.getDivisionID() == divisionID)
                return fld;
        }
        return null;
    }
}
