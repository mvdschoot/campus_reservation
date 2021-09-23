package nl.tudelft.oopp.demo.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
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

import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.entities.Room;
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
 * Test class that tests the room controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomRepository repo;
    @MockBean
    private BuildingRepository buildingRepo;
    @MockBean
    private ReservationsRepository reservationsRepo;

    private Room r1;
    private Room r2;
    private Room r3;
    private Reservations res1;
    private List<Room> roomList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        res1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        r1 = new Room(1, "room1", 22, true, 20, "photo1.jpg",
                "description", "Project room");
        r2 = new Room(2, "room2", 22, false, 10, "photo2.jpg",
                "description", "Project room");
        r3 = new Room(3, "room3", 24, false, 2, "photo3.jpg",
                "description", "Project room");
        roomList = Arrays.asList(r1, r2, r3);
    }

    /**
     * Test for createRoom method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createRoomTest() throws Exception {
        mvc.perform(post("/createRoom?name=room1&building=2&teacherOnly=true&capacity=30"
                + "&photos=photo.png&description=hello&type=type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).insertRoom(anyString(), anyInt(), anyBoolean(),
                anyInt(), anyString(), anyString(), anyString());
        mvc.perform(post("/createRoom?name=room1&building=2&teacherOnly=true&capacity=30"
                + "&photos=photo.png&description=hello&type=type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateRoom method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateRoomTest() throws Exception {
        when(repo.getRoom(anyInt())).thenReturn(r1);
        when(repo.getRoomByBuilding(anyInt())).thenReturn(roomList);
        mvc.perform(post("/updateRoom?id=3&name=room1&building=2&teacherOnly=true&capacity=30"
                + "&photos=photo.png&description=hello&type=type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/updateRoom?id=3&name=room1&building=22&teacherOnly=true&capacity=30"
                + "&photos=photo.png&description=hello&type=type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).updateBuilding(anyInt(), anyInt());
        mvc.perform(post("/updateRoom?id=3&name=room1&building=2&teacherOnly=true&capacity=30"
                + "&photos=photo.png&description=hello&type=type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteRoom method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteRoomTest() throws Exception {
        when(repo.getRoom(anyInt())).thenReturn(r1);
        when(repo.getRoomByBuilding(anyInt())).thenReturn(roomList);
        when(reservationsRepo.getReservationByRoom(anyInt())).thenReturn(new ArrayList<>());
        mvc.perform(post("/deleteRoom?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(reservationsRepo.getReservationByRoom(anyInt())).thenReturn(Arrays.asList(res1));
        mvc.perform(post("/deleteRoom?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).deleteRoom(anyInt());
        mvc.perform(post("/deleteRoom?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getRoom method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getRoomTest() throws Exception {
        when(repo.getRoom(anyInt())).thenReturn(r2);

        mvc.perform(get("/getRoom?id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(r2.getName())));

        when(repo.getRoom(anyInt())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getRoom?id=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllRooms method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllRoomsTest() throws Exception {
        when(repo.getAllRooms()).thenReturn(roomList);

        mvc.perform(get("/getAllRooms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name", is(r2.getName())));

        when(repo.getAllRooms()).thenThrow(new TestAbortedException());

        mvc.perform(get("/getAllRooms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}