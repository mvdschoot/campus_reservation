package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomTest {

    private Room room;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getAllRooms.
     */
    void expGetAllRooms() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllRooms"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"name\":\"name\",\"building\":15,\"teacherOnly\":true,"
                        + "\"capacity\":20,\"photos\":\"photo\","
                        + "\"description\":\"description\",\"type\":\"type\"}]"));
    }

    /**
     * Sets wrong mock server response for /getAllRooms.
     */
    void expGetAllRoomsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllRooms"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":505,"
                        + "\"name\":\"EthanRoom\",\"building\":695,\"teacherOnly\":true,"
                        + "\"capacity\":99,\"photos\":\"niceRoom3.jpg\","
                        + "\"description\":\"Lecctures are given here\",\"type\":\"LectureHall\"}"));
    }

    /**
     * Sets mock server response for /getRoom.
     */
    void expGetRoom() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getRoom"))
                .respond(response().withStatusCode(200).withBody("{\"id\":10,"
                        + "\"name\":\"name\",\"building\":15,\"teacherOnly\":true,"
                        + "\"capacity\":20,\"photos\":\"photo\","
                        + "\"description\":\"description\",\"type\":\"type\"}"));
    }

    /**
     * Sets wrong mock server response for /getRoom.
     */
    void expGetRoomError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getRoom"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"name\":\"name\",\"building\":15,\"teacherOnly\":true,"
                        + "\"capacity\":99,\"photos\":\"niceRoom3.jpg\","
                        + "\"description\":\"Lecctures are given here\",\"type\":\"LectureHall\"}]"));
    }

    /**
     * Server setup before any test functions are executed.
     */
    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
    }

    /**
     * Server shutdown after all test functions are executed.
     */
    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    /**
     * Class variable setup everytime a test function is executed.
     */
    @BeforeEach
    void setUp() {
        room = new Room(10, "name", 15, true, 20,
                "photo", "description", "type");
    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyConstructor() {
        room = new Room();
        assertEquals(-1, room.getRoomId().get());
        assertNull(room.getRoomName().get());
        assertEquals(-1, room.getRoomBuilding().get());
        assertFalse(room.getTeacherOnly().get());
        assertEquals(-1, room.getRoomCapacity().get());
        assertNull(room.getRoomPhoto().get());
        assertNull(room.getRoomDescription().get());
        assertNull(room.getRoomType().get());
    }

    /**
     * Tests the equals method.
     */
    @Test
    void equalsTest() {
        Room r2 = new Room(10, "name", 15, true, 20,
                "photo", "description", "type");
        Room r3 = new Room(1, "name", 15, true, 20,
                "photo", "description", "type");
        assertEquals(r2, r2);
        assertEquals(r2, room);
        assertNotEquals(r2, r3);
        assertNotEquals(r2, "HELLO");
    }

    /**
     * Tests the getRoomId method.
     */
    @Test
    void getRoomId() {
        assertEquals(10, room.getRoomId().get());
    }

    /**
     * Tests the getRoomName method.
     */
    @Test
    void getRoomName() {
        assertEquals("name", room.getRoomName().get());
    }

    /**
     * Tests the getTeacherOnly method.
     */
    @Test
    void getTeacherOnly() {
        assertTrue(room.getTeacherOnly().get());
    }

    /**
     * Tests the getRoomBuilding method.
     */
    @Test
    void getRoomBuilding() {
        assertEquals(15, room.getRoomBuilding().get());
    }

    /**
     * Tests the getRoomCapacity method.
     */
    @Test
    void getRoomCapacity() {
        assertEquals(20, room.getRoomCapacity().get());
    }

    /**
     * Tests the getRoomPhoto method.
     */
    @Test
    void getRoomPhoto() {
        assertEquals("photo", room.getRoomPhoto().get());
    }

    /**
     * Tests the getRoomDescription method.
     */
    @Test
    void getRoomDescription() {
        assertEquals("description", room.getRoomDescription().get());
    }

    /**
     * Tests the getRoomType method.
     */
    @Test
    void getRoomType() {
        assertEquals("type", room.getRoomType().get());
    }

    /**
     * Tests the setRoomId method.
     */
    @Test
    void setRoomId() {
        room.setRoomId(5);
        assertEquals(5, room.getRoomId().get());
    }

    /**
     * Tests the setRoomName method.
     */
    @Test
    void setRoomName() {
        room.setRoomName("newname");
        assertEquals("newname", room.getRoomName().get());
    }

    /**
     * Tests the setTeacherOnly method.
     */
    @Test
    void setTeacherOnly() {
        room.setTeacherOnly(false);
        assertFalse(room.getTeacherOnly().get());
    }

    /**
     * Tests the setRoomBuilding method.
     */
    @Test
    void setRoomBuilding() {
        room.setRoomBuilding(10);
        assertEquals(10, room.getRoomBuilding().get());
    }

    /**
     * Tests the setRoomCapacity function.
     */
    @Test
    void setRoomCapacity() {
        room.setRoomCapacity(15);
        assertEquals(15, room.getRoomCapacity().get());
    }

    /**
     * Tests the setRoomPhoto function.
     */
    @Test
    void setRoomPhoto() {
        room.setRoomPhoto("newphoto");
        assertEquals("newphoto", room.getRoomPhoto().get());
    }

    /**
     * Tests the setRoomDescription function.
     */
    @Test
    void setRoomDescription() {
        room.setRoomDescription("newdescription");
        assertEquals("newdescription", room.getRoomDescription().get());
    }

    /**
     * Tests the setRoomType function.
     */
    @Test
    void setRoomType() {
        room.setRoomType("newtype");
        assertEquals("newtype", room.getRoomType().get());
    }

    /**
     * Tests the getRoomData function.
     */
    @Test
    void getRoomData() {
        expGetAllRooms();
        ObservableList<Room> roomData = FXCollections.observableArrayList();
        roomData.add(room);
        assertEquals(roomData, Room.getRoomData());
        stopServer();
        startServer();
        expGetAllRoomsError();
        assertNull(Room.getRoomData());
    }

    /**
     * Tests the getRoomById function.
     */
    @Test
    void getRoomById() {
        expGetRoom();
        assertEquals(room, Room.getRoomById(0));
        stopServer();
        startServer();
        expGetRoomError();
        assertNull(Room.getRoomById(0));
    }
}