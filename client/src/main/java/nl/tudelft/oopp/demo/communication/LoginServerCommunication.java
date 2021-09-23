package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;

public class LoginServerCommunication {

    /**
     * Asks the server if login is granted for a user identified by username and password.
     *
     * @return the body of a get request to the server.
     */
    public static String sendLogin(String username, String password) {
        String params = "username=" + username + "&password=" + password;
        return sendGet("login", params);
    }
}
