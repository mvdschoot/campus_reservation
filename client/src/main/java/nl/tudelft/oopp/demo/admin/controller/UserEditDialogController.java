package nl.tudelft.oopp.demo.admin.controller;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.UserEditDialogLogic;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.general.GeneralMethods;


public class UserEditDialogController {

    public static boolean edit;
    public static User user;
    @FXML
    private TextField usernameField;
    @FXML
    private RadioButton userTypeAdmin;
    @FXML
    private RadioButton userTypeTeacher;
    @FXML
    private RadioButton userTypeStudent;
    @FXML
    private PasswordField userPasswordField;

    private Stage dialogStage;

    private static void emptyUser() {
        user = new User();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        User user1 = AdminManageUserViewController.currentSelectedUser;
        if (user1 == null) {
            return;
        }
        usernameField.setText(user1.getUsername().get());
        if (edit) {
            usernameField.setDisable(true);
        }
        if (user1.getUserType().get() == 0) {
            userTypeAdmin.setSelected(true);
        }
        if (user1.getUserType().get() == 1) {
            userTypeTeacher.setSelected(true);
        }
        if (user1.getUserType().get() == 2) {
            userTypeStudent.setSelected(true);
        }
    }

    /**
     * Called when the admin clicks ok.
     */
    @FXML
    private void handleOkClicked(ActionEvent event) {
        if (isInputValid()) {
            emptyUser();
            user.setUsername(usernameField.getText());
            if (userTypeAdmin.isSelected()) {
                user.setUserType(0);
            }
            if (userTypeTeacher.isSelected()) {
                user.setUserType(1);
            }
            if (userTypeStudent.isSelected()) {
                user.setUserType(2);
            }
            if (!userPasswordField.getText().equals("")) {
                user.setUserPassword(userPasswordField.getText());
            }
            if (edit && userPasswordField.getText().equals("")) {
                user.setUserPassword("");
            }
            if (edit && AdminManageUserViewController.currentSelectedUser.getUserType().get()
                    == 1 && user.getUserType().get() == 2) {
                Alert alert = GeneralMethods.createAlert("User type modified",
                        "Are you sure you want to change?\n"
                                + "All the teacher reservation will be deleted.",
                        ((Node) event.getSource()).getScene().getWindow(),
                        Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(null) == ButtonType.OK) {
                    UserEditDialogLogic.deleteOldTeacherReservations(user);
                    this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    dialogStage.close();
                } else {
                    alert.close();
                    this.initialize();
                }
            } else {
                this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                dialogStage.close();
            }
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancelClicked(ActionEvent event) {
        user = null;
        this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * This method is used to validate if the admin has entered a valid username and password during
     * creation/update.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String username = usernameField.getText();
        String password = userPasswordField.getText();
        boolean userTypeAdminSelected = userTypeAdmin.isSelected();
        boolean userTypeTeacherSelected = userTypeTeacher.isSelected();
        boolean userTypeStudentSelected = userTypeStudent.isSelected();

        String errorMessage = UserEditDialogLogic.isInputValid(username, userTypeAdminSelected,
                userTypeTeacherSelected, userTypeStudentSelected, password, edit);
        if (errorMessage == "") {
            return true;
        } else {
            // Show the error message.
            GeneralMethods.alertBox("Invalid Fields", "Please correct the invalid fields",
                    errorMessage, Alert.AlertType.ERROR);
            return false;
        }

    }

}
