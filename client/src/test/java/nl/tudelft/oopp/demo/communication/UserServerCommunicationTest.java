package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.UserServerCommunication.createUser;
import static nl.tudelft.oopp.demo.communication.UserServerCommunication.deleteUser;
import static nl.tudelft.oopp.demo.communication.UserServerCommunication.getAllUsers;
import static nl.tudelft.oopp.demo.communication.UserServerCommunication.getUser;
import static nl.tudelft.oopp.demo.communication.UserServerCommunication.updateUser;
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
class UserServerCommunicationTest {

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
        assertNotNull(new UserServerCommunication());
    }

    @Test
    void getAllUsersTest() {
        assertEquals("Success", getAllUsers());
    }

    @Test
    void getUserTest() {
        assertEquals("Success", getUser(null));
    }

    @Test
    void deleteUserTest() {
        assertTrue(deleteUser(null));
    }

    @Test
    void updateUserTest() {
        assertTrue(updateUser(null, null, 0));
    }

    @Test
    void createUserTest() {
        assertTrue(createUser(null, null, 0));
    }
}