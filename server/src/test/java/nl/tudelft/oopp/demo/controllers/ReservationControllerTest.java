package nl.tudelft.oopp.demo.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.repositories.ReservationsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class that tests the register controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationsRepository repo;

    private Reservations r1;
    private Reservations r2;
    private Reservations r3;
    private List<Reservations> resList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        r2 = new Reservations(2, "test", 36, "2020-09-02", "08:00",
                "12:00");
        r3 = new Reservations(3, "test2", 13, "2020-09-02", "08:00",
                "12:00");
        resList = Arrays.asList(r1, r2, r3);
    }

    /**
     * Test for createReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createReservationTest() throws Exception {
        mvc.perform(post("/createReservation?username=user1&room=2&date=2020-06-04"
                + "&startingTime=08:30&endingTime=10:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).insertReservation(anyString(), anyInt(),
                anyString(), anyString(), anyString());

        mvc.perform(post("/createReservation?username=user1&room=2&date=2020-06-04"
                + "&startingTime=08:30&endingTime=10:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getCurrentId method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getCurrentIdTest() throws Exception {
        when(repo.getCurrentId()).thenReturn(33);

        mvc.perform(get("/currentReservationId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(33)));

        when(repo.getCurrentId()).thenThrow(new TestAbortedException());

        mvc.perform(get("/currentReservationId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateReservationTest() throws Exception {
        when(repo.getReservation(anyInt())).thenReturn(r2);
        mvc.perform(post("/updateReservation?id=4&username=user1&room=2&date=2020-06-04"
                + "&startingTime=08:30&endingTime=10:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(repo.getReservation(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(post("/updateReservation?id=4&username=user1&room=2&date=2020-06-04"
                + "&startingTime=08:30&endingTime=10:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteReservationTest() throws Exception {
        mvc.perform(post("/deleteReservation?id=4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteReservation(anyInt());
        mvc.perform(post("/deleteReservation?id=4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getReservationTest() throws Exception {
        when(repo.getReservation(anyInt())).thenReturn(r1);

        mvc.perform(get("/getReservation?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.room", is(r1.getRoom())));

        when(repo.getReservation(anyInt())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getReservation?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllReservations method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllReservationsTest() throws Exception {
        when(repo.getAllReservations()).thenReturn(resList);

        mvc.perform(get("/getAllReservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].room", is(r3.getRoom())));

        when(repo.getAllReservations()).thenThrow(new TestAbortedException());

        mvc.perform(get("/getAllReservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getUserReservations method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getUserReservationsTest() throws Exception {
        when(repo.getUserReservations(anyString())).thenReturn(resList);
        mvc.perform(get("/getUserReservations?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].room", is(r1.getRoom())));

        when(repo.getUserReservations(anyString())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getUserReservations?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}