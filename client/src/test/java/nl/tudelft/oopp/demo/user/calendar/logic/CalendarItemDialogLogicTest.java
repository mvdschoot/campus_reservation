package nl.tudelft.oopp.demo.user.calendar.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.awt.Color;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

/**
 * Tests the calendarItemDialogLogic class.
 * Mocks a server to get custom responses and test possible cases.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalendarItemDialogLogicTest {

    private ClientAndServer mockServer;

    /**
     * Mocks a deleteItem POST endpoint that responds with a 200 status code.
     */
    void expectationPostTrue() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/deleteItem"),
                        exactly(1))
                .respond(response().withStatusCode(200));
    }

    /**
     * Mocks a deleteReservation POST endpoint that responds with a 200 status code.
     */
    void expectationPostTrue2() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/deleteReservation"),
                        exactly(1))
                .respond(response().withStatusCode(200));
    }

    /**
     * Mocks a deleteBikeReservation POST endpoint that responds with a 200 status code.
     */
    void expectationPostTrue3() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/deleteBikeReservation"),
                        exactly(1))
                .respond(response().withStatusCode(200));
    }

    /**
     * Before starting the tests the server gets initialized + expectations set.
     */
    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
        expectationPostTrue();
        expectationPostTrue2();
        expectationPostTrue3();
    }

    /**
     * After all the tests the server gets shut down.
     */
    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    /**
     * Tests the object constructor of the class.
     */
    @Test
    void objectConstructor() {
        assertNotNull(new CalendarItemDialogLogic());
    }

    /**
     * Tests the setItemType method.
     */
    @Test
    void setItemTypeTest() {
        CalendarItemDialogLogic.setItemType(Color.BLACK);
        assertFalse(CalendarItemDialogLogic.reservation);
        CalendarItemDialogLogic.setItemType(Color.ORANGE);
        assertTrue(CalendarItemDialogLogic.item);
        assertFalse(CalendarItemDialogLogic.reservation);
        assertFalse(CalendarItemDialogLogic.bikeReservation);
        CalendarItemDialogLogic.setItemType(Color.MAGENTA);
        assertTrue(CalendarItemDialogLogic.bikeReservation);
        CalendarItemDialogLogic.setItemType(Color.CYAN);
        assertTrue(CalendarItemDialogLogic.reservation);
    }

    /**
     * Tests the deleteItem method.
     */
    @Test
    void deleteItemTest() throws UnsupportedEncodingException {
        assertTrue(CalendarItemDialogLogic.deleteItem(0));
        assertFalse(CalendarItemDialogLogic.bikeReservation);
        assertFalse(CalendarItemDialogLogic.reservation);
        assertFalse(CalendarItemDialogLogic.item);
    }

    /**
     * Tests the deleteReservation method.
     */
    @Test
    void deleteReservationTest() throws UnsupportedEncodingException {
        CalendarItemDialogLogic.reservation = true;
        assertTrue(CalendarItemDialogLogic.deleteReservation(0));
        CalendarItemDialogLogic.bikeReservation = true;
        assertTrue(CalendarItemDialogLogic.deleteReservation(0));
        assertFalse(CalendarItemDialogLogic.deleteReservation(0));
    }
}