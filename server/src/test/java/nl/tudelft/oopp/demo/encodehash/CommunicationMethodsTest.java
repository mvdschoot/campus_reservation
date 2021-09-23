package nl.tudelft.oopp.demo.encodehash;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

class CommunicationMethodsTest {

    /**
     * Test for decodeCommunication method.
     */
    @Test
    void decodeCommunicationTest() throws UnsupportedEncodingException {
        new CommunicationMethods();
        String encoded = "drebbelweg+aan+zee%21";
        String expected = "drebbelweg aan zee!";
        assertEquals(expected, CommunicationMethods.decodeCommunication(encoded));
    }
}