package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.addFoodToBuilding;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.addFoodToReservation;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.createFood;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.deleteFood;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.deleteFoodFromBuilding;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.deleteFoodFromReservation;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.getAllFood;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.getFood;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.getFoodByBuildingId;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.getFoodReservationByReservation;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.updateFood;
import static nl.tudelft.oopp.demo.communication.FoodServerCommunication.updateFoodReservationQuantity;
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
class FoodServerCommunicationTest {

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
        assertNotNull(new FoodServerCommunication());
    }

    @Test
    void createFoodTest() {
        assertTrue(createFood("test", 0));
    }

    @Test
    void addFoodToBuildingTest() {
        assertTrue(addFoodToBuilding(0, 0));
    }

    @Test
    void addFoodToReservationTest() {
        assertTrue(addFoodToReservation(0, 0, 0));
    }

    @Test
    void testAddFoodToReservationTest() {
        assertTrue(addFoodToReservation(0, 0));
    }

    @Test
    void updateFoodTest() {
        assertTrue(updateFood(0, "test", 0));
    }

    @Test
    void deleteFoodFromReservationTest() {
        assertTrue(deleteFoodFromReservation(0, 10));
    }

    @Test
    void deleteFoodFromBuildingTest() {
        assertTrue(deleteFoodFromBuilding(0, 0));
    }

    @Test
    void updateFoodReservationQuantityTest() {
        assertTrue(updateFoodReservationQuantity(0, 0, 1));
    }

    @Test
    void deleteFoodTest() {
        assertTrue(deleteFood(0));
    }

    @Test
    void getFoodTest() {
        assertEquals("Success", getFood(0));
    }

    @Test
    void getFoodReservationByReservationTest() {
        assertEquals("Success", getFoodReservationByReservation(0));
    }

    @Test
    void getFoodByBuildingIdTest() {
        assertEquals("Success", getFoodByBuildingId(0));
    }

    @Test
    void getAllFoodTest() {
        assertEquals("Success", getAllFood());
    }
}