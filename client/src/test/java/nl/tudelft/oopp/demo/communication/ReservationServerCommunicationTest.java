package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.createReservation;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.deleteReservation;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.getAllReservations;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.getCurrentId;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.getReservation;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.getUserReservations;
import static nl.tudelft.oopp.demo.communication.ReservationServerCommunication.updateReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationServerCommunicationTest {

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
    
    @Test
    void objectConstructor() {
        assertNotNull(new ReservationServerCommunication());
    }

    @Test
    void createReservationTest() {
        assertTrue(createReservation(null, 0, null, null, null));
    }

    @Test
    void updateReservationTest() {
        assertTrue(updateReservation(0, 0, null, null, null));
    }

    @Test
    void deleteReservationTest() {
        assertTrue(deleteReservation(0));
    }

    @Test
    void getReservationTest() {
        assertEquals("Success", getReservation(0));
    }

    @Test
    void getAllReservationsTest() {
        assertEquals("Success", getAllReservations());
    }

    @Test
    void getUserReservationsTest() {
        assertEquals("Success", getUserReservations(null));
    }

    @Test
    void getCurrentIdTest() {
        assertEquals("Success", getCurrentId());
    }
}