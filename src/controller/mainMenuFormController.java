package controller;

import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Country;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This form is the controller for the main menu
 * @author Jacob Boyd
 */
public class mainMenuFormController implements Initializable {

    Stage stage;
    Parent scene;
    XYChart.Series series = new XYChart.Series<>();

    @FXML private TableView<Appointment> appointmentTblView;
    @FXML private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, String> contactCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol;
    @FXML private TableColumn<Appointment, Integer> customerIDAppCol;
    @FXML private RadioButton monthRadioBtn;
    @FXML private RadioButton weekRadioBtn;
    @FXML private ToggleGroup sortBy;
    @FXML private RadioButton allAppsRadioBtn;

    @FXML private TableView<Customer> customerTblView;
    @FXML private TableColumn<Customer, Integer> customerIDCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> customerPostCol;
    @FXML private TableColumn<Customer, String> customerPhoneCol;
    @FXML private TableColumn<Country, String> countryCol;
    @FXML private TableColumn<Customer, String> divisionCol;

    @FXML private TableView<Appointment> contactScheduleTblView;
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol1;
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol1;
    @FXML private TableColumn<Appointment, Integer> appointmentIDCol1;
    @FXML private TableColumn<Appointment, Integer> customerIDAppCol1;
    @FXML private TableColumn<Appointment, String> titleCol1;
    @FXML private TableColumn<Appointment, String> descriptionCol1;
    @FXML private TableColumn<Appointment, String> typeCol1;
    @FXML private ComboBox<Contact> contactComBox;

    @FXML private TableView<Appointment> customerScheduleTblView;
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol2;
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol2;
    @FXML private TableColumn<Appointment, Integer> appointmentIDCol2;
    @FXML private TableColumn<Appointment, String> contactCol2;
    @FXML private TableColumn<Appointment, String> titleCol2;
    @FXML private TableColumn<Appointment, String> descriptionCol2;
    @FXML private TableColumn<Appointment, String> typeCol2;
    @FXML private ComboBox<Integer> customerComBox;

    @FXML private ComboBox<String> appTypeComBox;
    @FXML private BarChart<String, Integer> appBarGraph;
    @FXML private CategoryAxis monthsAxis;
    @FXML private NumberAxis numericalAxis;

    /**
     * createAppointment switches the view to the add appointment form.
     * @param event fires if the add appointment button is clicked.
     * @throws IOException if the add appointment form is missing.
     */
    @FXML void createAppointment(ActionEvent event) throws IOException {
        switchViews(event,"/view/addAppointment.fxml");
    }

