package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class BuildingTest {

    private Building b1;
    private Building b2;
    private Building b3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        b1 = new Building(1, "Name", 5, "drebbelweg aan zee",
                4, "09:00", "22:00");
        b2 = new Building(1, "Name", 5, "drebbelweg aan zee",
                4, "07:00", "22:00");
        b3 = new Building(2, "Name", 5, "drebbelweg aan zee",
                4, "09:00", "23:00");
    }

    /**
     * Test for Equals method.
     */
    @Test
    void getId() {
        assertEquals(1, b1.getId());
        assertEquals(1, b2.getId());
        assertEquals(2, b3.getId());
    }

    /**
     * Test for getName method.
     */
    @Test
    void getNameTest() {
        assertEquals("Name", b1.getName());
    }

    /**
     * Test for getRoomCount method.
     */
    @Test
    void getRoomCountTest() {
        assertEquals(5, b1.getRoomCount());
    }

    /**
     * Test for getAddress method.
     */
    @Test
    void getAddressTest() {
        assertEquals("drebbelweg aan zee", b1.getAddress());
    }

    /**
     * Test for getMaxBikes method.
     */
    @Test
    void getMaxBikesTest() {
        assertEquals(4, b1.getMaxBikes());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(b1, b2);
        assertEquals(b2, b1);
        assertNotEquals(b1, b3);
        assertEquals(b1, b1);
        assertNotEquals(b1, "pizza");
    }
}