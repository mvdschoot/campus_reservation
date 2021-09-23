package nl.tudelft.oopp.demo.user.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RegisterViewLogicTest {

    @Test
    /**
     * tests if the username is valid.
     */
    void checkUsername() {
        String username = "";
        assertEquals("This field cannot be left empty !",
                RegisterViewLogic.checkUsername(username));

        username = "user name";
        assertEquals("The username is not allowed to have any spaces !",
                RegisterViewLogic.checkUsername(username));

        username = "user.name";
        assertEquals("The username is not allowed to have any punctuations !",
                RegisterViewLogic.checkUsername(username));

        username = "username";
        assertEquals("Good!", RegisterViewLogic.checkUsername(username));

    }

    @Test
    /**
     * tests if the password is valid.
     */
    void checkPassword() {
        String password = "";
        assertEquals("This field cannot be left empty !",
                RegisterViewLogic.checkPassword(password));

        password = "passwor";
        assertEquals("The password needs to have at least 8 characters !",
                RegisterViewLogic.checkPassword(password));

        password = "password";
        assertEquals("The password needs to have at least 1 numeric value !",
                RegisterViewLogic.checkPassword(password));

        password = "password01";
        assertEquals("The password needs to have at least 1 Upper Case letter !",
                RegisterViewLogic.checkPassword(password));

        password = "Password 01";
        assertEquals("The password is not allowed to have any spaces !",
                RegisterViewLogic.checkPassword(password));

        password = "Password.01";
        assertEquals("The password is not allowed to have any punctuations !",
                RegisterViewLogic.checkPassword(password));

        // this password is for testing purposes never use this as your password!
        password = "Password01";
        assertEquals("Good!", RegisterViewLogic.checkPassword(password));

    }

    @Test
    /**
     * tests if the password is the same as the second password.
     */
    void checkRePassword() {
        String password = "Password01";
        String rePassword = "Password02";
        assertEquals("The password needs to be the same !",
                RegisterViewLogic.checkRePassword(password, rePassword));

        rePassword = "Password01";
        assertEquals("Good!", RegisterViewLogic.checkRePassword(password, rePassword));
    }
}