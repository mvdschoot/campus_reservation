package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.net.http.HttpClient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeneralCommunicationTest {

    private ClientAndServer mockServer;
    private HttpClient client;

    void expectationPostFalse() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/testPostFalse"),
                        exactly(1))
                .respond(response().withStatusCode(401));
    }

    void expectationPostTrue() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/testPostTrue"),
                        exactly(1))
                .respond(response().withStatusCode(200));
    }

    void expectationGetFalse() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/testGetFalse"),
                        exactly(1))
                .respond(response().withStatusCode(401)
                        .withBody("Wrong"));
    }

    void expectationGetTrue() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/testGetTrue"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("Success"));
    }


    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);

        client = HttpClient.newBuilder().build();

        expectationPostFalse();
        expectationPostTrue();

        expectationGetFalse();
        expectationGetTrue();
    }

    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    void objectConstructor() {
        assertNotNull(new GeneralCommunication());
    }

    @Test
    void sendPost() {
        assertFalse(GeneralCommunication.sendPost("testPostFalse", ""));
        assertTrue(GeneralCommunication.sendPost("testPostTrue", ""));
        assertFalse(GeneralCommunication.sendPost("\"", ""));
    }

    @Test
    void sendGet() {
        assertEquals("Wrong", GeneralCommunication.sendGet("testGetFalse", ""));
        assertEquals("Success", GeneralCommunication.sendGet("testGetTrue", ""));
        assertNull(GeneralCommunication.sendGet("\"", ""));
    }
}