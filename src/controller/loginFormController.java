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
import utils.Log;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is the controller for the loginForm
 * @author Jake
 */
public class loginFormController implements Initializable{

    Stage stage;
    Parent scene;

    @FXML private Button submitBtn;
    @FXML private TextField usernameTxt;
    @FXML private PasswordField passwordTxt;
    @FXML private Label errorLbl;
    @FXML private Label passwordLbl;
    @FXML private Label usernameLbl;
    @FXML private Label locationLbl;
    @FXML private Label locationTagLbl;

    /**
     * submitCredentials logs the user into the application.
     * @param event fires when the user clicks the submit button.
     * @throws IOException If the main menu form is missing.
     */
    @FXML void submitCredentials(ActionEvent event) throws IOException {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        if(UserDB.getUser(username, password)){
            UserDB.getAllUsers();
            CountryDAO.getAllCountries();
            FirstLvlDivDAO.getAllFirstLvlDivs();
            ContactDAO.getAllContacts();
            CustomerDAO.getAllCustomers();
            AppointmentDAO.getAllAppointments();

            String meetingSoon = AppointmentDBImpl.meetingSoon();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if(!meetingSoon.equals("")){
                alert.setHeaderText("Meeting Soon!");
                alert.setContentText(meetingSoon);
            } else {
                alert.setHeaderText("No meeting soon.");
                alert.setContentText("Have a nice day! :)");
            }
            alert.show();

            switchViews(event, "/view/mainMenuForm1.fxml");
            Log.trackLogIn(LocalDateTime.now().toString(), true);
        } else {
            errorLbl.setVisible(true);
            Log.trackLogIn(LocalDateTime.now().toString(), false);
        }
    }

    /**
     * initialize initializes the values in the login form.
     * @param url The url
     * @param rb The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        ZoneId zoneId = ZoneId.systemDefault();
        if(Locale.getDefault().getLanguage().equals("fr")){
            rb = ResourceBundle.getBundle("resources/lang", Locale.getDefault());
            usernameLbl.setText(rb.getString("Username"));
            passwordLbl.setText(rb.getString("Password"));
            errorLbl.setText(rb.getString("Error"));
            submitBtn.setText(rb.getString("Submit"));
            
            locationTagLbl.setText(rb.getString("Location"));
        }
        locationLbl.setText(zoneId.toString());
        errorLbl.setVisible(false);
    }

    /**
     * switchViews changes the view from the current form to a target form
     * @param event fires when the user clicks submit and is successful
     * @param fileLocation The file path
     * @throws IOException if the main menu form is missing.
     */
    public void switchViews(ActionEvent event, String fileLocation) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
