package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BikeReservationTest {

    private BikeReservation br1;
    private BikeReservation br2;
    private BikeReservation br3;
    private User u1;
    private User u2;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        u1 = new User("test1", "passHASHED1", 0);
        u2 = new User("test2", "passHASHED2", 2);
        br1 = new BikeReservation(1, 22, u1, 20, "2020-09-04",
                "08:00", "12:00");
        br2 = new BikeReservation(1, 22, u1, 20, "2020-09-04",
                "08:00", "12:00");
        br3 = new BikeReservation(3, 12, u2, 1, "2020-09-04",
                "08:00", "09:00");

    }

    /**
     * Test for getId method.
     */
    @Test
    void getIdTest() {
        assertEquals(1, br1.getId());
    }

    /**
     * Test for getBuilding method.
     */
    @Test
    void getBuildingTest() {
        assertEquals(12, br3.getBuilding());
    }

    /**
     * Test for getUser method.
     */
    @Test
    void getUserTest() {
        assertEquals(u2, br3.getUser());
    }

    /**
     * Test for getNumBikes method.
     */
    @Test
    void getNumBikesTest() {
        assertEquals(1, br3.getNumBikes());
    }

    /**
     * Test for getDate method.
     */
    @Test
    void getDateTest() {
        assertEquals("2020-09-04", br1.getDate());
    }

    /**
     * Test for getStartingTime method.
     */
    @Test
    void getStartingTimeTest() {
        assertEquals("08:00", br3.getStartingTime());
    }

    /**
     * Test for getEndingTime method.
     */
    @Test
    void getEndingTimeTest() {
        assertEquals("12:00", br2.getEndingTime());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(br1, br2);
        assertNotEquals(br2, br3);
        assertNotEquals(br2, "TEST");
    }
}