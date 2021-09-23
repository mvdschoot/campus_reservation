package nl.tudelft.oopp.demo.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.FoodReservations;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.repositories.FoodRepository;
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
 * Test class that tests the Food controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FoodRepository repo;
    @MockBean
    private ReservationsRepository reservationRepo;
    @MockBean
    private ReservationController controller;

    private Food f1;
    private Food f2;
    private Food f3;
    private Reservations res1;
    private FoodReservations fr1;
    private List<Food> foodList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        res1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        fr1 = new FoodReservations(f2, res1, 2);
        f1 = new Food(1, "Pizza", 10.99);
        f2 = new Food(2, "Water", 0.88);
        f3 = new Food(3, "Pancakes", 8.76);
        foodList = Arrays.asList(f1, f2, f3);
    }

    /**
     * Test for createFood method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createFoodTest() throws Exception {
        mvc.perform(post("/createFood?name=test&price=0.99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).insertFood(anyString(), anyDouble());
        mvc.perform(post("/createFood?name=test&price=0.89")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for addFoodToBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void addFoodToBuildingTest() throws Exception {
        mvc.perform(post("/addFoodToBuilding?food=9&building=33")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).addFoodToBuilding(anyInt(), anyInt());
        mvc.perform(post("/addFoodToBuilding?food=9&building=33")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for addFoodToReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void addFoodToReservationTest() throws Exception {
        mvc.perform(post("/addFoodToReservation?food=9&reservation=222&quantity=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).addFoodToReservation(anyInt(), anyInt(), anyInt());
        mvc.perform(post("/addFoodToReservation?food=9&reservation=222&quantity=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateFood method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateFoodTest() throws Exception {
        mvc.perform(post("/updateFood?id=2&name=test&price=0.88")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).updateName(anyInt(), anyString());
        mvc.perform(post("/updateFood?id=2&name=test&price=0.88")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteFoodFromReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteFoodFromReservationTest() throws Exception {
        mvc.perform(post("/deleteFoodFromReservation?food=2&reservation=12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteFoodReservation(anyInt(), anyInt());
        mvc.perform(post("/deleteFoodFromReservation?food=2&reservation=12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteFoodFromBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteFoodFromBuildingTest() throws Exception {
        mvc.perform(post("/deleteFoodFromBuilding?food=2&building=9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteFoodBuilding(anyInt(), anyInt());
        mvc.perform(post("/deleteFoodFromBuilding?food=2&building=9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateFoodReservationQuantity method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateFoodReservationQuantityTest() throws Exception {
        mvc.perform(post("/updateFoodReservationQuantity?food=1&reservation=1&quantity=30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).updateFoodReservationQuantity(anyInt(),
                anyInt(), anyInt());
        mvc.perform(post("/updateFoodReservationQuantity?food=1&reservation=1&quantity=30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteFood method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteFoodTest() throws Exception {
        mvc.perform(post("/deleteFood?id=9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteFood(anyInt());
        mvc.perform(post("/deleteFood?id=9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getFood method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getFoodTest() throws Exception {
        when(repo.getFood(anyInt())).thenReturn(f1);
        mvc.perform(get("/getFood?id=8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(f1.getName())));

        when(repo.getFood(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getFood?id=8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getFoodByReservation method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getFoodByReservationTest() throws Exception {
        when(repo.getFoodByReservationId(anyInt())).thenReturn(foodList);
        mvc.perform(get("/getFoodByReservation?reservation=143")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is(f3.getName())));

        when(repo.getFoodByReservationId(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getFoodByReservation?reservation=143")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getFoodByBuildingId method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getFoodByBuildingIdTest() throws Exception {
        when(repo.getFoodByBuildingId(anyInt())).thenReturn(foodList);
        mvc.perform(get("/getFoodByBuildingId?building=23")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is(f2.getName())));

        when(repo.getFoodByBuildingId(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getFoodByBuildingId?building=23")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllFood method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllFoodTest() throws Exception {
        when(repo.getAllFood()).thenReturn(foodList);
        mvc.perform(get("/getAllFood")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(f1.getName())));

        when(repo.getAllFood()).thenThrow(new TestAbortedException());
        mvc.perform(get("/getAllFood")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getFoodReservationByReservationTest() throws Exception {
        List<Object[]> l = new ArrayList<>();
        l.add(new Integer[]{1, 2, 2});
        when(repo.getFoodReservationByReservationId(anyInt())).thenReturn(l);
        mvc.perform(get("/getFoodReservationByReservation?reservation=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        when(repo.getFoodReservationByReservationId(anyInt())).thenThrow(new TestAbortedException());
        mvc.perform(get("/getFoodReservationByReservation?reservation=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFoodReservationsTest() throws Exception {
        List<Object[]> l = new ArrayList<>();
        l.add(new Integer[]{1, 2, 2});
        when(repo.getAllFoodReservations()).thenReturn(l);
        mvc.perform(get("/getAllFoodReservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        when(repo.getAllFoodReservations()).thenThrow(new TestAbortedException());
        mvc.perform(get("/getAllFoodReservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}