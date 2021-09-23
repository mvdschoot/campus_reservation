package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User u1;
    private User u2;
    private User u3;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        u1 = new User("Ottedam", "password", 0);
        u2 = new User("Ottedam", "password", 0);
        u3 = new User("EthanKeller", "password01", 1);
    }

    @Test
    void objectConstructor() {
        assertNotNull(u2);
        assertNotNull(new User());
    }

    /**
     * Test for getUsername method.
     */
    @Test
    void getUsernameTest() {
        assertEquals("Ottedam", u1.getUsername());
        assertEquals("EthanKeller", u3.getUsername());
    }

    /**
     * Test for getPassword method.
     */
    @Test
    void getPasswordTest() {
        assertEquals("password", u1.getPassword());
        assertEquals("password01", u3.getPassword());
    }

    /**
     * Test for getType method.
     */
    @Test
    void getTypeTest() {
        assertEquals(0, u1.getType());
        assertEquals(1, u3.getType());
    }

    /**
     * Test for Equals method.
     */
    @Test
    void testEquals() {
        assertEquals(u1, u2);
        assertEquals(u2, u1);
        assertEquals(u1, u1);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, "pizza");
        assertNotNull(u1, (String) null);
    }
}
