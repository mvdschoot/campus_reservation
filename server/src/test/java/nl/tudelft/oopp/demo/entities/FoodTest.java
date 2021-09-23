package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodTest {

    private Food f1;
    private Food f2;
    private Food f3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        f1 = new Food(1, "Pizza", 10.99);
        f2 = new Food(2, "Water", 0.88);
        f3 = new Food(1, "Pizza", 10.99);
    }

    /**
     * Test for getId method.
     */
    @Test
    void getIdTest() {
        assertEquals(1, f1.getId());
    }

    /**
     * Test for getName method.
     */
    @Test
    void getNameTest() {
        assertEquals("Pizza", f3.getName());
    }

    /**
     * Test for getPrice method.
     */
    @Test
    void getPriceTest() {
        assertEquals(10.99, f1.getPrice());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(f1, f3);
        assertNotEquals(f2, f3);
        assertNotEquals(f2, "TEST");
    }
}