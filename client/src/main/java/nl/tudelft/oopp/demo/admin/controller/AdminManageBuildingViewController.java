package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.AdminLogic;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminHomePageView;
import nl.tudelft.oopp.demo.views.AdminManageBuildingView;
import nl.tudelft.oopp.demo.views.BuildingEditDialogView;
import nl.tudelft.oopp.demo.views.BuildingNewDialogView;
import nl.tudelft.oopp.demo.views.LoginView;


public class AdminManageBuildingViewController {

    public static Building currentSelectedBuilding;
    private static Logger logger = Logger.getLogger("GlobalLogger");

    @FXML
    private TableView<Building> buildingTable;
    @FXML
    private TableColumn<Building, Number> buildingIdColumn;
    @FXML
    private TableColumn<Building, String> buildingNameColumn;
    @FXML
    private TableColumn<Building, Number> buildingRoomCountColumn;
    @FXML
    private TableColumn<Building, String> maxBikesColumn;
    @FXML
    private TableColumn<Building, String> openingTimeColumn;
    @FXML
    private TableColumn<Building, String> closingTimeColumn;
    @FXML
    private TableColumn<Building, String> buildingAddressColumn;
    @FXML
    private Button signOutButton;
    @FXML
    private Button backButton;

    public AdminManageBuildingViewController() {
    }

    /**
     * Show all the buildings in the left side table.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");

            // Initialize the room table with the four columns.
            buildingIdColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(
                    cell.getValue().getBuildingId().get()));
            buildingNameColumn.setCellValueFactory(cell -> cell.getValue().getBuildingName());
            buildingRoomCountColumn.setCellValueFactory(cell ->
                    new SimpleIntegerProperty(cell.getValue().getBuildingRoomCount().get()));
            // To align the text in this column in a centralized manner; looks better
            buildingRoomCountColumn.setStyle("-fx-alignment: CENTER");

            maxBikesColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(String.valueOf(cell.getValue().getBuildingMaxBikes().get())));
            // To align the text in this column in a centralized manner; looks better
            maxBikesColumn.setStyle("-fx-alignment: CENTER");

            openingTimeColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                    cell.getValue().getOpeningTime().get()));
            // To align the text in this column in a centralized manner; looks better
            openingTimeColumn.setStyle("-fx-alignment: CENTER");

            closingTimeColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                    cell.getValue().getClosingTime().get()));
            // To align the text in this column in a centralized manner; looks better
            closingTimeColumn.setStyle("-fx-alignment: CENTER");

            buildingAddressColumn.setCellValueFactory(cell -> cell.getValue().getBuildingAddress());

            // Add observable list data to the table
            buildingTable.setItems(Building.getBuildingData());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Reloads the whole view with new information.
     * @param event event to get stage from
     * @throws IOException if errors occur due to I/O
     */
    public void refresh(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminManageBuildingView ambv = new AdminManageBuildingView();
        ambv.start(stage);
    }

    /**
     * Gets the currently selected building.
     *
     * @return a Building object
     */
    public Building getSelectedBuilding() {
        if (buildingTable.getSelectionModel().getSelectedIndex() >= 0) {
            return buildingTable.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }
    }

    /**
     * Gets a number representing the index of the selected building.
     *
     * @return int
     */
    public int getSelectedIndex() {
        return buildingTable.getSelectionModel().getSelectedIndex();
    }


    /**
     * Delete a building.
     */
    @FXML
    private void deleteBuildingClicked(ActionEvent event) {
        Building selectedBuilding = getSelectedBuilding();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteBuildingLogic(selectedBuilding)) {
                    refresh(event);
                    // Create an alert box.
                    GeneralMethods.alertBox("Delete Building", "", "Building deleted!",
                            AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Building deletion failed", AlertType.WARNING);
                }
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("No Selection", "No Building Selected", "Please"
                        + " select a building in the table", AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.
     */
    @FXML
    private void createNewBuildingClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentSelectedBuilding = null;
            BuildingNewDialogView view = new BuildingNewDialogView();
            view.start(stage);
            Building tempBuilding = BuildingEditDialogController.building;
            if (tempBuilding == null) {
                return;
            }
            if (AdminLogic.createBuildingLogic(tempBuilding)) {
                refresh(event);
                // Create an alert box.
                GeneralMethods.alertBox("New Building", "", "Added new building!",
                        AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Building creation failed", AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected building.
     */
    @FXML
    private void editBuildingClicked(ActionEvent event) {
        Building selectedBuilding = getSelectedBuilding();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedBuilding = selectedBuilding;

                BuildingEditDialogView view = new BuildingEditDialogView();
                view.start(stage);
                Building tempBuilding = BuildingEditDialogController.building;

                if (tempBuilding == null) {
                    return;
                }
                if (AdminLogic.editBuildingLogic(selectedBuilding, tempBuilding)) {
                    refresh(event);
                    // Create an alert box.
                    GeneralMethods.alertBox("Edit Building", "", "Edited building!",
                            AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Edit failed", "",
                            "Building edit failed", AlertType.WARNING);
                }
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("No Selection", "No Building Selected",
                        "Please select a building in the table.", AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Back button is clicked which redirects the admin back to admin home page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This loads up a new admin home page.
        AdminHomePageView ahpv = new AdminHomePageView();
        ahpv.start(stage);
    }

    /**
     * This button redirects to the admin back to the login page.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //Starts a new login page.
        LoginView loginView = new LoginView();
        loginView.start(stage);
    }

}
