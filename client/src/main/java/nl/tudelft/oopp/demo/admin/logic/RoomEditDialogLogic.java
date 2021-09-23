package nl.tudelft.oopp.demo.admin.logic;

import java.io.File;

import nl.tudelft.oopp.demo.entities.Building;

public class RoomEditDialogLogic {

    /**
     * .
     * To validate the input of the user
     *
     * @param roomNameField        - Name of room
     * @param roomBuildingComboBox - List of buildings
     * @param radioButtonYes       - Yes radio box
     * @param radioButtonNo        - No Radio box
     * @param roomCapacityField    - Room capacity
     * @param roomTypeField        - Room type
     * @param roomDescriptionField - Room description
     * @return - Boolean value to validate input
     */
    public static String isValidInput(String roomNameField, Building roomBuildingComboBox,
                                      boolean radioButtonYes, boolean radioButtonNo,
                                      String roomCapacityField, String roomTypeField,
                                      String roomDescriptionField, String oldFileName,
                                      boolean changedImage, String fileName) {
        String errorMessage = "";

        if (roomNameField.equals("")) {
            errorMessage += "No valid room name!\n";
        }
        //if (roomBuildingComboBox.getSelectionModel().) {
        //errorMessage += "No valid building selected!\n";
        //}
        if (!radioButtonYes && !radioButtonNo) {
            errorMessage += "No teacher only button selected!\n";
        }
        if (roomCapacityField.equals("")) {
            errorMessage += "No valid capacity!\n";
        }
        if (roomDescriptionField.length() >= 270) {
            errorMessage += "The description of the room can't be more than 270 characters";
        }
        // checks if there already exists an image with this name
        File imageFolder = new File("client/src/main/resources/images");
        // if admin creates new room or updates image, check if the image already exists
        if (oldFileName == null || changedImage) {
            for (File f : imageFolder.getAbsoluteFile().listFiles()) {
                if (f.getName().equals(fileName)) {
                    errorMessage += "This file name is already used, please choose another one!\n";
                    break;
                }
            }
        }
        if (roomTypeField.equals("")) {
            errorMessage += "No valid room type!\n";
        }
        if (roomDescriptionField.equals("")) {
            errorMessage += "No valid room description!\n";
        }

        return errorMessage;
    }
}
