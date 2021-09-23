package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.RoomServerCommunication.createRoom;
import static nl.tudelft.oopp.demo.communication.RoomServerCommunication.deleteRoom;
import static nl.tudelft.oopp.demo.communication.RoomServerCommunication.getAllRooms;
import static nl.tudelft.oopp.demo.communication.RoomServerCommunication.getRoom;
import static nl.tudelft.oopp.demo.communication.RoomServerCommunication.updateRoom;
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
class RoomServerCommunicationTest {

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
        assertNotNull(new RoomServerCommunication());
    }

    @Test
    void createRoomTest() {
        assertTrue(createRoom(null, 0, false, 0, null, null, null));
    }

    @Test
    void updateRoomTest() {
        assertTrue(updateRoom(0, null, 0, false, 0, null, null, null));
    }

    @Test
    void deleteRoomTest() {
        assertTrue(deleteRoom(0));
    }

    @Test
    void getRoomTest() {
        assertEquals("Success", getRoom(0));
    }

    @Test
    void getAllRoomsTest() {
        assertEquals("Success", getAllRooms());
    }
}