package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationsTest {

    private Reservations r1;
    private Reservations r2;
    private Reservations r3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Reservations(1, "Otte", 4, "2020-07-07",
                "13:00", "15:00");
        r2 = new Reservations(1, "Otte", 4, "2020-07-07",
                "13:00", "15:00");
        r3 = new Reservations(3, "Leon", 6, "2020-07-07",
                "12:30", "16:00");
    }

    /**
     * Test for getId method.
     */
    @Test
    void getId() {
        assertEquals(1, r1.getId());
        assertEquals(3, r3.getId());
    }

    /**
     * Test for getUsername method.
     */
    @Test
    void getUsernameTest() {
        assertEquals("Otte", r1.getUsername());
        assertEquals("Leon", r3.getUsername());

    }

    /**
     * Test for getRoom method.
     */
    @Test
    void getRoomTest() {
        assertEquals(4, r1.getRoom());
        assertEquals(6, r3.getRoom());
    }

    /**
     * Test for getDate method.
     */
    @Test
    void getDateTest() {
        assertEquals("2020-07-07", r1.getDate());
    }

    /**
     * Test for getStartingTime method.
     */
    @Test
    void getStartingTimeTest() {
        assertEquals("13:00", r1.getStartingTime());
    }

    /**
     * Test for getEndingTime method.
     */
    @Test
    void getEndingTimeTest() {
        assertEquals("15:00", r1.getEndingTime());
    }

    @Test
    void getFoodReservations() {
        Set<FoodReservations> set = new HashSet<>();
        assertEquals(set, r1.getFoodReservations());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(r1, r1);
        assertEquals(r2, r1);
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, "pizza");
    }
}