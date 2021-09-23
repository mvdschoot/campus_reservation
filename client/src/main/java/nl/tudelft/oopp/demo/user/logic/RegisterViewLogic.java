package nl.tudelft.oopp.demo.user.logic;

import java.util.regex.Pattern;

import nl.tudelft.oopp.demo.communication.RegisterServerCommunication;

public class RegisterViewLogic {

    /**
     * Checks for any input that is invalid for the username.
     *
     * @param usernameTxt the input of the user.
     * @return A string with the invalidation or Good! if there is none.
     */
    public static String checkUsername(String usernameTxt) {

        Pattern characters = Pattern.compile("[!@#$%^&*`~<,>./?:;'{|+=_-]");
        Pattern space = Pattern.compile(" ");

        //Checks whether the password username field is left empty or not.
        if (usernameTxt.trim().isEmpty()) {
            return "This field cannot be left empty !";

            //Checks if the username has any spaces.
        } else if (space.matcher(usernameTxt).find()) {
            return "The username is not allowed to have any spaces !";

            //Checks if the username has any punctuations.
        } else if (characters.matcher(usernameTxt).find()) {
            return "The username is not allowed to have any punctuations !";
        }
        return "Good!";
    }

    /**
     * Checks for any input that is invalid for the password.
     *
     * @param passwordTxt the input of the user.
     * @return A string with the invalidation or Good! if there is none.
     */
    public static String checkPassword(String passwordTxt) {
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern characters = Pattern.compile("[!@#$%^&*`~<,>./?:;'{|+=_-]");
        Pattern space = Pattern.compile(" ");

        //Checks whether the password field is left empty.
        if (passwordTxt.trim().isEmpty()) {
            return "This field cannot be left empty !";

            //Checks whether the password field is at least 8 characters long.
        } else if (passwordTxt.length() < 8) {
            return "The password needs to have at least 8 characters !";

            //Checks whether the password contains at least one numeric value.
        } else if (!passwordTxt.matches(".*\\d.*")) {
            return "The password needs to have at least 1 numeric value !";

            //Checks whether the password contains at least one upper case letter.
        } else if (!upperCasePattern.matcher(passwordTxt).find()) {
            return "The password needs to have at least 1 Upper Case letter !";

            //Checks whether the password has any spaces.
        } else if (space.matcher(passwordTxt).find()) {
            return "The password is not allowed to have any spaces !";

            //Checks whether the password has any punctuations.
        } else if (characters.matcher(passwordTxt).find()) {
            return "The password is not allowed to have any punctuations !";
        }

        return "Good!";
    }

    /**
     * Checks if the two entered passwords are the same.
     *
     * @param passwordTxt   first entered password.
     * @param rePasswordTxt second entered password.
     * @return The password needs to be the same ! or Good! if there is no mistake
     */
    public static String checkRePassword(String passwordTxt, String rePasswordTxt) {
        //Checks whether the password matches the repeat password field
        if (!passwordTxt.equals(rePasswordTxt)) {
            return "The password needs to be the same !";
        } else {
            return "Good!";
        }
    }

    /**
     * Method that communicates with the server to register a new user.
     * @param username username of the user
     * @param password password of the user
     * @param type type of the user
     * @return true if successful, false otherwise
     */
    public static boolean registerCommunication(String username, String password, int type) {
        return RegisterServerCommunication.sendRegister(username, password, type);
    }

}
