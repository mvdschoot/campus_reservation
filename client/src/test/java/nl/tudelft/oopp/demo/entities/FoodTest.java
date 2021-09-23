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
class FoodTest {

    public Food food;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getFood.
     */
    void getFoodById() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFood"))
                .respond(response().withStatusCode(200).withBody("{\"id\":10,"
                        + "\"name\":\"testfood\",\"price\":7.50}"));
    }

    /**
     * Sets wrong mock server response for /getFood.
     */
    void getFoodByIdError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFood"))
                .respond(response().withStatusCode(200).withBody("{\"id\":10,"
                        + "\"namerice\":10.2"));
    }

    /**
     * Sets mock server response for /getAllFood.
     */
    void getAllFood() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllFood"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"name\":\"testfood\",\"price\":7.50}]"));
    }

    /**
     * Sets wrong mock server response for /getAllFoodReservations.
     */
    void getAllFoodError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllFoodReservations"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"namee\":10.2}]"));
    }

    /**
     * Sets mock server response for /getFoodByBuildingId.
     */
    void getFoodByBuildingId() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFoodByBuildingId"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"name\":\"testfood\",\"price\":7.50}]"));
    }

    /**
     * Sets wrong mock server response for /getFoodByBuildingId.
     */
    void getFoodByBuildingIdError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getFoodByBuildingId"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"name\":.2}]"));
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
        food = new Food(10, "testfood", 7.50);
    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyConstructor() {
        food = new Food();
        assertEquals(-1, food.getFoodId().get());
        assertNull(food.getFoodName().get());
        assertEquals(-1.0, food.getFoodPrice().get());
    }

    /**
     * Tests the getFoodId method.
     */
    @Test
    void getFoodId() {
        assertEquals(10, food.getFoodId().get());
    }

    /**
     * Tests the getFoodName method.
     */
    @Test
    void getFoodName() {
        assertEquals("testfood", food.getFoodName().get());
    }

    /**
     * Tests the getFoodPrice method.
     */
    @Test
    void getFoodPrice() {
        assertEquals(7.50, food.getFoodPrice().get());
    }

    /**
     * Tests the setFoodId method.
     */
    @Test
    void setFoodId() {
        food.setFoodId(11);
        assertEquals(11, food.getFoodId().get());
    }

    /**
     * Tests the setFoodName method.
     */
    @Test
    void setFoodName() {
        food.setFoodName("newFoodName");
        assertEquals("newFoodName", food.getFoodName().get());
    }

    /**
     * Tests the setFoodPrice method.
     */
    @Test
    void setFoodPrice() {
        food.setFoodPrice(5.00);
        assertEquals(5.00, food.getFoodPrice().get());
    }

    /**
     * Tests the getAllFoodData method.
     */
    @Test
    void getAllFoodData() {
        getAllFood();
        ObservableList<Food> foodData = FXCollections.observableArrayList();
        foodData.add(food);
        assertEquals(foodData, Food.getAllFoodData());
        stopServer();
        startServer();
        getAllFoodError();
        assertNull(Food.getAllFoodData());
    }

    /**
     * Tests the getFoodByBuildingId method.
     */
    @Test
    void getFoodByBuildingIdTest() {
        getFoodByBuildingId();
        ObservableList<Food> foodData = FXCollections.observableArrayList();
        foodData.add(food);
        assertEquals(foodData, Food.getFoodByBuildingId(9));
        stopServer();
        startServer();
        getFoodByBuildingIdError();
        assertNull(Food.getFoodByBuildingId(0));
    }

    /**
     * Tests the getFoodById method.
     */
    @Test
    void getFoodByIdTest() {
        getFoodById();
        assertEquals(food, Food.getFoodById(9));
        stopServer();
        startServer();
        getFoodByIdError();
        assertNull(Food.getFoodById(0));
    }

    /**
     * Tests the equals method.
     */
    @Test
    void testEquals() {
        final Food food2 = new Food(20, "anotherFood", 20.00);
        final Food food3 = new Food(10, "testfood", 7.50);
        final Integer integer = 2;

        assertEquals(food, food);
        assertNotEquals(null, food);
        assertEquals(food, food3);
        assertNotEquals(food, food2);
        assertNotEquals(food, integer);
    }
}