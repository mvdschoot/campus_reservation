package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.createBikeReservation;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.deleteBikeReservation;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.getAllBikeReservation;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.getBikeReservation;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.getBuildingBikeReservations;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.getUserBikeReservations;
import static nl.tudelft.oopp.demo.communication.BikeReservationCommunication.updateBikeReservation;
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
class BikeReservationCommunicationTest {

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
        assertNotNull(new BikeReservationCommunication());
    }

    @Test
    void createBikeReservationTest() {
        assertTrue(createBikeReservation(1, "test", 2, "0000-00-00", "00:00:00", "00:00:00"));
    }

    @Test
    void updateBikeReservationTest() {
        assertTrue(updateBikeReservation(2, 1, "test", 2, "0000-00-00", "00:00:00", "00:00:00"));
    }

    @Test
    void deleteBikeReservationTest() {
        assertTrue(deleteBikeReservation(0));
    }

    @Test
    void getBikeReservationTest() {
        assertEquals("Success", getBikeReservation(0));
    }

    @Test
    void getAllBikeReservationTest() {
        assertEquals("Success", getAllBikeReservation());
    }

    @Test
    void getBuildingBikeReservationsTest() {
        assertEquals("Success", getBuildingBikeReservations(0));
    }

    @Test
    void getUserBikeReservationsTest() {
        assertEquals("Success", getUserBikeReservations("test"));
    }
}