package nl.tudelft.oopp.demo.admin.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BookingEditDialogLogicTest {

    String date = "";

    @Test
    /**
     * Tests if the input of a booking is valid.
     */
    void isValidInputTest() {
        // Checks whether a room is selected
        assertEquals("No valid room selected!\n", BookingEditDialogLogic.isInputValid(-1,
                "2020-08-30"));
        // Checks whether a time slot is selected
        assertEquals("No valid date selected!\n", BookingEditDialogLogic.isInputValid(2,
                date));
    }

    @Test
    /**
     * tests the correct responds to a valid input.
     */
    void allIsGoodTest() {
        // Checks whether with valid input, it returns expected results
        assertEquals("Good!\n", BookingEditDialogLogic.isInputValid(3, "2020-04-21"));
    }

}
