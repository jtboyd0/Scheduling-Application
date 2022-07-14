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
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class is the controller for the add appointment form.
 * @author Jacob Boyd
 */
public class addAppointmentController implements Initializable{

    Stage stage;
    Parent scene;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    //FXML field declarations
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
     * submit handles the user adding an appointment to the database/ appointment list.
     * @param event fires when the user hits the submit button.
     * @throws IOException If the the add appointment or main menu form are missing.
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
            errorString += "Please enter a location. \n";
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

        zoneStartDateTime = LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault());
        converter = zoneStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime endDateTimeEST = converter.toLocalDateTime();

        if(AppointmentDBImpl.isOutsideHours(startDateTimeEST, endDateTimeEST)){
            errorString += "Please select appointment time that falls between business hours. Business hours are 8 a.m to 10 p.m EST. \n";
        }

        //Checks if the appointment is overlapping with any other appointments.
        if(AppointmentDBImpl.isOverlapping(startDate, startTime, endDate,endTime, appointmentID)){
            errorString += "Please select a time that does not overlap with other appointments. \n";
        }

        if(!errorString.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText(errorString);
            alert.show();
        } else {

            AppointmentDAO.addAppointment(appointmentID, title, description, location, type, startDate, startTime, endDate,
                    endTime, userID, customerID, contactID);
            switchViews(event, "/view/mainMenuForm1.fxml");
        }
    }

    /**
     * cancel will change the view from the add appointment form back to the main menu.
     * @param event fires when the user clicks the cancel button.
     * @throws IOException If the add appointment or main menu form are missing.
     */
    @FXML void cancel(ActionEvent event) throws IOException {
        switchViews(event,"/view/mainMenuForm1.fxml");
    }

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
     * switchViews handles changing the view from the add appointment form to the main menu form.
     * @param event fires when the user submits or cancels
     * @param fileLocation The file location of the target form
     * @throws IOException If the target form is missing or non-existent.
     */
    public void switchViews(ActionEvent event, String fileLocation) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the components of the addAppointment view.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDTxt.setText(Integer.toString(AppointmentDBImpl.getAppointmentsList().get(AppointmentDBImpl.
                getAppointmentsList().size() - 1).getAppointmentID() + 1));
        contactComBox.setItems(ContactDBImpl.getContactsList());
        startDateDatePicker.setValue(LocalDate.now());
        endDateDatePicker.setValue(LocalDate.now());
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
    }
}
