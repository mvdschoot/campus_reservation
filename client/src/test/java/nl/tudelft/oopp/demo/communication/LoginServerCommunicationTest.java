package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.LoginServerCommunication.sendLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class LoginServerCommunicationTest {

    private ClientAndServer mockServer;

    void expectationGet() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET"))
                .respond(response().withStatusCode(200)
                        .withBody("Success"));
    }

    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
        expectationGet();
    }

    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    void objectConstructor() {
        assertNotNull(new LoginServerCommunication());
    }

    @Test
    void sendLoginTest() {
        assertEquals("Success", sendLogin(null, null));
    }
}