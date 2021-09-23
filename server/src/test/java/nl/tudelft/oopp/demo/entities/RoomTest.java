package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

    private Room r1;
    private Room r2;
    private Room r3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Room(1, "p4", 4, true, 30, "image1.jpg",
                "beautiful", "instruction");
        r2 = new Room(1, "p4", 4, true, 30, "image1.jpg",
                "beautiful", "instruction");
        r3 = new Room(2, "p5", 4, false, 35, "image2.png",
                "ugly", "lecture");
    }

    /**
     * Test for getId method.
     */
    @Test
    void getIdTest() {
        assertEquals(1, r1.getId());
        assertEquals(2, r3.getId());
    }

    /**
     * Test for getName method.
     */
    @Test
    void getNameTest() {
        assertEquals("p4", r1.getName());
        assertEquals("p5", r3.getName());
    }

    /**
     * Test for getBuilding method.
     */
    @Test
    void getBuildingTest() {
        assertEquals(4, r1.getBuilding());
    }

    /**
     * Test for isTeacherOnly method.
     */
    @Test
    void isTeacherOnlyTest() {
        assertTrue(r1.isTeacherOnly());
        assertFalse(r3.isTeacherOnly());
    }

    /**
     * Test for getCapacity method.
     */
    @Test
    void getCapacityTest() {
        assertEquals(30, r1.getCapacity());
        assertEquals(35, r3.getCapacity());
    }

    /**
     * Test for getPhotos method.
     */
    @Test
    void getPhotosTest() {
        assertEquals("image1.jpg", r1.getPhotos());
        assertEquals("image2.png", r3.getPhotos());
    }

    /**
     * Test for getDescription method.
     */
    @Test
    void getDescriptionTest() {
        assertEquals("beautiful", r1.getDescription());
        assertEquals("ugly", r3.getDescription());
    }

    /**
     * Test for getType method.
     */
    @Test
    void getTypeTest() {
        assertEquals("instruction", r1.getType());
        assertEquals("lecture", r3.getType());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(r1, r1);
        assertEquals(r2, r1);
        assertEquals(r1, r2);
        assertNotEquals(r3, r1);
        assertNotEquals(r1, "pizza");
    }
}