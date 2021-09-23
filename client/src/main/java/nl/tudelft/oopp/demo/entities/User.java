package nl.tudelft.oopp.demo.entities;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import nl.tudelft.oopp.demo.communication.UserServerCommunication;

import org.json.JSONArray;
import org.json.JSONException;


public class User {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getUsername().get().equals(user.getUsername().get());
    }

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private StringProperty username;
    private StringProperty userPassword;
    private IntegerProperty userType;

    /**
     * Default constructor.
     */
    public User() {
        this.username = new SimpleStringProperty(null);
        this.userPassword = new SimpleStringProperty(null);
        this.userType = new SimpleIntegerProperty(-1);
    }

    /**
     * Constructor.
     *
     * @param username     String
     * @param userPassword String
     * @param userType     int
     */
    public User(String username, String userPassword, int userType) {
        this.username = new SimpleStringProperty(username);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.userType = new SimpleIntegerProperty(userType);
    }

    /**
     * Convert server response into an ObservableList of rooms.
     *
     * @return ObservableList containing all users.
     */
    public static ObservableList<User> getUserData() throws JSONException {
        try {
            ObservableList<User> userData = FXCollections.observableArrayList();
            JSONArray jsonArrayUsers = new JSONArray(UserServerCommunication.getAllUsers());
            for (int i = 0; i < jsonArrayUsers.length(); i++) {
                User u = new User();
                u.setUsername(jsonArrayUsers.getJSONObject(i).getString("username"));
                u.setUserPassword(jsonArrayUsers.getJSONObject(i).getString("password"));
                u.setUserType(jsonArrayUsers.getJSONObject(i).getInt("type"));
                userData.add(u);
            }
            return userData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getUsername() {
        return username;
    }

    /**
     * Setter.
     *
     * @param username String, new.
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getUserPassword() {
        return userPassword;
    }

    /**
     * Setter.
     *
     * @param userPassword String, new.
     */
    public void setUserPassword(String userPassword) {
        this.userPassword.set(userPassword);
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty.
     */
    public IntegerProperty getUserType() {
        return userType;
    }

    /**
     * Setter.
     *
     * @param userType int, new
     */
    public void setUserType(int userType) {
        this.userType.set(userType);
    }
}
