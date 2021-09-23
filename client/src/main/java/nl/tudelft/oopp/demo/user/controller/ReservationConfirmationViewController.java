package nl.tudelft.oopp.demo.user.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.CurrentUserManager;

/**
 * Class that controls the dialog pop up that asks for a reservation confirmation.
 */
public class ReservationConfirmationViewController implements Initializable {

    // current Room
    public static Room room;
    // reservation information
    public static String date;
    public static String startTime;
    public static String endTime;
    public static boolean foodChosen;
    public static List<Food> foodList;
    public static Map<Food, Integer> foodMap;
    // confirmation state
    public static boolean confirmed = false;
    @FXML
    private Text confirmationText;

    /**
     * Method that gets called before everything (mostly to initialize nodes etc.).
     * JavaFX standard.
     *
     * @param location  is passed
     * @param resources is passed
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmed = false;
        String confirmationString = getConfirmationString();

        confirmationText.setText(confirmationString);
    }

    /**
     * Method that constructs the confirmation text for the dialog box.
     *
     * @return String the confirmation text
     */
    private String getConfirmationString() {
        double totalPrice = 0.0;

        String confirmationString = "You (" + CurrentUserManager.getUsername() + ") would like to book the "
                + room.getRoomName().get() + " on " + date + " from " + startTime + " until " + endTime + ".\n"
                + "Food ordered:\n\n";
        for (Food f : foodList) {
            double foodPrice = f.getFoodPrice().get();
            String price = GeneralMethods.formatPriceString(foodPrice);
            int amount = foodMap.get(f);
            totalPrice += (((double) amount) * foodPrice);
            confirmationString += "- " + amount + "x " + f.getFoodName().get()
                    + "            (" + amount + "x " + price + " euro(s))\n";
        }
        confirmationString += "\nTotal price: " + GeneralMethods.formatPriceString(totalPrice) + " euro(s)";
        confirmationString += "\nWould you like to confirm that?\n";

        return confirmationString;
    }


    /**
     * When user clicks 'confirm' reservation goes through.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void confirmClicked(ActionEvent event) {
        // set confirmed state
        confirmed = true;

        // close current stage
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.close();
    }

    /**
     * When user clicks 'cancel' reservation does not go through.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void cancelClicked(ActionEvent event) {
        // set confirmed state
        confirmed = false;

        // close current stage
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.close();
    }
}
