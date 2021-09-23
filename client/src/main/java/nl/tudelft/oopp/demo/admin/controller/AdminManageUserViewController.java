package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminHomePageView;
import nl.tudelft.oopp.demo.views.AdminManageUserView;
import nl.tudelft.oopp.demo.views.AdminUserBikeView;
import nl.tudelft.oopp.demo.views.AdminUserHistoryView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.UserEditDialogView;
import nl.tudelft.oopp.demo.views.UserNewDialogView;



public class AdminManageUserViewController {

    public static User currentSelectedUser;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> userTypeColumn;
    @FXML
    private TableColumn<User, String> userPasswordColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    public AdminManageUserViewController() {
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
            usernameColumn.setCellValueFactory(cell -> cell.getValue().getUsername());
            List<String> availableUserType = Arrays.asList("Admin", "Teacher", "Student");
            userTypeColumn.setCellValueFactory(cell -> new SimpleStringProperty(availableUserType.get(
                    cell.getValue().getUserType().get())));
            userPasswordColumn.setCellValueFactory(cell -> cell.getValue().getUserPassword());
            // Add observable list data to the table
            userTable.setItems(User.getUserData());
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
        AdminManageUserView amuv = new AdminManageUserView();
        amuv.start(stage);
    }

    /**
     * The user from the table view is selected.
     *
     * @return User selected.
     */
    public User getSelectedUser() {
        return AdminLogic.getSelectedUser(userTable);
    }

    public int getSelectedIndex() {
        return userTable.getSelectionModel().getSelectedIndex();
    }

    /**
     * Delete a building.
     */
    @FXML
    private void deleteUserClicked(ActionEvent event) {
        User selectedUser = getSelectedUser();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteUserLogic(selectedUser)) {
                    refresh(event);
                    // Creates an alert box for transparent communication.
                    GeneralMethods.alertBox("Delete user", "", "User deleted!", Alert.AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "User deletion failed", Alert.AlertType.WARNING);
                }
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No User Selected",
                        "Please select a User in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles clicking the create new button.
     */
    @FXML
    private void createNewUserClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentSelectedUser = null;
            UserEditDialogController.edit = false;
            UserNewDialogView view = new UserNewDialogView();
            view.start(stage);
            User tempUser = UserEditDialogController.user;
            if (tempUser == null) {
                return;
            }
            if (AdminLogic.createUserLogic(tempUser)) {
                refresh(event);
                // Informing the admin through a alert box that a new user is created successfully.
                GeneralMethods.alertBox("New user", "", "New User created!", Alert.AlertType.INFORMATION);
            } else {
                // Create an alert box.
                GeneralMethods.alertBox("Creation failed", "",
                        "User creation failed", Alert.AlertType.WARNING);
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
    private void editUserClicked(ActionEvent event) {
        User selectedUser = getSelectedUser();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedUser = selectedUser;

                UserEditDialogController.edit = true;
                UserEditDialogView view = new UserEditDialogView();
                view.start(stage);
                User tempUser = UserEditDialogController.user;



                if (tempUser == null) {
                    return;
                }
                if (tempUser.getUserPassword().get().equals("")) {
                    if (AdminLogic.editUserLogicWithoutPassword(tempUser)) {
                        // Creates an alert box for transparent communication.
                        GeneralMethods.alertBox("Edit user", "", "User edited!", Alert.AlertType.INFORMATION);
                        refresh(event);
                    } else {
                        // Create an alert box.
                        GeneralMethods.alertBox("Edit failed", "",
                                "User edit failed", Alert.AlertType.WARNING);
                    }
                } else {
                    if (AdminLogic.editUserLogic(tempUser)) {
                        // Creates an alert box for transparent communication.
                        GeneralMethods.alertBox("Edit user", "", "User edited!", Alert.AlertType.INFORMATION);
                        refresh(event);
                    } else {
                        // Create an alert box.
                        GeneralMethods.alertBox("Edit failed", "",
                                "User edit failed", Alert.AlertType.WARNING);
                    }
                }
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No User Selected",
                        "Please select an User in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * The history of the particular user is displayed.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    private void historyClicked(ActionEvent event) throws IOException {
        User selectedUser = getSelectedUser();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedUser = selectedUser;

                AdminUserHistoryView auhv = new AdminUserHistoryView();
                auhv.start(stage);
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No User Selected",
                        "Please select a User in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Bike button redirects the admin to the bike reservation page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    private void bikeClicked(ActionEvent event) throws IOException {
        User selectedUser = getSelectedUser();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentSelectedUser = selectedUser;

                AdminUserBikeView auhv = new AdminUserBikeView();
                auhv.start(stage);
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No User Selected",
                        "Please select a User in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Back button redirects the admin back to the admin home page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This starts a new admin home page view.
        AdminHomePageView ahpv = new AdminHomePageView();
        ahpv.start(stage);
    }

    /**
     * This button redirects the user back to the login page.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This loads up a new login page.
        LoginView loginView = new LoginView();
        loginView.start(stage);
    }

}
