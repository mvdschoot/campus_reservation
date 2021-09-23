package nl.tudelft.oopp.demo.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

class GeneralMethodsTest {

    @Test
    void loggerSetup() {
    }

    /**
     * Tests the encoding of a string.
     *
     * @throws UnsupportedEncodingException when the encoding method used is not supported.
     */
    @Test
    void encodeCommunication() throws UnsupportedEncodingException {
        String input1 = "er staat een paard in de gang?!";
        String input2 = "It = a song & it is good";
        String res1 = "er+staat+een+paard+in+de+gang%3F%21";
        String res2 = "It+=+a+song+&+it+is+good";

        assertEquals(res1, GeneralMethods.encodeCommunication(input1));
        assertEquals(res2, GeneralMethods.encodeCommunication(input2));
    }
}