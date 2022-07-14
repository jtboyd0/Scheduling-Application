package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;

import java.time.*;
import java.util.function.Predicate;

/**
 * This class holds a list of all appointments and is capable of adding/deleting/updating appointments on that list.
 * @author Jacob Boyd
 */
public class AppointmentDBImpl{

    private static ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

    /**
     * addAppointment adds an appointment to the appointmentsList.
     * @param appointment The appointment to be added.
     */
    public static void addAppointment(Appointment appointment){ appointmentsList.add(appointment); }

    /**
     * deleteAppointment deletes an appointment from the appointmentsList.
     * @param appointment The appointment to be deleted.
     */
    public static void deleteAppointment(Appointment appointment){ appointmentsList.remove(appointment); }

    /**
     * getAppointmentsList is a getter for the appointmentsList.
     * @return The appointmentList.
     */
    public static ObservableList<Appointment> getAppointmentsList() { return appointmentsList; }

    /**
     * getAppointmentListByMonth is a getter for all of the appointments in the current month.
     * @return List of appointments in the current month
     */
    public static ObservableList<Appointment> getAppointmentListByMonth() {
        ObservableList<Appointment> appointmentsListByMonth = FXCollections.observableArrayList();
        for(Appointment a : appointmentsList){
            if(a.getStartDateTime().toLocalDate().getMonthValue() == LocalDate.now().getMonthValue()){
                appointmentsListByMonth.add(a);
            }
        }
        return appointmentsListByMonth;
    }

    /**
     * getAppointmentListByWeek is a getter for all of the appointments in the upcoming seven days. (Does not include
     * appointments of previous days)
     * @return List of appointments on that day and next 6 days.
     */
    public static ObservableList<Appointment> getAppointmentListByWeek(){
        ObservableList<Appointment> appointmentsListByWeek = FXCollections.observableArrayList();
        for(Appointment a : appointmentsList){
            if(a.getStartDateTime().toLocalDate().isBefore(LocalDate.now().plusDays(7)) &&
                    (a.getStartDateTime().toLocalDate().isAfter(LocalDate.now()) || a.getStartDateTime().toLocalDate().isEqual(LocalDate.now()))){
                appointmentsListByWeek.add(a);
            }
        }
        return appointmentsListByWeek;

    }

    /**
     * updateAppointment updates the fields of an appointment in the appointmentList.
     * @param appointmentID The appointmentID
     * @param title The title
     * @param description The description
     * @param location The location
     * @param contactID The contactID
     * @param type The type
     * @param startDateTime The start date/time
     * @param endDateTime The end date/time
     * @param customerID the customerID
     * @param userID The userID
     */
    public static void updateAppointment(int appointmentID, String title, String description, String location,
                                         int contactID, String type, LocalDateTime startDateTime,
                                         LocalDateTime endDateTime, int customerID, int userID){
        for(Appointment a : appointmentsList){
            if(appointmentID == a.getAppointmentID()){
                a.setTitle(title);
                a.setDescription(description);
                a.setLocation(location);
                a.setContactID(contactID);
                a.setContactName(ContactDBImpl.getContact(contactID).getContactName());
                a.setType(type);
                a.setStartDateTime(startDateTime);
                a.setEndDateTime(endDateTime);
                a.setCustomerID(customerID);
                a.setUserID(userID);
                a.setDisplayStartDateTime(startDateTime);
                a.setDisplayEndDateTime(endDateTime);
            }
        }
    }

    /**
     * getAppointment is a getter for a specific appointment.
     * @param appointmentID The appointment ID
     * @return The appointment with matching appointment ID.
     */
    public static Appointment getAppointment(int appointmentID){
        for(Appointment a : appointmentsList){
            if(a.getAppointmentID() == appointmentID)
                return a;
        }
        return null;
    }

    /**
     * isOverlapping checks whether or not an appointments duration overlaps with any other appointment in appointmentsList.
     * @param startDate The start date
     * @param startTime The start time
     * @param endDate The end date
     * @param endTime The end time
     * @param appointmentID The appointment ID
     * @return True if it overlaps with any other appointment and False if it does not.
     */
    public static boolean isOverlapping(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int appointmentID){
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        for(Appointment a : appointmentsList){
            if(appointmentID != a.getAppointmentID()){
                if(startDateTime.isBefore(a.getEndDateTime()) && startDateTime.isAfter(a.getStartDateTime()))
                    return true;
                else if(endDateTime.isBefore(a.getEndDateTime()) && endDateTime.isAfter(a.getStartDateTime()))
                    return true;
                else if(startDateTime.equals(a.getStartDateTime()) || endDateTime.equals(a.getEndDateTime()))
                    return true;
                else if(startDateTime.isBefore(a.getStartDateTime()) && endDateTime.isAfter(a.getEndDateTime()))
                    return true;
            }
        }
        return false;
    }

    /**
     * isOutsideHours Determines if the appointment duration is outside of the business hours (in EST).
     * @param startDateTime The start date/time
     * @param endDateTime The end date/time
     * @return True if it is outside of the business hours, False if it is not.
     */
    public static boolean isOutsideHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalTime businessOpen = LocalTime.of(8, 0);
        LocalTime businessClose = LocalTime.of(22, 0);

