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

import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.BikeReservationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class that tests the Bike reservation controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(BikeReservationController.class)
class BikeReservationControllerTest {

    @MockBean
    private BikeReservationRepository repo;

    @Autowired
    private MockMvc mvc;

    private BikeReservation br1;
    private BikeReservation br2;
    private BikeReservation br3;
    private List<BikeReservation> brList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        br1 = new BikeReservation(1, 20, new User("test", "passHASHED1", 0),
                3, "2020-03-10", "12:00", "15:00");
        br2 = new BikeReservation(1, 10, new User("test", "passHASHED1", 0),
                13, "2020-03-09", "14:00", "15:00");
        br3 = new BikeReservation(1, 20, new User("hello", "passHASHED3", 2),
                8, "2020-12-09", "19:00", "22:00");
        brList = Arrays.asList(br1, br2, br3);
    }

    /**
     * Test for createBikeReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createBikeReservationTest() throws Exception {
        mvc.perform(post("/createBikeReservation?building=20&user=3&numBikes=33&date=2020-03-10"
                + "&startingTime=12:00&endingTime=16:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).insertBikeReservation(anyInt(), anyString(), anyInt(),
                anyString(), anyString(), anyString());
        mvc.perform(post("/createBikeReservation?building=0&user=3&numBikes=33&date=2020-03-10"
                + "&startingTime=12:00&endingTime=16:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateBikeReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateBikeReservationTest() throws Exception {
        mvc.perform(post("/updateBikeReservation?id=1&building=20&user=3&numBikes=33&date=2020-03-10"
                + "&startingTime=12:00&endingTime=16:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).updateBikeNum(anyInt(), anyInt());
        mvc.perform(post("/updateBikeReservation?id=1&building=20&user=3&numBikes=3&date=2020-03-10"
                + "&startingTime=12:00&endingTime=16:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteBikeReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteBikeReservationTest() throws Exception {
        mvc.perform(post("/deleteBikeReservation?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteBikeReservation(anyInt());
        mvc.perform(post("/deleteBikeReservation?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getBikeReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getBikeReservationTest() throws Exception {
        when(repo.getBikeReservation(anyInt())).thenReturn(br2);

        mvc.perform(get("/getBikeReservation?id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is(br2.getDate())));

        when(repo.getBikeReservation(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getBikeReservation?id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getBikeReservations method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getBikeReservationsTest() throws Exception {
        when(repo.getAllBikeReservations()).thenReturn(brList);

        mvc.perform(get("/getAllBikeReservation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].date", is(br2.getDate())));

        when(repo.getAllBikeReservations()).thenThrow(new TestAbortedException());

        mvc.perform(get("/getAllBikeReservation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getBuildingBikeReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getBuildingBikeReservationTest() throws Exception {
        when(repo.getBuildingBikeReservations(anyInt())).thenReturn(brList);

        mvc.perform(get("/getBuildingBikeReservations?building=33")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].date", is(br2.getDate())));

        when(repo.getBuildingBikeReservations(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getBuildingBikeReservations?building=33")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getUserBikeReservations method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getUserBikeReservationsTest() throws Exception {
        when(repo.getUserBikeReservations(anyString())).thenReturn(brList);

        mvc.perform(get("/getUserBikeReservations?user=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].date", is(br2.getDate())));

        when(repo.getUserBikeReservations(anyString())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getUserBikeReservations?user=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}