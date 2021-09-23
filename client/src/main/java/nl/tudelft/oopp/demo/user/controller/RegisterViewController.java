package nl.tudelft.oopp.demo.user.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.logic.RegisterViewLogic;
import nl.tudelft.oopp.demo.views.LoginView;

public class RegisterViewController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField rePassword;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label rePasswordLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private RadioButton student;

    @FXML
    private RadioButton teacher;

    @FXML
    private Label radioLabel;

    /**
     * Initializing the radio buttons into toggle groups.
     */
    public void initialize() {
        // This toggle group is created such that the user can't select both the radio boxes.
        ToggleGroup group = new ToggleGroup();
        student.setToggleGroup(group);
        teacher.setToggleGroup(group);
    }

    /**
     * .
     * To check if teacher is selected.
     *
     * @return Boolean
     */
    @FXML
    private boolean studentSelected() {
        if (student.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * .
     * To check if teacher is selected.
     *
     * @return Boolean
     */
    @FXML
    private boolean teacherSelected() {
        if (teacher.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * .
     * Handles the clicking of register button
     *
     * @param event - Passed as parameter
     * @throws IOException is thrown
     */
    public void registerClicked(ActionEvent event) throws IOException {
        String usernameTxt = username.getText();
        String passwordTxt = password.getText();
        String rePasswordTxt = rePassword.getText();

        String usernameResponse = RegisterViewLogic.checkUsername(usernameTxt);
        String passwordResponse = RegisterViewLogic.checkPassword(passwordTxt);
        String rePasswordResponse = RegisterViewLogic.checkRePassword(passwordTxt, rePasswordTxt);

        if (!usernameResponse.equals("Good!")) {
            usernameLabel.setText(usernameResponse);
            usernameLabel.setStyle("-fx-text-fill: red");
        } else if (!passwordResponse.equals("Good!")) {
            passwordLabel.setText(passwordResponse);
            passwordLabel.setStyle("-fx-text-fill: red");
        } else if (!rePasswordResponse.equals("Good!")) {
            rePasswordLabel.setText("The password needs to be the same !");
            rePasswordLabel.setStyle("-fx-text-fill: red");
            //Checks whether the role of the new user is selected.
        } else if (!student.isSelected() && !teacher.isSelected()) {
            radioLabel.setText("Please select your role !");
            radioLabel.setStyle("-fx-text-fill: red");
            usernameLabel.setText("");
            rePasswordLabel.setText("");
            //Server connection is established.
        } else {
            int userType = 0;
            if (studentSelected()) {
                userType = 2;
            }
            if (teacherSelected()) {
                userType = 1;
            }

            if (RegisterViewLogic.registerCommunication(username.getText(), password.getText(), userType)) {
                GeneralMethods.alertBox("register status", "",
                        "Successfully created a new account", Alert.AlertType.INFORMATION);
            } else {
                GeneralMethods.alertBox("register status", "",
                        "An error occured while creating a new account. Please try again",
                        Alert.AlertType.INFORMATION);
            }
            backClicked(event);
        }
    }

    /**
     * .
     * On the click of the back button, the stage is redirected to the login page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void backClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        LoginView lv = new LoginView();
        lv.start(stage);
    }
}
