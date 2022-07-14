package model;

import DAO.ContactDBImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class creates and manipulates Appointments.
 * @author Jacob Boyd
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private int contactID;
    private String contactName;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime displayStartDateTime;
    private LocalDateTime displayEndDateTime;
    private int customerID;
    private int userID;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    /**
     * This constructor initializes the Appointment's attributes. The display start/end dateTimes are used solely for
     * display purposes. They are updated
     * @param appointmentID The appointment's ID
     * @param title The appointment's title
     * @param description The appointment's description
     * @param location The appointment's location
     * @param contactID The appointment's associated contactID
     * @param type The appointment's type
     * @param startDateTime The appointment's start date/time
     * @param endDateTime The appointment's end date/time
     * @param customerID The appointment's associated customerID
     * @param userID The appointment's associated userID
     */
    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID, int userID) {
        this.setAppointmentID(appointmentID);
        this.setTitle(title);
        this.setDescription(description);
        this.setLocation(location);
        this.setContactID(contactID);
        this.setType(type);
        this.setStartDateTime(startDateTime);
        this.setEndDateTime(endDateTime);
        this.setCustomerID(customerID);
        this.setUserID(userID);
        this.contactName = ContactDBImpl.getContact(contactID).getContactName();
        this.displayStartDateTime = startDateTime;
        this.displayEndDateTime = endDateTime;
    }

    /**
     * getAppointmentID is a getter for the appointmentID field.
     * @return The value in the appointmentID field.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * setAppointmentID is a setter for the appointmentID field.
     * @param appointmentID The appointment ID.
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * getTitle is a getter for the title field.
     * @return The appointment's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle is a setter for the title field.
     * @param title The appointment title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getDescription is a getter for the description field.
     * @return The appointment's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription is a setter for the description field.
     * @param description The appointment description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getLocation is a getter for the location field.
     * @return The appointment's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * setLocation is a setter for the location field.
     * @param location The appointment location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * getContactID is a getter for the contactID field
     * @return The appointment's contactID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * setContactID is a setter for the contactID field.
     * @param contactID The appointment associated contactID.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * getContactName is a getter for the contactName field.
     * @return The appointment's contactName.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * setContactName is a setter for the contactName field.
     * @param contactName The appointment's contactName.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * getType is a getter for the type field.
     * @return The appointment's type.
     */
    public String getType() {
        return type;
    }

    /**
     * setType is a setter for the type field.
     * @param type The appointment type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getStartDateTime is a getter for the startDateTime field.
     * @return The appointment's startDateTime.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * setStartDateTime is a setter for the startDateTime field.
     * @param startDateTime The appointment start date/time.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * getEndDateTime is a getter for the endDateTime field.
     * @return The appointment's endDateTime.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * setEndDateTime is a setter for the endDateTime field.
     * @param endDateTime The appointment end date/Time.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * getCustomerID is a getter for the customerID field.
     * @return The appointment's customerID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * setCustomerID is a setter for the customerID field.
     * @param customerID The appointment's associated customerID.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * getUserID is a getter for the userID field.
     * @return The appointment's userID.
     */
    public int getUserID() { return userID; }

    /**
     * setUserID is a setter for the userID field.
     * @param userID The appointment's associated userID.
     */
    public void setUserID(int userID) { this.userID = userID; }

    /**
     * getDisplayStartDateTime is a getter for the displayStartDateTime field.
     * @return The appointment's displayStartDateTime.
     */
    public String getDisplayStartDateTime() {
        return displayStartDateTime.format(dateTimeFormatter);
    }

    /**
     * setDisplayStartDateTime is a setter for the displayStartDateTime field.
     * @param displayStartDateTime The appointment's display start date/time.
     */
    public void setDisplayStartDateTime(LocalDateTime displayStartDateTime) {
        this.displayStartDateTime = displayStartDateTime;
    }

    /**
     * getDisplayEndDateTime is a getter for the displayEndDateTime field.
     * @return The appointment's displayEndDateTime.
     */
    public String getDisplayEndDateTime() {
        return displayEndDateTime.format(dateTimeFormatter);
    }

    /**
     * setDisplayEndDateTime is a setter for the displayEndDateTime field.
     * @param displayEndDateTime The appointment's display end date/time.
     */
    public void setDisplayEndDateTime(LocalDateTime displayEndDateTime) {
        this.displayEndDateTime = displayEndDateTime;
    }
}
