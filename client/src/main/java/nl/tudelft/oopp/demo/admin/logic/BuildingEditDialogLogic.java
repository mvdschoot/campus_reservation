package nl.tudelft.oopp.demo.admin.logic;

public class BuildingEditDialogLogic {

    /**
     * Checks if the input is valid.
     * @param buildingNameField building name.
     * @param buildingAddressField Address of the building.
     * @param openingHoursSliderHigh The higher opening time.
     * @param openingHoursSliderLow The lower opening time.
     * @return true if the input is valid. False if not.
     */
    public static String isValidInput(String buildingNameField, String buildingAddressField,
                                        double openingHoursSliderHigh, double openingHoursSliderLow) {

        if (buildingNameField.equals("")) {
            return  "No valid building name!\n";
        }
        if (buildingAddressField.equals("")) {
            return "No valid building address!\n";
        }
        if (openingHoursSliderLow == openingHoursSliderHigh) {
            return "No valid opening hours!\n";
        }
        return "";
    }
}
