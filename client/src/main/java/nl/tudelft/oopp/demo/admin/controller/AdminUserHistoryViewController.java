package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.AdminLogic;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminFoodReservationView;
import nl.tudelft.oopp.demo.views.AdminManageUserView;
import nl.tudelft.oopp.demo.views.AdminUserHistoryView;
import nl.tudelft.oopp.demo.views.BookingEditDialogView;
import nl.tudelft.oopp.demo.views.LoginView;

public class AdminUserHistoryViewController {

    public static Reservation currentSelectedReservation;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    @FXML
    private Label usernameLabel;
    @FXML
    private TableView<Reservation> bookingTable;
    @FXML
    private TableColumn<Reservation, Number> bookingIdColumn;
    @FXML
    private TableColumn<Reservation, String> bookingDateColumn;
    @FXML
    private TableColumn<Reservation, Number> bookingRoomColumn;
    @FXML
    private TableColumn<Reservation, String> bookingStartColumn;
    @FXML
    private TableColumn<Reservation, String> bookingEndColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    public AdminUserHistoryViewController() {

    }

    /**
     * Show all the booking in the table.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            // Initialize the title of the table
            usernameLabel.setText(AdminManageUserViewController.currentSelectedUser.getUsername().get());
            // Initialize the booking table with the five columns.
            bookingIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getReservationId().get()));
            bookingRoomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getRoom().get()));
            bookingDateColumn.setCellValueFactory(cell -> cell.getValue().getDate());
            bookingStartColumn.setCellValueFactory(cell -> cell.getValue().getReservationStartingTime());
            bookingEndColumn.setCellValueFactory(cell -> cell.getValue().getReservationEndingTime());
            bookingTable.setItems(Reservation.getSelectedUserReservation());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
     * refresh the table when called
     */
    public void refresh(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminUserHistoryView auhv = new AdminUserHistoryView();
        auhv.start(stage);
    }

    /**
     * Called when admin clicks a reservation.
     */
    public Reservation getSelectedReservation() {
        return AdminLogic.getSelectedReservation(bookingTable);
    }

    public int getSelectedIndex() {
        return bookingTable.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a reservation.
     */
    @FXML
    private void deleteBookingClicked(ActionEvent event) {
        Reservation selectedReservation = getSelectedReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteReservationLogic(selectedReservation)) {
                    refresh(event);
                    // An alert pop up when a reservation deleted successfully
                    GeneralMethods.alertBox("Delete Reservation", "", "Reservation deleted!",
                            Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Reservation deletion failed", Alert.AlertType.WARNING);
                }
            } else {
                // An alert pop up when no reservation selected
                GeneralMethods.alertBox("No Selection", "No reservation Selected",
                        "Please select a reservation", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.
     * Opens a dialog to creat a new reservation.
     */
    @FXML
    private void createNewBookingClicked(ActionEvent event) {
        try {
            // Booking edit dialog pop up.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentSelectedReservation = null;
            BookingEditDialogView view = new BookingEditDialogView();
            view.start(stage);
            // Get the reservation from the pop up dialog.
            Reservation tempReservation = BookingEditDialogController.reservation;
            if (tempReservation == null) {
                return;
            }
            //check if the endtime is 24:00 and changes it if so(database can't handle it)
            String temp = tempReservation.getReservationEndingTime().get();
            if (temp.contains("24")) {
                tempReservation.setEndingTime("23:59");
            }
            if (AdminLogic.createReservationLogic(tempReservation)) {
                refresh(event);
                // An alert pop up when a new reservation created.
                GeneralMethods.alertBox("New Reservation", "", "New Reservation added!",
                        Alert.AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Reservation creation failed", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the food button, redirect to the food reservation view.
     *
     * @param event is passed
     */
    @FXML
    private void foodClicked(ActionEvent event) throws IOException {
        Reservation selectedReservation = getSelectedReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedReservation = selectedReservation;

                AdminFoodReservationView afrv = new AdminFoodReservationView();
                afrv.start(stage);
            } else {
                GeneralMethods.alertBox("No Selection", "No Reservation Selected",
                        "Please select a reservation in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the back button, redirect to the user view.
     *
     * @param event is passed
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        currentSelectedReservation = null;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageUserView amuv = new AdminManageUserView();
        amuv.start(stage);
    }

    /**
     * This button redirects the user back to the login page.
     *
     * @param event is passed
     * @throws IOException is thrown.
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This opens up a new login page.
        LoginView loginView = new LoginView();
        loginView.start(stage);
    }


}
