package nl.tudelft.oopp.demo.admin.logic;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.general.GeneralMethods;


public class UserEditDialogLogic {

    List<Reservation> reservationList = Reservation.getAllReservations();

    /**
     * To validate the input of the user.
     *
     * @param username                - Username
     * @param userTypeAdminSelected   - Admin type
     * @param userTypeTeacherSelected - Teacher type
     * @param userTypeStudentSelected - Student type
     * @param password                - Password
     * @param edit                    - Boolean value
     * @return Boolean value to validate
     */
    public static String isInputValid(String username, boolean userTypeAdminSelected,
                                      boolean userTypeTeacherSelected, boolean userTypeStudentSelected,
                                      String password, boolean edit) {
        String errorMessage = "";
        // Checks whether the type of user is selected.
        if (!(userTypeAdminSelected || userTypeTeacherSelected
                || userTypeStudentSelected)) {
            errorMessage += "Select the type of user!\n";
        }
        errorMessage = isUsernameValid(errorMessage, username);
        errorMessage = isPasswordValid(errorMessage, password, edit);
        return errorMessage;
    }

    /**
     * Checks if the username is valid.
     * Helpermethod for isInputValid.
     *
     * @param errorMessage String
     * @param username     String
     */
    public static String isUsernameValid(String errorMessage, String username) {
        // Checks whether the username field is empty.
        if (username.trim().isEmpty()) {
            errorMessage += "Username field can't be blank!\n";
        }
        // This pattern is used to compare the text to see if it has any spaces.
        Pattern space = Pattern.compile(" ");
        // Checks whether the username contains any spaces.
        if (space.matcher(username).find()) {
            errorMessage += "Username is not allowed to have any spaces.\n";
        }
        // This pattern is used to compare the text to see if it has any punctuations.
        Pattern characters = Pattern.compile("[!@#$%^&*`~<,>./?:;'{|+=_-]");
        // Checks whether the username contains any punctuations.
        if (characters.matcher(username).find()) {
            errorMessage += "Username is not allowed to have any punctuations.\n";
        }
        return errorMessage;
    }

    /**
     * Checks if the password is valid.
     * Helpermethod for isInputValid.
     *
     * @param errorMessage String
     * @param password     String
     * @param edit         Boolean
     */
    public static String isPasswordValid(String errorMessage, String password, boolean edit) {
        if (!edit) {
            // Checks whether the password field is empty.
            if (password.length() == 0) {
                errorMessage += "Password field can't be blank!\n";
            }
            // Checks whether the password field is at least 8 characters long.
            if (password.length() < 8) {
                errorMessage += "Password needs to at-least 8 characters.\n";
            }
            // Checks whether the password contains at least one numeric value.
            if (!password.matches(".*\\d.*")) {
                errorMessage += "Password needs at-least 1 numeric value.\n";
            }
            // This pattern is used to compare the text to see if it has an upper case character.
            Pattern upperCasePattern = Pattern.compile("[A-Z]");
            // Checks whether the password contains at least one upper case character.
            if (!upperCasePattern.matcher(password).find()) {
                errorMessage += "Password needs at-least 1 upper case character.\n";
            }
            // This pattern is used to compare the text to see if it has any spaces.
            Pattern space = Pattern.compile(" ");
            // Checks whether the password contains any spaces.
            if (space.matcher(password).find()) {
                errorMessage += "Password is not allowed to have spaces in them.\n";
            }
            // This pattern is used to compare the text to see if it has any punctuations.
            Pattern characters = Pattern.compile("[!@#$%^&*`~<,>./?:;'{|+=_-]");
            // Checks whether the password contains any punctuations.
            if (characters.matcher(password).find()) {
                errorMessage += "Password is not allowed to have any punctuations.\n";
            }
        }
        return errorMessage;
    }

    /**
     * Removes all the old teacher reservations when a user gets switched from teacher to student.
     */
    public static void deleteOldTeacherReservations(User user) {
        List<Reservation> reservationList = Reservation.getUserReservation2(user);

        if (reservationList == null) {
            return;
        }
        List<Room> roomList = Room.getRoomData();
        List<Room> rooms = roomList.stream()
                .filter(x -> x.getTeacherOnly().get() == true)
                .collect(Collectors.toList());
        for (Reservation re: reservationList) {
            for (Room ro: rooms) {
                if (re.getRoom().get() == ro.getRoomId().get()) {
                    AdminLogic.deleteReservationLogic(re);
                }
            }
        }
    }

}
