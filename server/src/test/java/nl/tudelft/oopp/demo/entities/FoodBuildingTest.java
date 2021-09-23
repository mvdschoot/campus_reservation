package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodBuildingTest {

    private Food f1;
    private Food f2;
    private Building b1;
    private Building b2;
    private FoodBuilding fb1;
    private FoodBuilding fb2;
    private FoodBuilding fb3;
    private FoodBuilding fb4;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        f1 = new Food(1, "Pizza", 10.99);
        f2 = new Food(2, "Water", 0.88);
        b1 = new Building(1, "Name", 5, "drebbelweg aan zee",
                4, "08:00", "22:00");
        b2 = new Building(2, "Name", 5, "drebbelweg aan zee",
                4, "08:00", "22:00");
        fb1 = new FoodBuilding(f1, b2);
        fb2 = new FoodBuilding(f1, b2);
        fb3 = new FoodBuilding(f2, b1);
        fb4 = new FoodBuilding(f2, b2);
    }

    @Test
    void objectConstructor() {
        assertNotNull(new FoodBuilding());
    }

    /**
     * Test for getFood method.
     */
    @Test
    void getFoodTest() {
        assertEquals(f1, fb1.getFood());
    }

    /**
     * Test for getBuilding method.
     */
    @Test
    void getBuildingTest() {
        assertEquals(b2, fb1.getBuilding());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(fb1, fb2);
        assertNotEquals(fb2, fb3);
        assertNotEquals(fb3, fb4);
        assertNotEquals(fb2, "TEST");
    }
}