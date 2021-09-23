package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.createItem;
import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.deleteItem;
import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.getAllItems;
import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.getCurrentId;
import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.getItem;
import static nl.tudelft.oopp.demo.communication.ItemServerCommunication.getUserItems;
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
class ItemServerCommunicationTest {

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
        assertNotNull(new ItemServerCommunication());
    }

    @Test
    void getAllItemsTest() {
        assertEquals("Success", getAllItems());
    }

    @Test
    void getItemTest() {
        assertEquals("Success", getItem(0));
    }

    @Test
    void createItemTest() {
        assertTrue(createItem(null, null, null, null, null, null));
    }

    @Test
    void deleteItemTest() {
        assertTrue(deleteItem(0));
    }

    @Test
    void getCurrentIdTest() {
        assertEquals("Success", getCurrentId());
    }

    @Test
    void getUserItemsTest() {
        assertEquals("Success", getUserItems("test"));
    }
}