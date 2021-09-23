package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.createBuilding;
import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.deleteBuilding;
import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.getAllBuildings;
import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.getBuilding;
import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.getBuildingByFoodId;
import static nl.tudelft.oopp.demo.communication.BuildingServerCommunication.updateBuilding;
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
class BuildingServerCommunicationTest {

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
        assertNotNull(new BuildingServerCommunication());
    }

    @Test
    void getAllBuildingsTest() {
        assertEquals("Success", getAllBuildings());
    }

    @Test
    void getBuildingTest() {
        assertEquals("Success", getBuilding(0));
    }

    @Test
    void getBuildingByFoodIdTest() {
        assertEquals("Success", getBuildingByFoodId(0));
    }

    @Test
    void createBuildingTest() {
        assertTrue(createBuilding("test", 0, "test", 0, "00:00", "00:00"));
    }

    @Test
    void deleteBuildingTest() {
        assertTrue(deleteBuilding(0));
    }

    @Test
    void updateBuildingTest() {
        assertTrue(updateBuilding(0, "test", 0, "test", 0, "00:00", "00:00"));
    }
}