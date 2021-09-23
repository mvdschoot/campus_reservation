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
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.BikeReservationRepository;
import nl.tudelft.oopp.demo.repositories.BuildingRepository;
import nl.tudelft.oopp.demo.repositories.ReservationsRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class that tests the building controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(BuildingController.class)
class BuildingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BuildingRepository repo;
    // following attributes are unused but needed as dependencies for the buildingRepository
    @MockBean
    private RoomRepository roomRepo;
    @MockBean
    private ReservationsRepository reservationsRepo;
    @MockBean
    private BikeReservationRepository bikesRepo;

    private Building b1;
    private Building b2;
    private Building b3;
    private Room r1;
    private Reservations res1;
    private BikeReservation br1;
    private List<Building> buildingList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        br1 = new BikeReservation(1, 20, new User("test", "passHASHED1", 0),
                3, "2020-03-10", "12:00", "15:00");
        res1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        r1 = new Room(1, "room1", 22, true, 20, "photo1.jpg",
                "description", "Project room");
        b1 = new Building(1, "TEST", 130, "TestStreet 18",
                201, "08:00", "22:00");
        b2 = new Building(2, "CIVIL", 230, "TestStreet 48",
                3, "08:00", "12:00");
        b3 = new Building(3, "AeroSpace", 80, "TestStreet 98",
                4, "07:30", "23:00");
        buildingList = Arrays.asList(b1, b2, b3);
    }

    /**
     * Test for createBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createBuildingTest() throws Exception {
        mvc.perform(post("/createBuilding?name=test&roomCount=200&"
                + "address=street5&maxBikes=200&openingTime=08:00&closingTime=22:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).insertBuilding(anyString(), anyInt(), anyString(),
                anyInt(), anyString(), anyString());
        mvc.perform(post("/createBuilding?name=test&roomCount=200&"
                + "address=street5&maxBikes=200&openingTime=08:00&closingTime=22:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateBuildingTest() throws Exception {
        mvc.perform(post("/updateBuilding?id=9&name=test&roomCount=66&address=street2&maxBikes=7 "
                + "&openingTime=08:00&closingTime=22:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).updateMaxBikes(anyInt(), anyInt());
        mvc.perform(post("/updateBuilding?id=9&name=test&roomCount=66&address=street2&maxBikes=7 "
                + "&openingTime=08:00&closingTime=22:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteBuildingTest() throws Exception {
        mvc.perform(post("/deleteBuilding?id=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).deleteBuilding(anyInt());

        mvc.perform(post("/deleteBuilding?id=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(roomRepo.getRoomByBuilding(anyInt())).thenReturn(Arrays.asList(r1));

        mvc.perform(post("/deleteBuilding?id=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(reservationsRepo.getReservationByRoom(anyInt())).thenReturn(Arrays.asList(res1));

        mvc.perform(post("/deleteBuilding?id=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(bikesRepo.getBuildingBikeReservations(anyInt())).thenReturn(Arrays.asList(br1));
        mvc.perform(post("/deleteBuilding?id=20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getBuilding method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getBuildingTest() throws Exception {
        when(repo.getBuilding(anyInt())).thenReturn(b1);

        mvc.perform(get("/getBuilding?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(b1.getName())));

        when(repo.getBuilding(anyInt())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getBuilding?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getBuildingByFoodId method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getBuildingByFoodIdTest() throws Exception {
        when(repo.getBuildingByFoodId(anyInt())).thenReturn(Arrays.asList(b2, b3));

        mvc.perform(get("/getBuildingByFoodId?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(b2.getName())));

        when(repo.getBuildingByFoodId(anyInt())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getBuildingByFoodId?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllBuildings method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllBuildingsTest() throws Exception {
        when(repo.getAllBuildings()).thenReturn(buildingList);

        mvc.perform(get("/getAllBuildings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is(b3.getName())));

        when(repo.getAllBuildings()).thenThrow(new TestAbortedException());
        mvc.perform(get("/getAllBuildings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}