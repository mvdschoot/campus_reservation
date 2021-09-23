package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminManageUserView;
import nl.tudelft.oopp.demo.views.AdminUserBikeView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.UserBikeEditDialogView;
import nl.tudelft.oopp.demo.views.UserBikeNewDialogView;


public class AdminUserBikeViewController {

    public static BikeReservation currentSelectedBikeReservation;
    private Logger logger = Logger.getLogger("GlobalLogger");

    @FXML
    private Label usernameLabel;
    @FXML
    private TableView<BikeReservation> userBikeTable;
    @FXML
    private TableColumn<BikeReservation, Number> bikeIdColumn;
    @FXML
    private TableColumn<BikeReservation, String> bikeBuildingColumn;
    @FXML
    private TableColumn<BikeReservation, Number> bikeQuantityColumn;
    @FXML
    private TableColumn<BikeReservation, String> bikeDateColumn;
    @FXML
    private TableColumn<BikeReservation, String> bikeStartingTimeColumn;
    @FXML
    private TableColumn<BikeReservation, String> bikeEndingTimeColumn;
    @FXML
    private Button editBikeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;
    @FXML
    private Button deleteBikeButton;

    public AdminUserBikeViewController() {

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

            final ObservableList<Building> buildingList = Building.getBuildingData();
            usernameLabel.setText(AdminManageUserViewController.currentSelectedUser.getUsername().get());
            // Initialize the bike reservation table with the five columns.
            bikeIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getBikeReservationId().get()));
            // To align the text in this column in a centralized manner; looks better
            bikeIdColumn.setStyle("-fx-alignment: CENTER");
            bikeBuildingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                    buildingList.stream().filter(x -> x.getBuildingId().get()
                            == cellData.getValue().getBikeReservationBuilding().get())
                            .collect(Collectors.toList()).get(0).getBuildingName().get()));
            // To align the text in this column in a centralized manner; looks better
            bikeBuildingColumn.setStyle("-fx-alignment: CENTER");

            bikeQuantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getBikeReservationQuantity().get()));
            // To align the text in this column in a centralized manner; looks better
            bikeQuantityColumn.setStyle("-fx-alignment: CENTER");

            bikeDateColumn.setCellValueFactory(cell -> cell.getValue().getBikeReservationDate());
            // To align the text in this column in a centralized manner; looks better
            bikeDateColumn.setStyle("-fx-alignment: CENTER");

            bikeStartingTimeColumn.setCellValueFactory(cell -> cell.getValue().getBikeReservationStartingTime());
            // To align the text in this column in a centralized manner; looks better
            bikeStartingTimeColumn.setStyle("-fx-alignment: CENTER");

            bikeEndingTimeColumn.setCellValueFactory(cell -> cell.getValue().getBikeReservationEndingTime());
            // To align the text in this column in a centralized manner; looks better
            bikeEndingTimeColumn.setStyle("-fx-alignment: CENTER");

            userBikeTable.setItems(BikeReservation.getUserBikeReservations(
                    AdminManageUserViewController.currentSelectedUser.getUsername().get()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * refresh the table when called.
     */
    public void refresh(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminUserBikeView aubv = new AdminUserBikeView();
        aubv.start(stage);
    }

    /**
     * Called when admin clicks a bike reservation.
     *
     * @return the bike reservation that is currently selected
     */
    public BikeReservation getSelectedBikeReservation() {
        if (userBikeTable.getSelectionModel().getSelectedIndex() >= 0) {
            BikeReservation br = userBikeTable.getSelectionModel().getSelectedItem();
            return br;
        } else {
            return null;
        }
    }

    /**
     * Gets a number representing the index of the selected bike reservation.
     *
     * @return int
     */
    public int getSelectedIndex() {
        return userBikeTable.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a bike reservation.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void deleteBikeClicked(ActionEvent event) {
        BikeReservation selectedBikeReservation = getSelectedBikeReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteBikeLogic(selectedBikeReservation)) {
                    refresh(event);
                    // An alert pop up when a reservation deleted successfully
                    GeneralMethods.alertBox("Delete bike reservation", "",
                            "Bike reservation deleted!", Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Bike reservation deletion failed", Alert.AlertType.WARNING);
                }
            } else {
                // An alert pop up when no bike reservation selected
                GeneralMethods.alertBox("No Selection", "No Bike Reservation Selected",
                        "Please select a bike reservation in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.
     * Opens a dialog to creat a new reservation.
     *
     * @param event is passed
     */
    @FXML
    private void createNewBikeClicked(ActionEvent event) {
        try {
            // Booking edit dialog pop up.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentSelectedBikeReservation = null;
            UserBikeEditDialogController.edit = false;
            UserBikeNewDialogView view = new UserBikeNewDialogView();
            view.start(stage);
            // Get the reservation from the pop up dialog.
            BikeReservation tempBikeReservation = UserBikeEditDialogController.bikeReservation;
            if (tempBikeReservation == null) {
                return;
            }
            if (AdminLogic.creatBikeLogic(tempBikeReservation)) {
                refresh(event);
                // An alert pop up when a new reservation created.
                GeneralMethods.alertBox("New bike reservation", "",
                        "Successfully added new bike reservation!", Alert.AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Bike reservation creation failed", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected bike reservation.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void editBikeClicked(ActionEvent event) {
        BikeReservation selectedBikeReservation = getSelectedBikeReservation();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedBikeReservation = selectedBikeReservation;
                UserBikeEditDialogController.edit = true;
                UserBikeEditDialogView view = new UserBikeEditDialogView();
                view.start(stage);
                BikeReservation tempBikeReservation = UserBikeEditDialogController.bikeReservation;

                if (tempBikeReservation == null) {
                    return;
                }
                if (AdminLogic.editBikeLogic(selectedBikeReservation, tempBikeReservation)) {
                    refresh(event);
                    // Creates an alert box to display the message.
                    GeneralMethods.alertBox("Edit bike reservation", "",
                            "Bike Reservation edited!", Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Edit failed", "",
                            "Bike reservation edit failed", Alert.AlertType.WARNING);
                }
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No Bike Reservation Selected",
                        "Please select a bike reservation in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }


    /**
     * Handles clicking the back button, redirect to the admin home page view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageUserView amuv = new AdminManageUserView();
        amuv.start(stage);
    }

    /**
     * Handles clicking the sign out button, redirect to the log in view.
     *
     * @param event event that triggered this method
     * @throws IOException exception that gets thrown if fails
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        LoginView view = new LoginView();
        view.start(stage);
    }

}
