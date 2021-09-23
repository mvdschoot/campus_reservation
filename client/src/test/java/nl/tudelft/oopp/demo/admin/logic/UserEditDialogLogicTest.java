package nl.tudelft.oopp.demo.admin.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserEditDialogLogicTest {
    @Test
    /**
     * tests if the input of a user is valid.
     */
    void isInputValidTest() {

        String test = UserEditDialogLogic.isInputValid(
                "username", false, false, false,
                "Password123", false);
        String expected = "Select the type of user!\n";
        assertEquals(expected, test);
    }

    @Test
    /**
     * tests the response when an username is empty.
     */
    void isUsernameValidTestEmpty() {
        String errorMessage = "";
        String test = UserEditDialogLogic
                .isUsernameValid(errorMessage, "");
        assertEquals("Username field can't be blank!\n", test);
    }

    @Test
    /**
     * tests if the username is valid.
     */
    void isUsernameValidTest() {
        String errorMessage = "";
        String test = UserEditDialogLogic
                .isUsernameValid(errorMessage, "test .");
        String expected = "Username is not allowed to have any spaces.\n"
                + "Username is not allowed to have any punctuations.\n";
        assertEquals(expected, test);
    }

    @Test
    /**
     * tests if the response to an empty password is correct.
     */
    void isPasswordValidTestEmpty() {
        String errorMessage = "";
        String test = UserEditDialogLogic
                .isPasswordValid(errorMessage, "", false);
        String expected = "Password field can't be blank!\n"
                + "Password needs to at-least 8 characters.\n"
                + "Password needs at-least 1 numeric value.\n"
                + "Password needs at-least 1 upper case character.\n";
        assertEquals(expected, test);
    }

    @Test
    /**
     * tests if the password is valid.
     */
    void isPasswordValidTest() {
        String errorMessage = "";
        String test = UserEditDialogLogic
                .isPasswordValid(errorMessage, "a .", true);
        assertEquals("", test);
    }
}