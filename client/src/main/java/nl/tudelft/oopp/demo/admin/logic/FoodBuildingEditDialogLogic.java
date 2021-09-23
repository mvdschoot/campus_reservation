package nl.tudelft.oopp.demo.admin.logic;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.general.GeneralMethods;

public class FoodBuildingEditDialogLogic {

    /**
     * Validates the user input.
     *
     * @return true if the input is valid
     */
    public static boolean isInputValid(Building b, Food selectedFood) {
        String errorMessage = "";

        if (!getDuplicate(b, selectedFood)) {
            errorMessage = "Food is already added to the selected building!";
        }

        if (errorMessage.equals("")) {
            return true;
        } else {
            // Show the error message.
            GeneralMethods.alertBox("Invalid Fields", "Please correct the invalid fields",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }

    /**
     * Checks if the food has already been added to the selected building or not.
     * @param b selected Building object
     * @return true if there is no specified building added already
     */
    public static boolean getDuplicate(Building b, Food selectedFood) {
        ObservableList<Food> foodList = Food.getFoodByBuildingId(b.getBuildingId().get());

        for (Food f: foodList) {
            if (f.equals(selectedFood)) {
                return false;
            }
        }
        return true;
    }

}
