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
class FoodReservationTest {

    private FoodReservation foodRes;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getFoodReservationByReservation.
     */
    void expectationGetUserReservationFood() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFoodReservationByReservation"))
                .respond(response().withStatusCode(200).withBody("[{\"food\":{\"id\":10,\"name\":\"Pizza\","
                        + "\"price\":7.9},\"reservation\":{\"id\":20,\"username\":\"mam\","
                        + "\"room\":505,\"date\":\"2020-04-13\","
                        + "\"startingTime\":\"09:00:00\",\"endingTime\":\"17:00:00\"},\"quantity\":7}]"));
    }

    /**
     * Sets mock server response for /getFoodReservationByReservation.
     */
    void expectationGetUserReservationFoodError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFoodReservationByReservation"))
                .respond(response().withStatusCode(200).withBody("[[20,10,"));
    }

    /**
     * Sets mock server response for /getAllFoodReservations.
     */
    void expectationGetAllFoodReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllFoodReservations"))
                .respond(response().withStatusCode(200).withBody("[[20,10,7]]"));
    }

    /**
     * Sets mock server response for /getAllFoodReservations.
     */
    void expectationGetAllFoodReservationsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllFoodReservations"))
                .respond(response().withStatusCode(200).withBody("[[20,10,7"));
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
        foodRes = new FoodReservation(20, 10, 2);
    }

    /**
     * Tests the equals method.
     */
    @Test
    void equalsTest() {
        final FoodReservation fr1 = new FoodReservation(20, 10, 3);
        final FoodReservation fr2 = new FoodReservation(10, 10, 3);
        final FoodReservation fr3 = new FoodReservation(10, 40, 3);
        assertEquals(fr1, fr1);
        assertEquals(fr1, foodRes);
        assertNotEquals(fr1, fr2);
        assertNotEquals(fr2, fr3);
        assertNotEquals(fr1, "HELLO");

    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyConstructor() {
        foodRes = new FoodReservation();
        assertEquals(-1, foodRes.getFoodId().get());
        assertEquals(-1, foodRes.getFoodQuantity().get());
        assertEquals(-1, foodRes.getReservationId().get());
    }

    /**
     * Tests the getFoodId method.
     */
    @Test
    void getFoodId() {
        assertEquals(10, foodRes.getFoodId().get());
    }

    /**
     * Tests the getReservationId method.
     */
    @Test
    void getReservationId() {
        assertEquals(20, foodRes.getReservationId().get());
    }

    /**
     * Tests the getFoodQuantity method.
     */
    @Test
    void getFoodQuantity() {
        assertEquals(2, foodRes.getFoodQuantity().get());
    }

    /**
     * Tests the setFoodId method.
     */
    @Test
    void setFoodId() {
        foodRes.setFoodId(11);
        assertEquals(11, foodRes.getFoodId().get());
    }

    /**
     * Tests the setReservationId method.
     */
    @Test
    void setReservationId() {
        foodRes.setReservationId(21);
        assertEquals(21, foodRes.getReservationId().get());
    }

    /**
     * Tests the setFoodQuantity method.
     */
    @Test
    void setFoodQuantity() {
        foodRes.setFoodQuantity(3);
        assertEquals(3, foodRes.getFoodQuantity().get());
    }

    /**
     * Tests the getUserReservationFood method.
     */
    @Test
    void getUserReservationFood() {
        Reservation r = new Reservation(20,
                "test", 0, "test", "test", "test");
        expectationGetUserReservationFood();
        ObservableList<FoodReservation> foodData = FXCollections.observableArrayList();
        foodData.add(foodRes);
        assertEquals(foodData, FoodReservation.getUserReservationFood(r));
        stopServer();
        startServer();
        expectationGetUserReservationFoodError();
        assertNull(FoodReservation.getUserReservationFood(r));
    }

    /**
     * Tests the getAllFoodReservations method.
     */
    @Test
    void getAllFoodReservations() {
        expectationGetAllFoodReservations();
        ObservableList<FoodReservation> foodData = FXCollections.observableArrayList();
        foodData.add(foodRes);
        assertEquals(foodData, FoodReservation.getAllFoodReservations());
        stopServer();
        startServer();
        expectationGetAllFoodReservationsError();
        assertNull(FoodReservation.getAllFoodReservations());
    }
}