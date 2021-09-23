package nl.tudelft.oopp.demo.user.logic;

public class LoginViewLogic {

    /**
     * Checks if the username input and password input are valid.
     *
     * @param usernameInput input of the username.
     * @param passwordInput input of the password.
     * @return a String with what is wrong or that it is good.
     */
    public static String isValidInput(String usernameInput, String passwordInput) {
        // Checks whether the password username field is left empty or not.
        if (usernameInput.isEmpty()) {
            return "The username field cannot be left empty !";
        } else if (passwordInput.isEmpty()) {
            // Checks whether the password field is left empty.
            return "The password field cannot be left empty !";
        } else {
            // This string value means that all the fields are filled.
            return "Good!";
        }
    }
}
