package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
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
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminManageFoodView;
import nl.tudelft.oopp.demo.views.FoodBuildingEditDialogView;
import nl.tudelft.oopp.demo.views.LoginView;


public class AdminManageFoodBuildingViewController {

    private static Logger logger = Logger.getLogger("GlobalLogger");
    public static Building currentSelectedFoodBuilding;
    @FXML
    private Label foodNameLabel;
    @FXML
    private TableView<Building> foodBuildingTable;
    @FXML
    private TableColumn<Building, String> foodBuildingIdColumn;
    @FXML
    private TableColumn<Building, String> foodBuildingNameColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    public AdminManageFoodBuildingViewController() {
    }

    /**
     * Show all the buildings offers this type of food in the table.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            // Initialize the title of the table
            foodNameLabel.setText(AdminManageFoodViewController.currentSelectedFood.getFoodName().get());
            // Initialize the room table with the two columns.
            foodBuildingIdColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                    String.valueOf(cell.getValue().getBuildingId().get())));
            // To align the text in this column in a centralized manner; looks better
            foodBuildingIdColumn.setStyle("-fx-alignment: CENTER");

            foodBuildingNameColumn.setCellValueFactory(cell -> cell.getValue().getBuildingName());
            // To align the text in this column in a centralized manner; looks better
            foodBuildingNameColumn.setStyle("-fx-alignment: CENTER");

            // Add observable list data to the table
            foodBuildingTable.setItems(Building.getBuildingByFoodId(
                    AdminManageFoodViewController.currentSelectedFood.getFoodId().get()));
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
     * Called when a building is selected.
     *
     * @return Building
     */
    public Building getSelectedBuilding() {
        if (foodBuildingTable.getSelectionModel().getSelectedIndex() >= 0) {
            return foodBuildingTable.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }
    }

    /**
     * Gets a number representing the index of the selected food.
     *
     * @return int
     */
    public int getSelectedIndex() {
        return foodBuildingTable.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a building.
     *
     * @param event is passed
     */
    @FXML
    private void deleteFoodBuildingClicked(ActionEvent event) {
        //Get the selected building
        Building selectedBuilding = getSelectedBuilding();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteFoodReservationLogic(selectedBuilding)) {
                    refresh();
                    // An alert pop up when a building deleted successfully
                    GeneralMethods.alertBox("Delete building", "",
                            "Building deleted!", Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Food deletion failed", Alert.AlertType.WARNING);
                }
            } else {
                // An alert pop up when no building selected
                GeneralMethods.alertBox("No Selection", "No Building Selected",
                        "Please select a building in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the add button.
     * Opens a dialog to edit details for the new food.
     *
     * @param event is passed
     */
    @FXML
    private void addFoodBuildingClicked(ActionEvent event) {
        try {
            // Building edit dialog pop up.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentSelectedFoodBuilding = null;
            // Get the building from the pop up dialog.
            FoodBuildingEditDialogView view = new FoodBuildingEditDialogView();
            view.start(stage);
            Building tempBuilding = FoodBuildingEditDialogController.building;
            if (tempBuilding == null) {
                return;
            }
            if (AdminLogic.addFoodReservationLogic(tempBuilding)) {
                refresh();
                // An alert pop up when a new building added.
                GeneralMethods.alertBox("New building", "",
                        "New building added!", Alert.AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Building creation failed", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the back button, redirect to the food view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageFoodView amfv = new AdminManageFoodView();
        amfv.start(stage);
    }

    /**
     * Handles clicking the login button, redirect to the login view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        LoginView view = new LoginView();
        view.start(stage);
    }
}
