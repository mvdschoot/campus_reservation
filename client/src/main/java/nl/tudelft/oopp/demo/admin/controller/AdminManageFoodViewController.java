package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.AdminLogic;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminFoodBuildingView;
import nl.tudelft.oopp.demo.views.AdminHomePageView;
import nl.tudelft.oopp.demo.views.AdminManageFoodView;
import nl.tudelft.oopp.demo.views.FoodEditDialogView;
import nl.tudelft.oopp.demo.views.FoodNewDialogView;
import nl.tudelft.oopp.demo.views.LoginView;



public class AdminManageFoodViewController {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    public static Food currentSelectedFood;
    @FXML
    private TableView<Food> foodTable;
    @FXML
    private TableColumn<Food, Number> foodIdColumn;
    @FXML
    private TableColumn<Food, String> foodNameColumn;
    @FXML
    private TableColumn<Food, Number> foodPriceColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    public AdminManageFoodViewController() {
    }

    /**
     * Show all the food in the table.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            // Initialize the food table with the three columns.
            foodIdColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getFoodId().get()));
            // To align the text in this column in a centralized manner; looks better
            foodIdColumn.setStyle("-fx-alignment: CENTER");

            foodNameColumn.setCellValueFactory(cell -> cell.getValue().getFoodName());
            // To align the text in this column in a centralized manner; looks better
            foodNameColumn.setStyle("-fx-alignment: CENTER");

            foodPriceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(

                    (double) Math.round((cell.getValue().getFoodPrice().get()) * 100) / 100));
            // Add observable list data to the table
            foodTable.setItems(Food.getAllFoodData());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Used to initialize the view everytime something new is created, edited or deleted.
     */
    public void refresh(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminManageFoodView amfv = new AdminManageFoodView();
        amfv.start(stage);
    }

    /**
     * Called when admin clicks a food.
     *
     * @return a Food object
     */
    public Food getSelectedFood() {
        return AdminLogic.getSelectedFood(foodTable);
    }

    /**
     * Gets a number representing the index of the selected food.
     *
     * @return int
     */
    public int getSelectedIndex() {
        return foodTable.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a food.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void deleteFoodClicked(ActionEvent event) {
        Food selectedFood = getSelectedFood();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteFoodLogic(selectedFood)) {
                    refresh(event);
                    // An alert pop up when a food deleted successfully
                    GeneralMethods.alertBox("Delete food", "",
                            "Food deleted!", AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Food deletion failed", AlertType.WARNING);
                }
            } else {
                // An alert pop up when no food selected
                GeneralMethods.alertBox("No Selection", "No Food Selected",
                        "Please select a food in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.Opens a dialog to edit
     * details for the new food.
     *
     * @param event is passed
     */
    @FXML
    private void createNewFoodClicked(ActionEvent event) {
        try {
            // Food edit dialog pop up.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentSelectedFood = null;
            FoodNewDialogView view = new FoodNewDialogView();
            view.start(stage);
            // Get the food from the pop up dialog.
            Food tempFood = FoodEditDialogController.food;
            if (tempFood == null) {
                return;
            }
            if (AdminLogic.addFoodLogic(tempFood)) {
                refresh(event);
                // An alert pop up when a new food created.
                GeneralMethods.alertBox("New food", "", "New Food added!", AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "Food creation failed", AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected food.
     *
     * @param event is passed
     */
    @FXML
    private void editFoodClicked(ActionEvent event) {
        Food selectedFood = getSelectedFood();
        int selectedIndex = getSelectedIndex();
        try {
            // Food edit dialog pop up.
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedFood = selectedFood;
                FoodEditDialogView view = new FoodEditDialogView();
                view.start(stage);
                // Get the food from the pop up dialog.
                Food tempFood = FoodEditDialogController.food;
                if (tempFood == null) {
                    return;
                }
                if (AdminLogic.updateFoodLogic(selectedFood, tempFood)) {
                    refresh(event);
                    // An alert pop up when a new food created.
                    GeneralMethods.alertBox("Edit food", "", "Food edited!", AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Edit failed", "",
                            "Food edit failed", AlertType.WARNING);
                }
            } else {
                // An alert pop up when no food selected
                GeneralMethods.alertBox("No Selection", "No Food Selected",
                        "Please select a food in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Show all the buildings which provide this food in a table.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    @FXML
    private void foodBuildingClicked(ActionEvent event) throws IOException {
        Food selectedFood = getSelectedFood();
        int selectedIndex = getSelectedIndex();
        try {
            // Switch to the building table of selected food
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedFood = selectedFood;
                FoodBuildingEditDialogController.selectedFood = selectedFood;

                AdminFoodBuildingView afbv = new AdminFoodBuildingView();
                afbv.start(stage);
            } else {
                // An alert pop up when no food selected
                GeneralMethods.alertBox("No Selection", "No Food Selected",
                        "Please select a food in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the back button, redirect to the admin home page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminHomePageView ahpv = new AdminHomePageView();
        ahpv.start(stage);
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
