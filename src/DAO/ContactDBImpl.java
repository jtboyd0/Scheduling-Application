package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

/**
 * This class holds a list of all contacts and is capable of adding to and getting from that list.
 * @author Jacob Boyd
 */
public class ContactDBImpl {

    private static final ObservableList<Contact> contactsList = FXCollections.observableArrayList();

    /**
     * addContact adds a contact to the contactsList.
     * @param contact The contact to be added
     */
    public static void addContact(Contact contact){ contactsList.add(contact); }

    /**
     * getContactsList is a getter for the contactsList.
     * @return The contactsList.
     */
    public static ObservableList<Contact> getContactsList() { return contactsList; }

    /**
     * getContact is a getter for a contact with a given ID
     * @param contactID The contact ID
     * @return The contact with the contactID.
     */
    public static Contact getContact(int contactID){
        for(Contact c : contactsList){
            if(c.getContactID() == contactID)
                return c;
        }
        return null;
    }
}
