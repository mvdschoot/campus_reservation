package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.mindfusion.common.DateTime;

import java.awt.Color;

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
class ItemTest {

    private Item item;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getUserItems.
     */
    void expGetUserItems() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getUserItems"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"user\":\"user\",\"title\":\"title\","
                        + "\"date\":\"2020-04-04\",\"startingTime\":\"12:00:00\","
                        + "\"endingTime\":\"13:00:00\",\"description\":"
                        + "\"description\"}]"));
    }

    /**
     * Sets wrong mock server response for /getUserItems.
     */
    void expGetUserItemsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getUserItems"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":32,"
                        + "\"user\":\"user\",\"title\":\"title\","
                        + "\"date\"]"));
    }

    /**
     * Sets mock server response for /getAllItems.
     */
    void expGetAllItems() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllItems"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"user\":\"user\",\"title\":\"title\","
                        + "\"date\":\"2020-04-04\",\"startingTime\":\"12:00:00\","
                        + "\"endingTime\":\"13:00:00\",\"description\":"
                        + "\"description\"}]"));
    }

    /**
     * Sets wrong mock server response for /getAllItems.
     */
    void expGetAllItemsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllItems"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":32,"
                        + "\"user\":"));
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
        item = new Item(10, "user", "title", "2020-04-04",
                "12:00:00", "13:00:00", "description");
    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyContructor() {
        item = new Item();
        assertEquals(-1, item.getItemId().get());
        assertNull(item.getUser().get());
        assertNull(item.getTitle().get());
        assertNull(item.getDate().get());
        assertNull(item.getItemStartingTime().get());
        assertNull(item.getItemEndingTime().get());
        assertNull(item.getItemDescription().get());
    }

    /**
     * Tests the getItemId method.
     */
    @Test
    void getId() {
        assertEquals(10, item.getItemId().get());
    }

    /**
     * Tests the getUser method.
     */
    @Test
    void getUser() {
        assertEquals("user", item.getUser().get());
    }

    /**
     * Tests the getTitle method.
     */
    @Test
    void getTitle() {
        assertEquals("title", item.getTitle().get());
    }

    /**
     * Tests the getDate method.
     */
    @Test
    void getDate() {
        assertEquals("2020-04-04", item.getDate().get());
    }

    /**
     * Tests the getItemStartingTime method.
     */
    @Test
    void getStartingTime() {
        assertEquals("12:00:00", item.getItemStartingTime().get());
    }

    /**
     * Tests the getItemEndingTime method.
     */
    @Test
    void getEndingTime() {
        assertEquals("13:00:00", item.getItemEndingTime().get());
    }

    /**
     * Tests the getItemDescription method.
     */
    @Test
    void getDescription() {
        assertEquals("description", item.getItemDescription().get());
    }

    /**
     * Tests the setId method.
     */
    @Test
    void setId() {
        item.setId(20);
        assertEquals(20, item.getItemId().get());
    }

    /**
     * Tests the setUser method.
     */
    @Test
    void setUser() {
        item.setUser("anotheruser");
        assertEquals("anotheruser", item.getUser().get());
    }

    /**
     * Tests the setTitle method.
     */
    @Test
    void setTitle() {
        item.setTitle("anothertitle");
        assertEquals("anothertitle", item.getTitle().get());
    }

    /**
     * Tests the setDate method.
     */
    @Test
    void setDate() {
        item.setDate("anotherdate");
        assertEquals("anotherdate", item.getDate().get());
    }

    /**
     * Tests the setStartingTime method.
     */
    @Test
    void setStartingTime() {
        item.setStartingTime("anotherstartingtime");
        assertEquals("anotherstartingtime", item.getItemStartingTime().get());
    }

    /**
     * Tests the setEndingTime method.
     */
    @Test
    void setEndingTime() {
        item.setEndingTime("anotherendingtime");
        assertEquals("anotherendingtime", item.getItemEndingTime().get());
    }

    /**
     * Tests the setDescription function.
     */
    @Test
    void setDescription() {
        item.setDescription("anotherdescription");
        assertEquals("anotherdescription", item.getItemDescription().get());
    }

    /**
     * Tests the equals function.
     */
    @Test
    void testEquals() {
        final Item item2 = new Item(5, "", "", "", "", "", "");
        final Item item3 = new Item(10, "", "", "", "", "", "");
        final Integer integer = 2;

        assertEquals(item, item);
        assertNotEquals(item, integer);
        assertEquals(item, item3);
        assertNotEquals(item, item2);
    }

    /**
     * Tests the getAllItems function.
     */
    @Test
    void getAllItems() {
        expGetAllItems();
        ObservableList<Item> itemData = FXCollections.observableArrayList();
        itemData.add(item);
        assertEquals(itemData, Item.getAllItems());
        stopServer();
        startServer();
        expGetAllItemsError();
        assertNull(Item.getAllItems());
    }

    /**
     * Tests the getUserItems function.
     */
    @Test
    void getUserItems() {
        expGetUserItems();
        ObservableList<Item> itemData = FXCollections.observableArrayList();
        itemData.add(item);
        assertEquals(itemData, Item.getUserItems("uidyed"));
        stopServer();
        startServer();
        expGetUserItemsError();
        assertNull(Item.getUserItems("ioded"));
    }

    /**
     * Tests the getId function.
     */
    @Test
    void testGetId() {
        assertEquals("10", item.getId());
    }

    /**
     * Tests the setId function.
     */
    @Test
    void testSetId() {
        item.setId(1);
        assertEquals("1", item.getId());
    }

    /**
     * Tests the getHeader function.
     */
    @Test
    void getHeader() {
        assertEquals("Item", item.getHeader());
    }

    /**
     * Tests the getStartTime function.
     */
    @Test
    void getStartTime() {
        DateTime time = new DateTime(2020, 4, 4, 12, 0, 0);
        assertEquals(time, item.getStartTime());
    }

    /**
     * Tests the getEndTime function.
     */
    @Test
    void getEndTime() {
        DateTime time = new DateTime(2020, 4, 4, 13, 0, 0);
        assertEquals(time, item.getEndTime());
    }

    /**
     * Tests the getDescription function.
     */
    @Test
    void testGetDescription() {
        assertEquals("description", item.getDescription());
    }

    /**
     * Tests the getColor function.
     */
    @Test
    void getColor() {
        assertEquals(Color.ORANGE, item.getColor());
    }
}