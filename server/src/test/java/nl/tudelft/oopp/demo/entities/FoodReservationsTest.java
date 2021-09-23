package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodReservationsTest {

    private FoodReservations fr1;
    private FoodReservations fr2;
    private FoodReservations fr3;
    private FoodReservations fr4;
    private Food f1;
    private Food f2;
    private Reservations r1;
    private Reservations r2;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Reservations(1, "Otte", 4, "2020-07-07",
                "13:00", "15:00");
        r2 = new Reservations(3, "Leon", 6, "2020-07-07",
                "12:30", "16:00");
        f1 = new Food(1, "Pizza", 10.99);
        f2 = new Food(2, "Water", 0.88);
        fr1 = new FoodReservations(f1, r1, 2);
        fr2 = new FoodReservations(f1, r1, 2);
        fr3 = new FoodReservations(f2, r2, 1);
        fr4 = new FoodReservations(f2, r1, 1);
    }

    @Test
    void objectConstructor() {
        assertNotNull(fr1);
        assertNotNull(new FoodReservations());
    }

    /**
     * Test for getFood method.
     */
    @Test
    void getFoodTest() {
        assertEquals(f2, fr3.getFood());
    }

    /**
     * Test for getReservation method.
     */
    @Test
    void getReservationTest() {
        assertEquals(r1, fr1.getReservation());
    }

    /**
     * Test for getQuantity method.
     */
    @Test
    void getQuantityTest() {
        assertEquals(1, fr3.getQuantity());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(fr1, fr2);
        assertNotEquals(fr1, fr3);
        assertNotEquals(fr3, "TEST");
        assertNotEquals(fr3, fr4);
    }
}