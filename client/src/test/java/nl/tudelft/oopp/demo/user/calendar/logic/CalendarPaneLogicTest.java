package nl.tudelft.oopp.demo.user.calendar.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.mindfusion.scheduling.model.Appointment;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.FoodReservation;
import nl.tudelft.oopp.demo.entities.Item;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

/**
 * Class that tests the CalendarPaneLogic class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalendarPaneLogicTest {

    private ClientAndServer mockServer;
    private Item item;
    private Reservation reservation;
    private BikeReservation bikeReservation;

    /**
     * Mocks a getAllRooms GET endpoint that responds with a 200 status code + body.
     */
    void getRooms() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllRooms"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":504,\"name\":\"OtteRoom\",\"building\":688,"
                                + "\"teacherOnly\":false,\"capacity\":35,\"photos\":\"sleepRoom930.jpg\","
                                + "\"description\":\"Rooms to love Ethan\",\"type\":\"Lover Room\"}]"));
    }

    /**
     * Mocks a getAllBuildings GET endpoint that responds with a 200 status code + body.
     */
    void getBuildings() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllBuildings"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":688,\"name\":\"OtteBuilding\",\"roomCount\":3,"
                                + "\"address\":\"OtteLaan 45\",\"openingTime\":\"08:00:00\","
                                + "\"closingTime\":\"22:00:00\",\"maxBikes\":20}]"));
    }

    /**
     * Mocks a getAllFood GET endpoint that responds with a 200 status code + body.
     */
    void getFoods() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllFood"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":1,\"name\":\"Pizza\",\"price\":7.9}]"));
    }

    /**
     * Mocks a getAllFoodReservations GET endpoint that responds with a 200 status code + body.
     */
    void getFoodReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllFoodReservations"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[[857,1,4]]"));
    }

    /**
     * Mocks a getUserReservations GET endpoint that responds with a 200 status code + body.
     */
    void getUserReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserReservations"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":857,\"username\":\"ethan\","
                                + "\"room\":504,\"date\":\"2020-04-01\","
                                + "\"startingTime\":\"12:00:00\",\"endingTime\":\"16:30:00\"}]"));
    }

    /**
     * Mocks a getUserReservations GET endpoint that responds with a 401 status code + null body.
     */
    void getUserReservationsNull() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserReservations"),
                        exactly(1))
                .respond(response().withStatusCode(401)
                        .withBody((String) null));
    }

    /**
     * Mocks a getUserItems GET endpoint that responds with a 200 status code + body.
     */
    void getUserItems() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserItems"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":43,\"user\":\"ethan\",\"title\":\"test\","
                                + "\"date\":\"2020-04-08\",\"startingTime\":\"13:00:00\","
                                + "\"endingTime\":\"18:00:00\",\"description\":\"ddd\"}]"));
    }

    /**
     * Mocks a getUserItems GET endpoint that responds with a 401 status code + null body.
     */
    void getUserItemsNull() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserItems"),
                        exactly(1))
                .respond(response().withStatusCode(401)
                        .withBody((String) null));
    }

    /**
     * Mocks a getUserBikeReservations GET endpoint that responds with a 200 status code + body.
     */
    void getUserBikeReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserBikeReservations"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":64,\"building\":688,"
                                + "\"user\":{\"username\":\"ethan\","
                                + "\"password\":\"2689367b205c16ce32ed4200942b8b8b1e"
                                + "262dfc70d9bc9fbc77c49699a4f1df\",\"type\":2},\"numBikes\":6,"
                                + "\"date\":\"2020-04-02\",\"startingTime\":\"10:00:00\","
                                + "\"endingTime\":\"18:00:00\"}]"));
    }

    /**
     * Mocks a getUserBikeReservations GET endpoint that responds with a 401 status code + null body.
     */
    void getUserBikeReservationsNull() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getUserBikeReservations"),
                        exactly(1))
                .respond(response().withStatusCode(401)
                        .withBody((String) null));
    }

    /**
     * Mocks a getAllBikeReservation GET endpoint that responds with a 200 status code + body.
     */
    void getAllBikeReservation() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllBikeReservation"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[{\"id\":64,\"building\":688,"
                                + "\"user\":{\"username\":\"ethan\","
                                + "\"password\":\"2689367b205c16ce32ed4200942b8b8b1e"
                                + "262dfc70d9bc9fbc77c49699a4f1df\",\"type\":2},\"numBikes\":6,"
                                + "\"date\":\"2020-04-02\",\"startingTime\":\"10:00:00\","
                                + "\"endingTime\":\"18:00:00\"}]"));
    }

    /**
     * Mocks a getAllBikeReservation GET endpoint that responds with a 401 status code + null body.
     */
    void getAllBikeReservationNull() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllBikeReservation"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody((String) null));
    }

    /**
     * Mocks a getAllFoodReservations GET endpoint that responds with a 200 status code + body.
     */
    void getAllFoodReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/getAllFoodReservations"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("[[857,1,4]]"));
    }

    /**
     * Mocks a currentId GET endpoint that responds with a 200 status code + body.
     */
    void getCurrentId() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/currentId"),
                        exactly(1))
                .respond(response().withStatusCode(200)
                        .withBody("9"));
    }

    /**
     * Mocks a currentId GET endpoint that responds with a 401 status code + null body.
     */
    void getCurrentIdError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET")
                                .withPath("/currentId"),
                        exactly(1))
                .respond(response().withStatusCode(401)
                        .withBody((String) null));
    }

    /**
     * Mocks a createItem POST endpoint that responds with a 200 status code.
     */
    void createItem() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("POST")
                                .withPath("/createItem"),
                        exactly(1))
                .respond(response().withStatusCode(200));
    }

    /**
     * Before starting the tests the server gets initialized + expectations set.
     */
    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(8080);
        getBuildings();
        getFoodReservations();
        getFoods();
        getRooms();
        getAllFoodReservations();
        createItem();
    }

    /**
     * After all the tests the server gets shut down.
     */
    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    /**
     * Sets the class fields and needed lists for the logic class before every test.
     */
    @BeforeEach
    void setUp() {
        reservation = new Reservation(857, "ethan", 504,
                "2020-04-01", "12:00:00", "16:30:00");
        bikeReservation = new BikeReservation(64, 688, "ethan", 6,
                "2020-04-02", "10:00:00", "18:00:00");
        item = new Item(43, "ethan", "test", "2020-04-08",
                "13:00:00", "18:00:00", "ddd");

        CalendarPaneLogic.foodList = new ArrayList<>();
        CalendarPaneLogic.foodList.add(new Food(1, "Pizza", 7.9));
        CalendarPaneLogic.buildingList = new ArrayList<>();
        CalendarPaneLogic.buildingList.add(new Building(688, "OtteBuilding",
                3, "OtteLaan 45", 20,
                "08:00:00", "22:00:00"));
        CalendarPaneLogic.roomList = new ArrayList<>();
        CalendarPaneLogic.roomList.add(new Room(504, "OtteRooom", 688,
                false, 35, "sleepRoom930.jpg",
                "Rooms to love Ethan", "Lover Room"));
        CalendarPaneLogic.foodReservationList = new ArrayList<>();
        CalendarPaneLogic.foodReservationList.add(new FoodReservation(857, 1, 4));
    }

    /**
     * Tests the object constructor of the class.
     */
    @Test
    void objectConstructor() {
        assertNotNull(new CalendarPaneLogic());
    }

    /**
     * Tests getAllCalendarItems method for null responses from the server.
     * The mock server gets shut down and restarted again with the correct endpoints.
     */
    @Test
    void getAllCalendarItemsNull1() {
        mockServer.stop();
        startServer();
        getCurrentId();
        getUserBikeReservationsNull();
        getUserReservations();
        getUserItemsNull();
        getAllBikeReservationNull();

        List<Appointment> appList = new ArrayList<>();
        appList.add(getAppointment(reservation));
        assertEquals(appList.get(0).getId(), CalendarPaneLogic.getAllCalendarItems().get(0).getId());
    }

    /**
     * Tests getAllCalendarItems method for null responses from the server.
     * The mock server gets shut down and restarted again with the correct endpoints.
     */
    @Test
    void getAllCalendarItemsNull2() {
        mockServer.stop();
        startServer();
        getCurrentId();
        getUserReservationsNull();
        getUserItemsNull();
        getUserBikeReservations();
        getAllBikeReservation();
        getUserItemsNull();

        List<Appointment> appList = new ArrayList<>();
        appList.add(getAppointment(bikeReservation));
        assertEquals(appList.get(0).getId(), CalendarPaneLogic.getAllCalendarItems().get(0).getId());
    }

    /**
     * Tests getAllCalendarItems method.
     * The mock server gets shut down and restarted again with the correct endpoints.
     */
    @Test
    void getAllCalendarItems() {
        mockServer.stop();
        startServer();
        getCurrentId();
        getUserItems();
        getUserReservations();
        getUserBikeReservations();
        getAllBikeReservation();
        List<Appointment> appList = new ArrayList<>();
        appList.add(getAppointment(reservation));
        appList.add(getAppointment(item));
        appList.add(getAppointment(bikeReservation));
        List<Appointment> list = CalendarPaneLogic.getAllCalendarItems();
        assertEquals(appList.get(0).getId(), list.get(0).getId());
        assertEquals(appList.get(1).getId(), list.get(1).getId());
        assertEquals(appList.get(2).getId(), list.get(2).getId());
    }

    /**
     * Tests serverCreateItem method.
     * The mock server gets shut down and restarted again with the correct endpoints.
     */
    @Test
    void serverCreateItem() {
        assertFalse(CalendarPaneLogic.serverCreateItem(null));
        Reservation r = new Reservation(857, "ethan", 504,
                "2020-04-01", "12:00:00", "16:30:00");
        assertTrue(CalendarPaneLogic.serverCreateItem(getAppointment(r)));
    }

    /**
     * Tests serverCreateItem method with null responses from the server.
     * The mock server gets shut down and restarted again with the correct endpoints.
     */
    @Test
    void serverCreateItemError() {
        mockServer.stop();
        startServer();
        getCurrentIdError();
        getUserItems();
        getUserReservations();
        getUserBikeReservations();
        getAllBikeReservation();
        Reservation r = new Reservation(857, "ethan", 504,
                "2020-04-01", "12:00:00", "16:30:00");
        assertFalse(CalendarPaneLogic.serverCreateItem(getAppointment(r)));
    }

    /**
     * Helper method to create an appointment object from an abstractCalendarItem object.
     *
     * @param r the abstractCalendarItem.
     * @return the appointment.
     */
    private Appointment getAppointment(AbstractCalendarItem r) {
        Appointment app = new Appointment();
        app.setId(r.getId());
        app.setHeaderText(r.getHeader());
        app.setDescriptionText(r.getDescription());
        app.setStartTime(r.getStartTime());
        app.setEndTime(r.getEndTime());
        app.setLocked(true);
        app.setAllowMove(false);
        app.getStyle().setFillColor(r.getColor());

        return app;
    }
}