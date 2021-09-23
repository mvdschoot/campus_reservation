package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.AdminLogic;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminFoodReservationView;
import nl.tudelft.oopp.demo.views.AdminHomePageView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.ReservationEditDialogView;
import nl.tudelft.oopp.demo.views.ReservationNewDialogView;

public class AdminManageReservationViewController {

    public static Reservation currentSelectedReservation;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    /**
     * These are the FXML elements that inject some functionality into the application.
     */
    @FXML
    private TableView<Reservation> listReservations;
    @FXML
    private TableColumn<Reservation, Number> id;
    @FXML
    private TableColumn<Reservation, String> username;
    @FXML
    private TableColumn<Reservation, String> room;
    @FXML
    private TableColumn<Reservation, String> date;
    @FXML
    private TableColumn<Reservation, String> startingTime;
    @FXML
    private TableColumn<Reservation, String> endingTime;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    /**
     * Default constructor of the class.
     */
    public AdminManageReservationViewController() {
    }

    /**
     * To show all the bookings in the table.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            //Initializing all the columns created in the table view to inhibit the data passed
            // down through the server.

            id.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getReservationId().get()));

            username.setCellValueFactory(cell -> cell.getValue().getUsername());
            // To align the text in this column in a centralized manner; looks better
            username.setStyle("-fx-alignment: CENTER");

            room.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(
                    cellData.getValue().getRoom().get())));

            date.setCellValueFactory(cell -> cell.getValue().getDate());
            startingTime.setCellValueFactory(cell -> cell.getValue().getReservationStartingTime());
            endingTime.setCellValueFactory(cell -> cell.getValue().getReservationEndingTime());


            //Adding the Observable List Data to the tableView created.
            listReservations.setItems(Reservation.getAllReservations());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Used to initialize the view everytime something new is created, edited or deleted.
     */
    public void refresh() {
        initialize();
    }

    /**
     * This method selects a particular reservation from the table.
     *
     * @return Returns the selected reservation.
     */
    public Reservation getSelectedReservation() {
        return AdminLogic.getSelectedReservation(listReservations);
    }

    /**
     * The index of the reservation is selected.
     *
     * @return the index of the selected reservation.
     */
    public int getSelectedIndex() {
        return listReservations.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a Reservation.
     */
    @FXML
    public void deleteReservationClicked(ActionEvent event) {
        // To delete a reservation, one of the reservations need to be selected from the tabular view.
        Reservation selectedReservation = getSelectedReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteReservationLogic(selectedReservation)) {
                    // To update the tabular view after removing the reservation.
                    refresh();
                    // Displaying a message to the admin for clearer communication through an alert box.
                    GeneralMethods.alertBox("Delete Reservation", "", "Reservation deleted!",
                            Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Reservation deletion failed", Alert.AlertType.WARNING);
                }
            } else {
                GeneralMethods.alertBox("No Selection", "No Reservation Selected",
                        "Please select a Reservation in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.
     */
    @FXML
    public void newReservationClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentSelectedReservation = null;
            ReservationNewDialogView view = new ReservationNewDialogView();
            view.start(stage);
            Reservation tempReservation = ReservationEditDialogController.reservation;
            //If none of the items in the dialog box is selected.
            if (ReservationEditDialogController.reservation == null) {
                return;

            }
            if (AdminLogic.createReservationLogic(tempReservation)) {
                refresh();
                // Displaying a message to the admin for clearer communication through an alert box.
                GeneralMethods.alertBox("New Reservation", "", "New Reservation created!",
                        Alert.AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Reservation creation failed", Alert.AlertType.WARNING);
            }
            // Displaying a message to the admin for clearer communication through an alert box.
            GeneralMethods.alertBox("New Reservation", "", "New Reservation created!",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles the clicking of Edit button.
     *
     * @param event is passed
     */
    @FXML
    public void editReservationClicked(ActionEvent event) {
        Reservation selectedReservation = getSelectedReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedReservation = selectedReservation;

                ReservationEditDialogView view = new ReservationEditDialogView();
                view.start(stage);
                Reservation tempReservation = ReservationEditDialogController.reservation;

                if (tempReservation == null) {
                    return;
                }
                if (AdminLogic.editReservationLogic(selectedReservation, tempReservation)) {
                    refresh();
                    // Displaying a message to the admin for clearer communication through an alert box.
                    GeneralMethods.alertBox("Edit Reservation", "", "Edited Reservation!",
                            Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Edit failed", "",
                            "Reservation edit failed", Alert.AlertType.WARNING);
                }
            } else {
                // Displaying a message to the admin for clearer communication through an alert box.
                GeneralMethods.alertBox("No Selection", "No Reservation Selected",
                        "Please select a Reservation in the table.", Alert.AlertType.INFORMATION);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * This will redirect the adminManageReservation view back to the home page for the admin to have
     * for options to choose from.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        currentSelectedReservation = null;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This loads up a new admin home page.
        AdminHomePageView ahpv = new AdminHomePageView();
        ahpv.start(stage);
    }

    /**
     * Handles clicking the food button, redirect to the food reservation view.
     *
     * @param event is passed as a parameter.
     * @throws IOException is thrown.
     */
    @FXML
    private void foodReservationClicked(ActionEvent event) throws IOException {
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
     * This button redirects the admin back to the login page.
     *
     * @param event is passed as a parameter.
     * @throws IOException is thrown.
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This loads up a new login page.
        LoginView loginView = new LoginView();
        loginView.start(stage);
    }

    /**
     * This button redirects the admin back to the home page.
     *
     * @param event is passed as a parameter.
     * @throws IOException is thrown.
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This starts a new admin home page view.
        AdminHomePageView ahpv = new AdminHomePageView();
        ahpv.start(stage);
    }
}
