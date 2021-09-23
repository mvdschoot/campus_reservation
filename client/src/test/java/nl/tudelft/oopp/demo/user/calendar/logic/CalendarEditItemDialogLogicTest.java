package nl.tudelft.oopp.demo.user.calendar.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.model.Appointment;

import java.time.LocalDate;

import javafx.util.StringConverter;

import org.junit.jupiter.api.Test;

/**
 * Tests the calendarEditItemDialogLogic class.
 */
class CalendarEditItemDialogLogicTest {

    /**
     * Tests the object constructor.
     */
    @Test
    void objectConstructor() {
        assertNotNull(new CalendarEditItemDialogLogic());
    }

    /**
     * Tests the getRangeSliderConverter method.
     */
    @Test
    void getRangeSliderConverterTest() {
        StringConverter<Number> converter = CalendarEditItemDialogLogic.getRangeSliderConverter();
        assertEquals(728, converter.fromString("12:08"));
        assertEquals("16:45", converter.toString(1005));
        assertNull(converter.fromString("400"));
        assertNull(converter.toString(null));
    }

    /**
     * Tests the checkInputValidity method.
     */
    @Test
    void checkInputValidityTest() {
        String errorMessage1 = CalendarEditItemDialogLogic.checkInputValidity(null, "",
                "description");
        assertEquals("No date provided!\nNo header provided!\n", errorMessage1);

        String errorMessage2 = CalendarEditItemDialogLogic.checkInputValidity(LocalDate.now(), "header",
                "");
        assertEquals("No description provided!\n", errorMessage2);
    }

    /**
     * Tests the checkIfAnyError method.
     */
    @Test
    void checkIfAnyErrorTest() {
        assertTrue(CalendarEditItemDialogLogic.checkIfAnyError(""));
        assertFalse(CalendarEditItemDialogLogic.checkIfAnyError("notEmpty"));
    }

    /**
     * Tests the getDatePickerConverter method.
     */
    @Test
    void getDatePickerConverterTest() {
        StringConverter<LocalDate> converter = CalendarEditItemDialogLogic.getDatePickerConverter();
        assertEquals("2002-06-14", converter.toString(LocalDate.of(2002, 6, 14)));
        assertEquals(LocalDate.of(2003, 7, 15), converter.fromString("2003-07-15"));
        assertEquals("", converter.toString(null));
        assertNull(converter.fromString(null));
        assertNull(converter.fromString(""));
    }

    /**
     * Tests the createItem method.
     */
    @Test
    void createItemTest() {
        Appointment app = CalendarEditItemDialogLogic.createItem("header", "description",
                LocalDate.of(2002, 6, 14), "08:00", "24:00");
        Appointment app2 = CalendarEditItemDialogLogic.createItem("header2", "description2",
                LocalDate.of(2002, 6, 14), "08:00", "21:00");
        assertEquals("header", app.getHeaderText());
        assertEquals("description2", app2.getDescriptionText());
        assertNotEquals(DateTime.now(), app.getStartTime());
        assertNotEquals(DateTime.now(), app2.getEndTime());
    }
}