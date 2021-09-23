package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.RegisterServerCommunication.sendRegister;
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
class RegisterServerCommunicationTest {

    private ClientAndServer mockServer;

    void expectationPost() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST"))
                .respond(response().withStatusCode(200));
    }

    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
        expectationPost();
    }

    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    void objectConstructor() {
        assertNotNull(new RegisterServerCommunication());
    }

    @Test
    void sendRegisterTest() {
        assertTrue(sendRegister(null, null, 0));
    }
}