package nl.tudelft.oopp.demo.admin.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BuildingEditDialogLogicTest {

    /**
     * Test for isValidInput.
     */
    @Test
    void isValidInput() {

        String buildingName = "";
        String buildingAddress = "";
        double openingHoursLow = 0;
        double openingHoursHigh = 0;

        assertEquals("No valid building name!\n",
                BuildingEditDialogLogic.isValidInput(buildingName,
                        buildingAddress, openingHoursHigh, openingHoursLow));

        buildingName = "building";
        assertEquals("No valid building address!\n",
                BuildingEditDialogLogic.isValidInput(buildingName,
                        buildingAddress, openingHoursHigh, openingHoursLow));

        buildingAddress = "address";
        assertEquals("No valid opening hours!\n",
                BuildingEditDialogLogic.isValidInput(buildingName,
                        buildingAddress, openingHoursHigh, openingHoursLow));

        openingHoursHigh = 2;
        assertEquals("", BuildingEditDialogLogic
                .isValidInput(buildingName, buildingAddress,
                        openingHoursHigh, openingHoursLow));

    }

}