    /**
     * deleteAppointment attempts to delete an appointment from the database and the list of appointments. If it is not
     * successful, a proper error message will be displayed.
     * @param event fires when the user clicks the delete button.
     */
    @FXML void deleteAppointment(ActionEvent event) {
        if(appointmentTblView.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Construction in Progress...");
            alert.setContentText("Please select an appointment to delete.");
            alert.show();
        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Please select an option.");
            alert.setContentText("Are you sure you want to delete this appointment?");

            ButtonType confirm = new ButtonType("Confirm");
            ButtonType cancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(confirm, cancel);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == confirm){
                AppointmentDAO.deleteAppointment(appointmentTblView.getSelectionModel().getSelectedItem());

                if(allAppsRadioBtn.isSelected())
                    appointmentTblView.setItems(AppointmentDBImpl.getAppointmentsList());
                else if (monthRadioBtn.isSelected())
                    appointmentTblView.setItems(AppointmentDBImpl.getAppointmentListByMonth());
                else
                    appointmentTblView.setItems(AppointmentDBImpl.getAppointmentListByWeek());

                appBarGraph.getData().clear();
                appTypeComBox.setItems(AppointmentDBImpl.getTypeList());

            } else if(result.get() == cancel){
                alert.close();
            }

        }
    }

    /**
     * updateAppointment changes the view to the update appointment form if an appointment is selected. If unsuccessful
     * an error message will be displayed.
     * @param event fires if the update appointment button is clicked.
     * @throws IOException if the update appointment form is missing.
     */
    @FXML void updateAppointment(ActionEvent event) throws IOException {
        if(appointmentTblView.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Construction in Progress...");
            alert.setContentText("Please select an appointment to update.");
            alert.show();
        } else {
            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("/view/updateAppointment.fxml"));
            Parent updateAppointmentParent = Loader.load();
            Scene updateAppointmentScene = new Scene(updateAppointmentParent);
            updateAppointmentController controller = Loader.getController();
            controller.passSelectedAppointment(appointmentTblView.getSelectionModel().getSelectedItem());
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(updateAppointmentScene);
            window.show();
        }
    }

    /**
     * createCustomer changes the view to the add customer form.
     * @param event fires when the user clicks the add customer button.
     * @throws IOException if the add customer form is missing.
     */
    @FXML void createCustomer(ActionEvent event) throws IOException {
        switchViews(event, "/view/addCustomerForm.fxml");
    }

    /**
     * deleteCustomer attempts to delete a customer from the database and the customerList, if it is unsuccessful an
     * error will be shown to the user.
     * @param event fires when the user clicks the delete customer button.
     */
    @FXML void deleteCustomer(ActionEvent event) {

        if(customerTblView.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Construction in Progress...");
            alert.setContentText("Please select a customer to delete.");
            alert.show();
        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Please select an option.");
            alert.setContentText("Are you sure you want to delete this customer?");

            ButtonType confirm = new ButtonType("Confirm");
            ButtonType cancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(confirm, cancel);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == confirm){
                CustomerDAO.deleteCustomerDB(customerTblView.getSelectionModel().getSelectedItem());
                customerComBox.getItems().clear();
                for(Customer c : CustomerDBImpl.getCustomerList()){
                    customerComBox.getItems().add(c.getCustomerID());
                }
                if(monthRadioBtn.isSelected()){
                    sortByMonth(event);
                } else if(weekRadioBtn.isSelected()){
                    sortByWeek(event);
                } else{
                    sortByAll(event);
                }
            } else if(result.get() == cancel){
                alert.close();
            }
        }
    }

    /**
     * updateCustomer will change to the updateCustomer form if a customer is selected to be updated.
     * @param event fires when the user clicks the update customer button.
     * @throws IOException if the update customer form is missing.
     */
    @FXML void updateCustomer(ActionEvent event) throws IOException {
        if(customerTblView.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Construction in Progress...");
            alert.setContentText("Please select a customer to update");
            alert.show();
        } else {
            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("/view/updateCustomerForm.fxml"));
            Parent updateCustomerFormParent = Loader.load();
            Scene updateCustomerFormScene = new Scene(updateCustomerFormParent);
            updateCustomerFormController controller = Loader.getController();
            controller.passSelectedCustomer(customerTblView.getSelectionModel().getSelectedItem());
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(updateCustomerFormScene);
            window.show();
        }
    }

    /**
     * sortByMonth displays only the appointments during the current month.
     * @param event fires when the user clicks the sort by month radio button.
     */
    @FXML
    void sortByMonth(ActionEvent event) {
        appointmentTblView.setItems(AppointmentDBImpl.getAppointmentListByMonth());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        customerIDAppCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        customerTblView.setItems(CustomerDBImpl.getCustomerList());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    }

    /**
     * sortByWeek will display only the appointments on the current day and the next 6 days.
     * @param event fires when the user clicks the sort by week radio button.
     */
    @FXML
    void sortByWeek(ActionEvent event) {
        appointmentTblView.setItems(AppointmentDBImpl.getAppointmentListByWeek());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        customerIDAppCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        customerTblView.setItems(CustomerDBImpl.getCustomerList());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    }

    /**
     * sortByAll displays all of the appointments that are in the appointmentsList.
     * @param event fires when the user clicks the sort by all radio button.
     */
    @FXML
    void sortByAll(ActionEvent event) {
        appointmentTblView.setItems(AppointmentDBImpl.getAppointmentsList());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        customerIDAppCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        customerTblView.setItems(CustomerDBImpl.getCustomerList());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    }

    /**
     * selectContact displays the contact's schedule on the contact schedule report tab when a contact is selected.
     * @param event fires when a contact is selected from the contact combo box.
     */
    @FXML void selectContact(ActionEvent event) {
        contactScheduleTblView.setItems(AppointmentDBImpl.getContactSchedule(contactComBox.getSelectionModel().getSelectedItem()));
        startCol1.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol1.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        appointmentIDCol1.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        customerIDAppCol1.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        titleCol1.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol1.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol1.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    /**
     * selectCustomer displays the customer's schedule on the customer schedule report tab when a customer is selected.
     * @param event fires when a customer is selected in the customer combo box.
     */
    @FXML
    void selectCustomer(ActionEvent event) {
        customerScheduleTblView.setItems(AppointmentDBImpl.getCustomerSchedule(customerComBox.getSelectionModel().getSelectedItem()));
        startCol2.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol2.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        appointmentIDCol2.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        contactCol2.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        titleCol2.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol2.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol2.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    /**
     * selectType displays the number of the selected type of appointment by month in the bar chart on the
     * type of appointment report tab.
     * @param event fires when a type of appointment is selected.
     */
    @FXML void selectType(ActionEvent event) {
        appBarGraph.getData().clear();
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();

        series.getData().add(new XYChart.Data<String, Integer>("Jan", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 1)));
        series.getData().add(new XYChart.Data<String, Integer>("Feb", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 2)));
        series.getData().add(new XYChart.Data<String, Integer>("Mar", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 3)));
        series.getData().add(new XYChart.Data<String, Integer>("Apr", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 4)));
        series.getData().add(new XYChart.Data<String, Integer>("May", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 5)));
        series.getData().add(new XYChart.Data<String, Integer>("Jun", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 6)));
        series.getData().add(new XYChart.Data<String, Integer>("Jul", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 7)));
        series.getData().add(new XYChart.Data<String, Integer>("Aug", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 8)));
        series.getData().add(new XYChart.Data<String, Integer>("Sep", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 9)));
        series.getData().add(new XYChart.Data<String, Integer>("Oct", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 10)));
        series.getData().add(new XYChart.Data<String, Integer>("Nov", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 11)));
        series.getData().add(new XYChart.Data<String, Integer>("Dec", AppointmentDBImpl.getNumberOfAppsByMonth(appTypeComBox.getSelectionModel().getSelectedItem(), 12)));
        int max = series.getData().get(0).getYValue();
        for(int x = 1; x < series.getData().size(); x++){
            if(series.getData().get(x).getYValue() > max){
                max = series.getData().get(x).getYValue();
            }
        }
        numericalAxis.setUpperBound(max + 5);
        appBarGraph.getData().add(series);
    }

    /**
     * initialize initializes all of the values in the main menu form.
     * @param url The url
     * @param resourceBundle The resource bundle
     */
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentTblView.setItems(AppointmentDBImpl.getAppointmentsList());

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("displayStartDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("displayEndDateTime"));
        customerIDAppCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        customerTblView.setItems(CustomerDBImpl.getCustomerList());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        appTypeComBox.setItems(AppointmentDBImpl.getTypeList());
        ObservableList<String> months = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        monthsAxis.setCategories(months);

        contactComBox.setItems(ContactDBImpl.getContactsList());
        for(Customer c : CustomerDBImpl.getCustomerList()){
            customerComBox.getItems().add(c.getCustomerID());
        }
    }

    /**
     * switchViews changes the view from the current form to the target form.
     * @param event fires when a button that changes the view is clicked.
     * @param fileLocation The file path
     * @throws IOException if the target form is missing.
     */
    public void switchViews(ActionEvent event, String fileLocation) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
