package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTest {

    private Item i1;
    private Item i2;
    private Item i3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        i1 = new Item(1, "test", "title", "2020-09-08", "08:00",
                "09:00", "description");
        i2 = new Item(1, "test", "title", "2020-09-08", "08:00",
                "09:00", "description");
        i3 = new Item(3, "test", "title3", "2020-09-13", "19:00",
                "22:00", "description3");
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(i1, i2);
        assertNotEquals(i1, i3);
        assertNotEquals(i2, i3);
        assertNotEquals(i1, "test");
    }

    /**
     * Test for getId method.
     */
    @Test
    void getId() {
        assertEquals(1, i1.getId());
        assertEquals(3, i3.getId());
    }

    /**
     * Test for getUser method.
     */
    @Test
    void getUserTest() {
        assertEquals("test", i1.getUser());
        assertEquals("test", i2.getUser());
    }

    /**
     * Test for getTitle method.
     */
    @Test
    void getTitleTest() {
        assertEquals("title", i1.getTitle());
        assertEquals("title3", i3.getTitle());
    }

    /**
     * Test for getDate method.
     */
    @Test
    void getDateTest() {
        assertEquals("2020-09-08", i1.getDate());
        assertEquals("2020-09-13", i3.getDate());

    }

    /**
     * Test for getStartingTime method.
     */
    @Test
    void getStartingTimeTest() {
        assertEquals("08:00", i1.getStartingTime());
    }

    /**
     * Test for getEndingTime method.
     */
    @Test
    void getEndingTimeTest() {
        assertEquals("22:00", i3.getEndingTime());
    }

    /**
     * Test for getDescription method.
     */
    @Test
    void getDescriptionTest() {
        assertEquals("description3", i3.getDescription());
    }
}
