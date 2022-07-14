package controller;

import DAO.CountryDBImpl;
import DAO.CustomerDAO;
import DAO.CustomerDBImpl;
import DAO.FirstLvlDivDBImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.FirstLvlDiv;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller for the addCustomer form.
 * @author Jacob Boyd
 */
public class addCustomerFormController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField nameTxt;
    @FXML private TextField addressTxt;
    @FXML private TextField postalCodeTxt;
    @FXML private TextField phoneNumTxt;
    @FXML private ComboBox<Country> countryComBox;
    @FXML private ComboBox<FirstLvlDiv> stateProvComBox;
    @FXML private TextField customerIDTxt;

    /**
     * cancel changes the view back to the main menu form without saving anything.
     * @param event fires when the user clicks the cancel button.
     * @throws IOException if the main menu form is missing.
     */
    @FXML void cancel(ActionEvent event) throws IOException {
        switchViews(event, "/view/mainMenuForm1.fxml");
    }

    /**
     * submit attempts to save a customer to the database and the customerList.
     * @param event fires when the user clicks the submit button
     * @throws IOException if the main menu form is missing
     */
    @FXML void submit(ActionEvent event) throws IOException {
        String errorString = "";
        int customerID = Integer.parseInt(customerIDTxt.getText());
        String name = "";
        String address = "";
        String postalCode = "";
        String phoneNum = "";
        int firstLvlDivID = 0;


        if(!nameTxt.getText().replaceAll(" ", "").equals("")) {
            name = nameTxt.getText();
        } else {
            errorString += "Please specify a name. \n";
        }
        if (!addressTxt.getText().replaceAll(" ", "").equals("")) {
            address = addressTxt.getText();
        } else {
            errorString += "Please specify an address. \n";
        }
        if (!postalCodeTxt.getText().replaceAll(" ", "").equals("")) {
            postalCode = postalCodeTxt.getText();
        } else {
            errorString += "Please specify a postal code. \n";
        }
        if (!phoneNumTxt.getText().replaceAll(" ", "").equals("")) {
            phoneNum = phoneNumTxt.getText();
        } else {
            errorString += "Please specify a phone #. \n";
        }
        if (!(stateProvComBox.getValue() == null)){
            firstLvlDivID = stateProvComBox.getSelectionModel().getSelectedItem().getDivisionID();
        } else {
            errorString += "Please select a Country/Division for the customer. \n";
        }

        if(!errorString.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText(errorString);
            alert.show();
        } else{
            CustomerDAO.addCustomerDB(customerID, name, address, postalCode, phoneNum, firstLvlDivID);
            switchViews(event, "/view/mainMenuForm1.fxml");
        }
    }

    /**
     * selectCountry changes the selections available in the first level division combo box to
     * divisions in the selected country.
     * @param event fires when the user selects a country in the country combo box.
     */
    @FXML void selectCountry(ActionEvent event) {
        int countryID = countryComBox.getSelectionModel().getSelectedItem().getCountryID();
        stateProvComBox.setItems(FirstLvlDivDBImpl.filterFirstLvlDivsList(countryID));
    }

    /**
     * switchViews switches the view from the current form to a target form.
     * @param event fires when the user clicks the submit(if successful) or cancel buttons.
     * @param fileLocation The target form
     * @throws IOException if the target form is missing/misplaced.
     */
    public void switchViews(ActionEvent event, String fileLocation) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * initialize initializes the values in the addCustomerForm.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComBox.setItems(CountryDBImpl.getCountryList());
        customerIDTxt.setText(Integer.toString(CustomerDBImpl.getCustomerList().get(CustomerDBImpl.getCustomerList().size() - 1).getCustomerID() + 1));
    }
}
