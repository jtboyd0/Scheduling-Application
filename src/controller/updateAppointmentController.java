package controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class is the controller for the updateAppointment form.
 * @author Jacob Boyd
 */
public class updateAppointmentController implements Initializable {

    Stage stage;
    Parent scene;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    @FXML private TextField appointmentIDTxt;
    @FXML private TextField titleTxt;
    @FXML private TextField descriptionTxt;
    @FXML private TextField locationTxt;
    @FXML private ComboBox<Contact> contactComBox;
    @FXML private TextField typeTxt;
    @FXML private DatePicker startDateDatePicker;
    @FXML private ComboBox<String> startTimeComBox;
    @FXML private DatePicker endDateDatePicker;
    @FXML private ComboBox<String> endTimeComBox;
    @FXML private TextField customerIDTxt;
    @FXML private TextField userIDTxt;

    /**
     * cancel changes the view to the main menu without saving anything.
     * @param event fires when the cancel button is clicked.
     * @throws IOException if the main menu form is missing.
     */
    @FXML void cancel(ActionEvent event) throws IOException {
        switchViews(event, "/view/mainMenuForm1.fxml");
    }

    /**
     * submit attempts to update an appointment in the database and appointment list. If it is unsuccessful an error
     * alert will display with the proper information. If successful, the form will be changed to the main menu form.
     * @param event fires when the user clicks the submit button.
     * @throws IOException if the main menu form is missing.
     */
    @FXML void submit(ActionEvent event) throws IOException {
        String errorString = "";
        int appointmentID = Integer.parseInt(appointmentIDTxt.getText());
        int contactID = -1;
        int customerID = -1;
        int userID = -1;
        String title = "";
        String description = "";
        String location = "";
        String type = "";
        LocalDate startDate = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        LocalDate endDate = LocalDate.now();
        LocalTime endTime = LocalTime.now();

        if(!(contactComBox.getValue() == null)){
            contactID = contactComBox.getSelectionModel().getSelectedItem().getContactID();
        } else {
            errorString += "Please select a contact. \n";
        }
        try {
            customerID = Integer.parseInt(customerIDTxt.getText());
            if(!CustomerDBImpl.customerExists(customerID)){
                errorString += "Please input a valid customer ID\n";
            }

        } catch (NumberFormatException e) {
            errorString += "Please input a valid integer for customerID. \n";
        }

        try {
            userID = Integer.parseInt(userIDTxt.getText());
            if(!UserDB.userExists(userID)){
                errorString += "Please input a valid user ID. \n";
            }
        } catch (NumberFormatException e) {
            errorString += "Please input a valid integer for userID. \n";
        }
        if (!titleTxt.getText().replaceAll(" ", "").equals("")) {
            title = titleTxt.getText();
        } else {
            errorString += "Please enter a title. \n";
        }
        if (!descriptionTxt.getText().replaceAll(" ", "").equals("")) {
            description = descriptionTxt.getText();
        } else {
            errorString += "Please enter a description. \n";
        }
        if (!locationTxt.getText().replaceAll(" ", "").equals("")) {
            location = locationTxt.getText();
        } else {
            errorString += "Please enter a loaction. \n";
        }
        if (!typeTxt.getText().replaceAll(" ", "").equals("")) {
            type = typeTxt.getText();
        } else {
            errorString += "Please enter a type. \n";
        }
        if (!(startDateDatePicker.getValue() == null)) {
            startDate = startDateDatePicker.getValue();
        } else {
            errorString += "Please select a start Date \n";
        }
        if (!(endDateDatePicker.getValue() == null)) {
            endDate = endDateDatePicker.getValue();
        } else {
            errorString += "Please select an end date. \n";
        }

        startTime = LocalTime.parse(startTimeComBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);
        endTime = LocalTime.parse(endTimeComBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);

        //Checks if the hours of the appointment are within business hours 8-10 EST
        ZonedDateTime zoneStartDateTime = LocalDateTime.of(startDate, startTime).atZone(ZoneId.systemDefault());
        ZonedDateTime converter = zoneStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime startDateTimeEST = converter.toLocalDateTime();
        System.out.println(startDateTimeEST);

        zoneStartDateTime = LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault());
        converter = zoneStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime endDateTimeEST = converter.toLocalDateTime();
        System.out.println(endDateTimeEST);

