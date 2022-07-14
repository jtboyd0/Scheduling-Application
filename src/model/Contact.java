package model;

/**
 * This class creates and manipulates Contacts.
 * @author Jacob Boyd
 */
public class Contact {

    private int contactID;
    private String contactName;
    private String contactEmail;

    /**
     * This constructor initializes the Contact attributes.
     * @param contactID The contact's ID
     * @param contactName The contact's name.
     * @param contactEmail The contact's email.
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * getContactID is a getter for the contactID field.
     * @return The contactID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * getContactName is a getter for the contactName field.
     * @return The contactName.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * getContactEmail is a getter for the contactEmail field.
     * @return The contactEmail.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * toString overrides the toString method for this class. (When printing the object we see the returned attribute.)
     * @return The contactName.
     */
    @Override
    public String toString(){
        return contactName;
    }
}
