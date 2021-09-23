package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class UserServerCommunication {

    /**
     * This client-server method is used to get all the users.
     *
     * @return all users
     */
    public static String getAllUsers() {
        return sendGet("getAllUsers", "");
    }

    /**
     * This client-server method is used to get a particular user.
     *
     * @param username username of the user
     * @return User object
     */
    public static String getUser(String username) {
        String params = "username=" + username;
        return sendGet("getUser", params);
    }

    /**
     * This client-server method is used to delete an user.
     *
     * @param username username of the user
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteUser(String username) {
        String params = "username=" + username;
        return sendPost("deleteUser", params);
    }

    /**
     * This client-server method is used to update the information assigned to a user.
     *
     * @param username - User's username
     * @param password - User's Password
     * @param type     - Type of user (teacher, student or admin)
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateUser(String username, String password, int type) {
        String params = "username=" + username + "&password=" + password + "&type=" + type;
        return sendPost("updateUser", params);
    }

    /**
     * This client-server method is used to update the information assigned to a user (but not password).
     *
     * @param username - User's username
     * @param type     - Type of user (teacher, student or admin)
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateUser(String username, int type) {
        String params = "username=" + username + "&type=" + type;
        return sendPost("updateUser2", params);
    }


    /**
     * This client-server method is used to create a new user.
     *
     * @param username - User's username
     * @param password - User's Password
     * @param type     - Type of user (teacher, student or admin)
     * @return true if communication was successful, false otherwise
     */
    public static boolean createUser(String username, String password, int type) {
        String params = "username=" + username + "&password=" + password + "&type=" + type;
        return sendPost("createUser", params);
    }
}