        if(AppointmentDBImpl.isOutsideHours(startDateTimeEST, endDateTimeEST)){
            errorString += "Please select appointment time that falls between business hours. Business hours are 8 a.m to 10 p.m EST. \n";
        }

        //Checks if the appointment is overlapping with any other appointments.
        if(AppointmentDBImpl.isOverlapping(startDate, startTime, endDate,endTime, appointmentID) && errorString.equals("")){
            errorString += "Please select a time that does not overlap with other appointments. \n";
        }

        if(!errorString.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText(errorString);
            alert.show();
        } else {

            AppointmentDAO.updateAppointment(appointmentID, title, description, location, type, startDate, startTime, endDate, endTime, userID, customerID, contactID);
            switchViews(event, "/view/mainMenuForm1.fxml");
        }
    }

    /**
     * selectTime populates the end time combo box with the times available after the selected start time and before
     * closing hours. The times displayed are based on the user's system default.
     * @param event fires when a time is selected in the startTime combo box.
     */
    @FXML void selectTime(ActionEvent event) {
        endTimeComBox.getSelectionModel().clearSelection();
        endTimeComBox.getItems().clear();
        LocalTime startTime;
        if(startTimeComBox.getSelectionModel().getSelectedItem().contains("PM") && !startTimeComBox.getSelectionModel().getSelectedItem().contains("12")){
            startTime = LocalTime.parse(startTimeComBox.getSelectionModel().getSelectedItem().substring(0,5)).plusHours(12);
        } else {
            startTime = LocalTime.parse(startTimeComBox.getSelectionModel().getSelectedItem().substring(0,5));
        }

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(22,0));
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime transferZone = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime endTime = transferZone.toLocalTime();
        while(startTime.isBefore(endTime)){
            startTime = startTime.plusMinutes(10);
            endTimeComBox.getItems().add(startTime.format(dateTimeFormatter));
        }
    }

    /**
     * switchViews changes the view from the current form to a target form.
     * @param event fires when the user clicks cancel or submit(successful).
     * @param fileLocation The form file path
     * @throws IOException if the target form is missing.
     */
    public void switchViews(ActionEvent event, String fileLocation) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * initialize initializes the values in the updateAppointment form.
     * @param url The url
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactComBox.setItems(ContactDBImpl.getContactsList());
    }

    /**
     * passSelectedAppointment allows for the passing of information from the main menu to this controller for
     * initialization purposes.
     * @param selectedItem The passed appointment.
     */
    public void passSelectedAppointment(Appointment selectedItem) {
        appointmentIDTxt.setText(Integer.toString(selectedItem.getAppointmentID()));
        titleTxt.setText(selectedItem.getTitle());
        descriptionTxt.setText(selectedItem.getDescription());
        locationTxt.setText(selectedItem.getLocation());
        contactComBox.getSelectionModel().select(ContactDBImpl.getContact(selectedItem.getContactID()));
        typeTxt.setText(selectedItem.getType());

        startDateDatePicker.setValue(selectedItem.getStartDateTime().toLocalDate());

        endDateDatePicker.setValue(selectedItem.getEndDateTime().toLocalDate());

        //Initializes the comboxes values
        LocalDate tempNow = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(tempNow, LocalTime.of(8,0));
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime transferZone = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime startTime = transferZone.toLocalTime();

        localDateTime = LocalDateTime.of(tempNow, LocalTime.of(22,0));
        zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        transferZone = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime endTime = transferZone.toLocalTime();

        while(startTime.isBefore(endTime)){
            startTimeComBox.getItems().add(startTime.format(dateTimeFormatter));
            startTime = startTime.plusMinutes(10);
        }

        //Selects the start/end times of the appointment that is being updated.

        LocalTime tempStartTime = selectedItem.getStartDateTime().toLocalTime();
        while(tempStartTime.isBefore(endTime)){
            tempStartTime = tempStartTime.plusMinutes(10);
            endTimeComBox.getItems().add(tempStartTime.format(dateTimeFormatter));
        }

        startTimeComBox.getSelectionModel().select(selectedItem.getStartDateTime().toLocalTime().format(dateTimeFormatter));
        endTimeComBox.getSelectionModel().select(selectedItem.getEndDateTime().toLocalTime().format(dateTimeFormatter));

        customerIDTxt.setText(Integer.toString(selectedItem.getCustomerID()));
        userIDTxt.setText(Integer.toString(selectedItem.getUserID()));
    }
}
