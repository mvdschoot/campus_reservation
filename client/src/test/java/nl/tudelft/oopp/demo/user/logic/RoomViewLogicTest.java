package nl.tudelft.oopp.demo.user.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.Reservation;

import org.junit.jupiter.api.Test;

class RoomViewLogicTest {

    @Test
    /**
     * tests if the time slot is valid.
     */
    void checkTimeSlotValidity() {
        double currentStartValue = 15;
        double currentEndValue = 0;
        double startValue = 14;
        double endValue = 14;

        assertFalse(RoomViewLogic.checkTimeSlotValidity(currentStartValue,
                currentEndValue, startValue, endValue));

        currentStartValue = 4;
        currentEndValue = 14;

        assertTrue(RoomViewLogic.checkTimeSlotValidity(currentStartValue,
                currentEndValue, startValue, endValue));
    }

    /**
     * tests which reservation is earlier.
     * -1 for the first one.
     * 1 for the second one.
     */
    @Test
    void compare() {
        Reservation o1 = new Reservation(1, "jim", 1,
                "2020-05-05", "12:00:00", "14:00:00");
        Reservation o2 = new Reservation(2, "jim", 1,
                "2020-05-05", "13:00:00", "14:00:00");

        assertEquals(-1, RoomViewLogic.compare(o1, o2));

        o2.setStartingTime("12:30:00");
        assertEquals(-1, RoomViewLogic.compare(o1, o2));

        o1.setStartingTime("13:30:00");
        assertEquals(1, RoomViewLogic.compare(o1, o2));

        o2.setStartingTime("13:00:00");
        assertEquals(1, RoomViewLogic.compare(o1, o2));
    }

    /**
     * tests if the toString from a number works.
     */
    @Test
    void testToString() {
        int time = 150;
        assertEquals("02:30", RoomViewLogic.toStringNum(time));
    }

    /**
     * tests if the fromString to a number works.
     */
    @Test
    void fromString() {
        String time = "03:30";
        assertEquals(210, RoomViewLogic.fromStringTime(time).intValue());

        assertNull(RoomViewLogic.fromStringTime(null));
    }

    @Test
    /**
     * tests if the fromString for a date works.
     */
    void testFromString() {
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        String date = "";
        assertNull(RoomViewLogic.fromStringDate(date, dateFormatter));

        date = "2020-05-05";
        LocalDate expected = LocalDate.parse(date, dateFormatter);

        assertEquals(expected, RoomViewLogic.fromStringDate(date, dateFormatter));
    }

    /**
     * tests if the toString for a date works.
     */
    @Test
    void testToString1() {
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        String date = "2020-05-05";
        LocalDate given = LocalDate.parse(date, dateFormatter);

        assertEquals("2020-05-05", RoomViewLogic.toStringDate(given, dateFormatter));

        given = null;
        assertEquals("", RoomViewLogic.toStringDate(given, dateFormatter));
    }

    /**
     * tests if the fromString for food works.
     */
    @Test
    void testFromString1() {
        Food f1 = new Food(1, "Hamburger", 14.50);
        Food f2 = new Food(2, "Pizza", 12.50);

        List<Food> foodList = new ArrayList<Food>();
        foodList.add(f1);
        foodList.add(f2);

        assertEquals(f1, RoomViewLogic.fromStringFood("Hamburger", foodList));

        foodList = null;
        assertNull(RoomViewLogic.fromStringFood(null, foodList));
    }

    /**
     * tests if the toString for food works.
     */
    @Test
    void testToString2() {
        Food f1 = new Food(1, "Hamburger", 14.50);

        assertEquals("Hamburger", RoomViewLogic.toStringFood(f1));
        assertNull(RoomViewLogic.toStringFood(null));
    }
}