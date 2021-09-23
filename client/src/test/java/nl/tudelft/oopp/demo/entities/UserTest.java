package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
class UserTest {

    private User user;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getAllUsers.
     */
    void expGetAllUsers() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllUsers"))
                .respond(response().withStatusCode(200).withBody("[{\"username\":\"username\","
                        + "\"password\":\"password\",\"type\":\"0\"}]"));
    }

    /**
     * Sets wrong mock server response for /getAllUsers.
     */
    void expGetAllUsersError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllUsers"))
                .respond(response().withStatusCode(200).withBody("[{\"username\":\"username\","
                        + "\"password\":\"password\",\"type\";\"0\"}"));
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
    void setup() {
        user = new User("username", "password", 0);
    }

    /**
     * Tests the equals method.
     */
    @Test
    void equalsTest() {
        User u = new User("username", "password", 0);
        User u2 = new User("ededed", "password", 0);
        assertEquals(u, u);
        assertEquals(user, u);
        assertNotEquals(u2, u);
        assertNotEquals(u2, "HELLO");
    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyConstructor() {
        user = new User();
        assertNull(user.getUsername().get());
        assertNull(user.getUserPassword().get());
        assertEquals(-1, user.getUserType().get());
    }

    /**
     * Tests the getUsername method.
     */
    @Test
    void getUsername() {
        assertEquals("username", user.getUsername().get());
    }

    /**
     * Tests the getUserPassword method.
     */
    @Test
    void getUserPassword() {
        assertEquals("password", user.getUserPassword().get());
    }

    /**
     * Tests the getUserType method.
     */
    @Test
    void getUserType() {
        assertEquals(0, user.getUserType().get());
    }

    /**
     * Tests the setUsername method.
     */
    @Test
    void setUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername().get());
    }

    /**
     * Tests the setUserPassword method.
     */
    @Test
    void setUserPassword() {
        user.setUserPassword("newpassword");
        assertEquals("newpassword", user.getUserPassword().get());
    }

    /**
     * Tests the setUserType method.
     */
    @Test
    void setUserType() {
        user.setUserType(2);
        assertEquals(2, user.getUserType().get());
    }

    /**
     * Tests the getUserData method.
     */
    @Test
    void getUserData() {
        expGetAllUsers();
        ObservableList<User> userData = FXCollections.observableArrayList();
        userData.add(user);
        assertEquals(userData, User.getUserData());
        stopServer();
        startServer();
        expGetAllUsersError();
        assertNull(User.getUserData());
    }
}