        if (startDateTime.toLocalTime().isAfter(businessClose) || startDateTime.toLocalTime().isBefore(businessOpen)) {
            return true;
        }

        if (endDateTime.toLocalTime().isBefore(businessOpen) || endDateTime.toLocalTime().isAfter(businessClose)) {
            return true;
        }

        if (!startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
            return true;
        }

        return false;
    }

    /**
     * meetingSoon determines whether a meeting is occurring soon. (Within 15 minutes of logon)
     * @return An appointment ID and start time if one is occurring soon, else returns an empty string.
     */
    public static String meetingSoon(){
        ZonedDateTime nowSystem = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime nowUTC = nowSystem.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime now = nowUTC.toLocalDateTime();

        for(Appointment a : appointmentsList){
            if(a.getStartDateTime().isBefore(now.plusMinutes(15)) && a.getStartDateTime().isAfter(now)){
                String appointmentID = Integer.toString(a.getAppointmentID());
                String dateTime = a.getDisplayStartDateTime();
                return appointmentID + dateTime;
            }
        }
        return "";
    }

    /**
     * getTypeList is a getter for all of the types of appointments in appointmentList.
     * @return A list of all of the types of appointments in appointmentList
     */
    public static ObservableList<String> getTypeList(){
        ObservableList<String> typeList = FXCollections.observableArrayList();
        for(Appointment a : AppointmentDBImpl.getAppointmentsList()){
            if(!typeList.contains(a.getType())){
                typeList.add(a.getType());
            }
        }
        return typeList;

    }

    /**
     * Lambda: This method contains a lambda expression. The findNumOfAppointments method allows for growth/change of
     * attributes in the appointment class and will still be able to handle a search utilizing those fields with simply one
     * line.
     * getNumberOfAppsByMonth is a getter for the number of appointments of a particular type in each month
     * @param type The Type
     * @param monthID The month
     * @return The number of appointments of a particular type in a month.
     */
    public static int getNumberOfAppsByMonth(String type, int monthID){
        /*int count = 0;
        for(Appointment a: appointmentsList){
            if(a.getType().equals(type) && a.getStartDateTime().getMonthValue() == monthID){
                count++;
            }
        }
        return count;

         */

        int count = findNumOfAppointments(appointmentsList, a -> a.getType().equals(type) && a.getStartDateTime().getMonthValue() == monthID);
        return count;
    }

    /**
     * Lambda: This method contains a lambda expression. The findAppointments method allows for growth/change of
     * attributes in the appointment class and will still be able to handle a search utilizing those fields with simply one
     * line.
     * getContactSchedule is a getter for all of the appointments that associate with a given contact.
     * @param contact The contact
     * @return The list of the contact's appointments.
     */
    public static ObservableList<Appointment> getContactSchedule(Contact contact){
        ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();

        /*for(Appointment a: appointmentsList){
            if(a.getContactID() == contact.getContactID()){
                contactSchedule.add(a);
            }
        }
        return contactSchedule;

         */
        //Replaced code with lambda expression.
        contactSchedule = findAppointments(appointmentsList, a -> a.getContactID() == contact.getContactID());
        return contactSchedule;
    }

    /**
     * Lambda: This method contains a lambda expression. The findAppointments method allows for growth/change of
     * attributes in the appointment class and will still be able to handle a search utilizing those fields with simply one
     * line.
     * getCustomerSchedule is a getter for all of the appointments that associate with a given customer.
     * @param customerID The customerID
     * @return The list of the customer's appointments.
     */
    public static ObservableList<Appointment> getCustomerSchedule(int customerID){
        ObservableList<Appointment> customerSchedule = FXCollections.observableArrayList();

       /* for(Appointment a: appointmentsList){
            if(a.getCustomerID() == customerID){
                customerSchedule.add(a);
            }
        }
        return customerSchedule;

        */
        //Replaced Code with lambda expression.
        customerSchedule = findAppointments(appointmentsList, a -> a.getCustomerID() == customerID);
        return customerSchedule;
    }

    /**
     * findAppointments gets all of the appointments that meet a given set of rules.
     * @param appointments The appointments List.
     * @param checker The set of rules.
     * @return The appointments whose attributes meet the criteria.
     */
    private static ObservableList<Appointment> findAppointments(ObservableList<Appointment> appointments, Predicate<Appointment> checker){
        ObservableList<Appointment> checkedAppointments= FXCollections.observableArrayList();
        for(Appointment a: appointments){
            if(checker.test(a)){
                checkedAppointments.add(a);
            }
        }
        return checkedAppointments;
    }

    /**
     * findNumOfAppointments gets the number of appointments that meet a given set of rules.
     * @param appointments The appointments list.
     * @param checker The set of rules
     * @return The number of appointments whose attributes meet the criteria.
     */
    private static int findNumOfAppointments(ObservableList<Appointment> appointments, Predicate<Appointment> checker){
        int count = 0;
        for(Appointment a : appointments){
            if(checker.test(a)){
                count++;
            }
        }
        return count;
    }
}
