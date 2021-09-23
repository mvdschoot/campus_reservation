package nl.tudelft.oopp.demo.admin.logic;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.time.LocalDate;

import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationEditDialogLogicTest {
    private ClientAndServer mockServer;

    void expectationPost() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST"))
                .respond(response().withStatusCode(200));
    }

    void expectationGet() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET"))
                .respond(response().withStatusCode(200)
                        .withBody("Success"));
    }

    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
        expectationPost();
        expectationGet();
    }

    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    /**
     * tests the isInputValid with on null checks.
     */
    @Test
    void isInputValidNullTest() {
        User user = null;
        Room room = null;
        LocalDate date = null;
        double currentStartValue = 1.0;
        double currentEndValue = 1.0;
        StringConverter<LocalDate> temp = null;

        String test = ReservationEditDialogLogic.isInputValid(
                user, room, date, currentStartValue, currentEndValue, temp);
        String expected = "No valid username provided!\n"
                + "No valid Room provided! \n"
                + "No date provided!\n"
                + "No valid timeslot selected!\n";
        assertEquals(expected, test);
    }

    /**
     * Tests checkTimeSlotValidity on null checks.
     */
    @Test
    void checkTimeSlotValidityNullRoomTest() {
        assertFalse(ReservationEditDialogLogic
                .checkTimeSlotValidity(null, null,
                        0.1, 0.1, null));
    }

    /**
     * tests the checkTimeSlotValidity.
     */
    @Test
    void checkTimeSLotValidityTest() {
        Room room = new Room(
                1, "", 1,
                false, 1, "", "", "");
        assertFalse(ReservationEditDialogLogic
                .checkTimeSlotValidity(room, null, 1,
                        2, null));
    }

    /**
     * test the converter of the rangeSlider.
     */
    @Test
    void getRangeSliderConverterTest() {
        StringConverter<Number> converter = ReservationEditDialogLogic.getRangeSliderConverter();
        assertEquals(728.0, converter.fromString("12:08"));
        assertEquals("16:45", converter.toString(1005));
    }
}