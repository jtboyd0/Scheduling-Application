package controller;

import DAO.CountryDBImpl;
import DAO.CustomerDAO;
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
import model.Customer;
import model.FirstLvlDiv;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller for the updateCustomer form.
 * @author Jacob Boyd
 */
public class updateCustomerFormController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField customerIDTxt;

    @FXML private TextField nameTxt;

    @FXML private TextField addressTxt;

    @FXML private TextField postalCodeTxt;

    @FXML private TextField phoneNumTxt;

    @FXML private ComboBox<Country> countryComBox;

    @FXML private ComboBox<FirstLvlDiv> stateProvComBox;

    /**
     * cancel switches the view from the update customer form to the main menu without saving any information.
     * @param event fires when the user clicks the cancel button.
     * @throws IOException if the main menu form is missing.
     */
    @FXML void cancel(ActionEvent event) throws IOException {
        switchViews(event, "/view/mainMenuForm1.fxml");
    }

    /**
     * submit attempts to update a customer in the customer table in the database and the customer list. If unsuccessful
     * an error alert with the proper information will be displayed. If successful, the view will be switched to the main
     * menu form.
     * @param event fires when the user clicks the submit button.
     * @throws IOException if the main menu form is missing.
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
            CustomerDAO.updateCustomer(name, address, postalCode, phoneNum, firstLvlDivID, customerID);
            switchViews(event, "/view/mainMenuForm1.fxml");
        }
    }

    /**
     * selectCountry populates the first level divisions combo box with the divisions from the selected country.
     * @param event fires when a country in the country combo box is selected.
     */
    @FXML void selectCountry(ActionEvent event) {
        int countryID = countryComBox.getSelectionModel().getSelectedItem().getCountryID();
        stateProvComBox.setItems(FirstLvlDivDBImpl.filterFirstLvlDivsList(countryID));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * passSelectedCustomer passes the selected customer for initialization purposes.
     * @param customer The customer that is passed.
     */
    public void passSelectedCustomer(Customer customer){

        customerIDTxt.setText("" + customer.getCustomerID());
        nameTxt.setText(customer.getName());
        addressTxt.setText(customer.getAddress());
        postalCodeTxt.setText(customer.getPostalCode());
        phoneNumTxt.setText(customer.getPhoneNum());

        FirstLvlDiv fld = FirstLvlDivDBImpl.getFirstLvlDiv(customer.getDivisionId());
        Country country = CountryDBImpl.getCountry(fld.getCountryID());

        countryComBox.setItems(CountryDBImpl.getCountryList());
        stateProvComBox.setItems(FirstLvlDivDBImpl.filterFirstLvlDivsList(fld.getCountryID()));

        countryComBox.getSelectionModel().select(country);
        stateProvComBox.getSelectionModel().select(fld);
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
}
