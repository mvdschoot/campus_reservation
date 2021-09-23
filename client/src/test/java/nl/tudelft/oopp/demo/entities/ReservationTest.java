package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.mindfusion.common.DateTime;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.admin.controller.AdminManageUserViewController;
import nl.tudelft.oopp.demo.user.calendar.logic.CalendarPaneLogic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationTest {

    private Reservation reservation;

    private ClientAndServer mockServer;

    /**
     * Sets mock server response for /getAllReservations.
     */
    void expGetAllReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllReservations"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"username\":\"username\",\"room\":20,\"date\":\"2020-04-04\","
                        + "\"startingTime\":\"12:00:00\",\"endingTime\":\"13:00:00\"}]"));
    }

    /**
     * Sets wrong mock server response for /getAllReservations.
     */
    void expGetAllReservationsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getAllReservations"))
                .respond(response().withStatusCode(200).withBody("\"id\":10,"
                        + "\"username\":\"username\",\"room\":20,\"date\":\"2020-04-04\","
                        + "\"startingTime\":\"12:00:00\",\"endingTime\":\"13:00:00\"}"));
    }

    /**
     * Sets mock server response for /getUserReservations.
     */
    void expGetUserReservations() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getUserReservations"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"username\":\"username\",\"room\":20,\"date\":\"2020-04-04\","
                        + "\"startingTime\":\"12:00:00\",\"endingTime\":\"13:00:00\"}]"));
    }

    /**
     * Sets wrong mock server response for /getUserReservations.
     */
    void expGetUserReservationsError() {
        new MockServerClient("127.0.0.1", 8080)
                .when(request().withMethod("GET").withPath("/getUserReservations"))
                .respond(response().withStatusCode(200).withBody("[{\"id\":10,"
                        + "\"username\":\"username\",\"room\":20,\"date\":\"2020-04-04\","
                        + "\"startingTime\":\"12:00:00\",\"endingTime\":\"13:00:00\"}"));
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
        reservation = new Reservation(10, "username", 20,
                "2020-04-04", "12:00:00", "13:00:00");
    }

    /**
     * Tests empty constructor.
     */
    @Test
    void emptyConstructor() {
        reservation = new Reservation();
        assertEquals(-1, reservation.getReservationId().get());
        assertNull(reservation.getUsername().get());
        assertEquals(-1, reservation.getRoom().get());
        assertNull(reservation.getDate().get());
        assertNull(reservation.getReservationStartingTime().get());
        assertNull(reservation.getReservationEndingTime().get());
    }

    /**
     * Tests the getReservationId method.
     */
    @Test
    void getId() {
        assertEquals(10, reservation.getReservationId().get());
    }

    /**
     * Tests the getUsername method.
     */
    @Test
    void getUsername() {
        assertEquals("username", reservation.getUsername().get());
    }

    /**
     * Tests the getRoom method.
     */
    @Test
    void getRoom() {
        assertEquals(20, reservation.getRoom().get());
    }

    /**
     * Tests the getDate method.
     */
    @Test
    void getDate() {
        assertEquals("2020-04-04", reservation.getDate().get());
    }

    /**
     * Tests the getReservationStartingTime method.
     */
    @Test
    void getStartingTime() {
        assertEquals("12:00:00", reservation.getReservationStartingTime().get());
    }

    /**
     * Tests the getReservationEndingTime method.
     */
    @Test
    void getEndingTime() {
        assertEquals("13:00:00", reservation.getReservationEndingTime().get());
    }

    /**
     * Tests the setId method.
     */
    @Test
    void setId() {
        reservation.setId(5);
        assertEquals(5, reservation.getReservationId().get());
    }

    /**
     * Tests the setUsername method.
     */
    @Test
    void setUsername() {
        reservation.setUsername("newusername");
        assertEquals("newusername", reservation.getUsername().get());
    }

    /**
     * Tests the setRoom method.
     */
    @Test
    void setRoom() {
        reservation.setRoom(50);
        assertEquals(50, reservation.getRoom().get());
    }

    /**
     * Tests the setDate method.
     */
    @Test
    void setDate() {
        reservation.setDate("newdate");
        assertEquals("newdate", reservation.getDate().get());
    }

    /**
     * Tests the setStartingTime method.
     */
    @Test
    void setStartingTime() {
        reservation.setStartingTime("newstartingtime");
        assertEquals("newstartingtime", reservation.getReservationStartingTime().get());
    }

    /**
     * Tests the setEndingTime method.
     */
    @Test
    void setEndingTime() {
        reservation.setEndingTime("newendingtime");
        assertEquals("newendingtime", reservation.getReservationEndingTime().get());
    }

    /**
     * Tests the equals method.
     */
    @Test
    void equals() {
        final Reservation testRes = new Reservation(1, "", 0, "", "", "");
        final Reservation testRes2 = new Reservation(10, "", 0, "", "", "");
        Integer integer = 2;

        assertEquals(reservation, reservation);
        assertNotEquals(reservation, integer);
        assertNotEquals(reservation, testRes);
        assertEquals(reservation, testRes2);
    }

    /**
     * Tests the getRoomReservationsOnDate method.
     */
    @Test
    void getRoomReservationsOnDate() {
        LocalDate ld = LocalDate.of(2020, 4, 4);

        expGetAllReservations();
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        reservationList.add(reservation);
        assertEquals(reservationList, Reservation.getRoomReservationsOnDate(20, ld, getConverter()));
        stopServer();
        startServer();
        expGetAllReservationsError();
        assertNull(Reservation.getRoomReservationsOnDate(20, ld, getConverter()));
    }

    /**
     * Tests the getAllReservations method.
     */
    @Test
    void getReservation() {
        expGetAllReservations();
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        reservationList.add(reservation);
        assertEquals(reservationList, Reservation.getAllReservations());
        stopServer();
        startServer();
        expGetAllReservationsError();
        assertNull(Reservation.getAllReservations());
    }

    /**
     * Tests the getUserReservation method.
     */
    @Test
    void getUserReservation() {
        expGetUserReservations();
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        reservationList.add(reservation);
        assertEquals(reservationList, Reservation.getUserReservation());
        stopServer();
        startServer();
        expGetUserReservationsError();
        assertNull(Reservation.getUserReservation());
    }

    /**
     * Tests the getSelectedUserReservation method.
     */
    @Test
    void getSelectedUserReservation() {
        stopServer();
        startServer();
        AdminManageUserViewController.currentSelectedUser = new User("username",
                "pass", 0);
        expGetUserReservations();
        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
        reservationList.add(reservation);
        assertEquals(reservationList, Reservation.getSelectedUserReservation());
        stopServer();
        startServer();
        expGetUserReservationsError();
        assertNull(Reservation.getSelectedUserReservation());
    }

    /**
     * Tests the getId method.
     */
    @Test
    void testGetId() {
        assertEquals("10", reservation.getId());
    }

    /**
     * Tests the getHeader method.
     */
    @Test
    void getHeader() {
        assertEquals("Reservation", reservation.getHeader());
    }

    /**
     * Tests the getStartTime method.
     */
    @Test
    void getStartTime() {
        DateTime datetime = new DateTime(2020, 4, 4, 12, 0, 0);
        assertEquals(datetime, reservation.getStartTime());
    }

    /**
     * Tests the getEndTime method.
     */
    @Test
    void getEndTime() {
        DateTime datetime = new DateTime(2020, 4, 4, 13, 0, 0);
        assertEquals(datetime, reservation.getEndTime());
    }

    /**
     * Tests the getDescription function.
     */
    @Test
    void getDescription() {
        final Food f = new Food(1, "Pizza", 10);
        final FoodReservation fr = new FoodReservation(10, 1, 20);
        final Building b = new Building(1, "test",
                0, null, 0, null, null);
        final Room r = new Room(20, "test", 1, false,
                0, null, null, null);
        CalendarPaneLogic.foodList = Arrays.asList(f);
        CalendarPaneLogic.foodReservationList = Arrays.asList(fr);
        CalendarPaneLogic.buildingList = Arrays.asList(b);
        CalendarPaneLogic.roomList = Arrays.asList(r);
        String expected = "test\ntest\n12:00 - 13:00\n20x Pizza\ntotal price = 200.0 euro(s)";
        assertEquals(expected, reservation.getDescription());
    }

    /**
     * Tests the getColor function.
     */
    @Test
    void getColor() {
        assertEquals(Color.CYAN, reservation.getColor());
    }

    /**
     * Gets a Date string converter for the getRoomReservationsOnDate test function.
     * @return Returns a StringConverter object
     */
    private StringConverter<LocalDate> getConverter() {
        try {
            return new StringConverter<>() {
                // set the wanted pattern (format)
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        // get correctly formatted String
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        // get correct LocalDate from String format
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}