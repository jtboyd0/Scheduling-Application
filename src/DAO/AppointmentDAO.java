package DAO;

import javafx.scene.control.Alert;
import model.Appointment;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * This interface retrieves from and manipulates the appointments table in a database.
 * @author Jacob Boyd
 */
public interface AppointmentDAO {

    /**
     * getAllAppointments retrieves all of the appointments from the appointments table in a database and stores them in
     * the appointments list.
     */
    static void getAllAppointments() {
        try(Connection conn = DBConnection.startConnection()){

            String statement = "SELECT * FROM appointments";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            ResultSet result = ps.getResultSet();
            while(result.next()){
                int appointmentID = result.getInt("Appointment_ID");
                String title = result.getString("Title");
                String description = result.getString("Description");
                String location = result.getString("Location");
                int contactID = result.getInt("Contact_ID");
                String type = result.getString("Type");
                LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();
                int customerID = result.getInt("Customer_ID");
                int userID = result.getInt("User_ID");
                Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type,
                        startDateTime, endDateTime, customerID, userID);
                AppointmentDBImpl.addAppointment(appointment);
            }
            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

    /**
     * deleteAppointment deletes a specified appointment from the appointment table in a database.
     * @param appointment The selected appointment
     */
    static void deleteAppointment(Appointment appointment){
        try(Connection conn = DBConnection.startConnection()){

            int appointmentID = appointment.getAppointmentID();
            String statement = "DELETE FROM appointments WHERE Appointment_ID = ?";
            DBQuery.setPreparedStatementKeys(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setInt(1, appointmentID);
            ps.execute();

            AppointmentDBImpl.deleteAppointment(appointment);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Appointment Deleted");
            alert.setContentText("Appointment with ID of " + appointmentID + " and of type " + appointment.getType() + " has been deleted.");
            alert.show();

        }catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

    /**
     * addAppointment adds an appointment to the appointment table in a database.
     * @param appointmentID The appointment ID
     * @param title The title
     * @param description The description
     * @param location The location
     * @param type The type
     * @param startDate The start date
     * @param startTime The start time
     * @param endDate The end date
     * @param endTime The end time
     * @param userID The user ID
     * @param customerID The customer ID
     * @param contactID The contact ID
     */
    static void addAppointment(int appointmentID, String title, String description, String location, String type,
                               LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime,
                               int userID, int customerID, int contactID) {
        try(Connection conn = DBConnection.startConnection()){

            String statement = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            DBQuery.setPreparedStatement(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            String userName = UserDB.getCurrentUser().getUserName();
            ps.setInt(1, appointmentID);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5,type);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.of(startDate,startTime)));
            ps.setTimestamp(7,Timestamp.valueOf(LocalDateTime.of(endDate, endTime)));
            ps.setString(8, userName);
            ps.setString(9, userName);
            ps.setInt(10, customerID);
            ps.setInt(11,userID);
            ps.setInt(12, contactID);
            ps.execute();

            Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type,
                    LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime), customerID,
                    userID);
            AppointmentDBImpl.addAppointment(appointment);

            DBConnection.closeConnection();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }

    /**
     * updateAppointment updates a specific row in the appointments table.
     * @param appointmentID The appointment ID
     * @param title The title
     * @param description The description
     * @param location The location
     * @param type The type
     * @param startDate The start date
     * @param startTime The start time
     * @param endDate The end date
     * @param endTime The end time
     * @param userID The user ID
     * @param customerID The customer ID
     * @param contactID The contact ID
     */
    static void updateAppointment(int appointmentID, String title, String description, String location, String type,
                                  LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime,
                                  int userID, int customerID, int contactID) {
        try(Connection conn = DBConnection.startConnection()){

            String userName = UserDB.getCurrentUser().getUserName();
            String statement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?," +
                    " End = ?, Last_Update = current_timestamp(), Last_Updated_By = ?, Customer_ID = ?," +
                    " User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            DBQuery.setPreparedStatementKeys(conn, statement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.of(startDate, startTime)));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.of(endDate, endTime)));
            ps.setString(7, userName);
            ps.setInt(8, customerID);
            ps.setInt(9, userID);
            ps.setInt(10, contactID);
            ps.setInt(11, appointmentID);
            ps.execute();

            AppointmentDBImpl.updateAppointment(appointmentID, title, description, location, contactID, type,
                    LocalDateTime.of(startDate,startTime), LocalDateTime.of(endDate, endTime), customerID, userID);

            DBConnection.closeConnection();

        }catch(SQLException e){
            System.out.println(e.getMessage());
            DBConnection.closeConnection();
        }
    }
}
