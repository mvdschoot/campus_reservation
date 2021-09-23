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
import nl.tudelft.oopp.demo.entities.Item;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.BikeReservationRepository;
import nl.tudelft.oopp.demo.repositories.ItemRepository;
import nl.tudelft.oopp.demo.repositories.ReservationsRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class that tests the user controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository repo;
    @MockBean
    private ReservationsRepository reservationRepo;
    @MockBean
    private ItemRepository itemRepo;
    @MockBean
    private BikeReservationRepository bikeResRepo;

    private User u1;
    private User u2;
    private User u3;
    private Reservations r1;
    private BikeReservation br1;
    private Item i1;
    private List<User> userList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        i1 = new Item(1, "test", "title", "2020-09-08", "08:00",
                "09:00", "description");
        br1 = new BikeReservation(1, 20, new User("test", "passHASHED1", 0),
                3, "2020-03-10", "12:00", "15:00");
        u1 = new User("user1", "passHASHED1", 0);
        u2 = new User("user2", "passHASHED2", 1);
        u3 = new User("user3", "passHASHED3", 2);
        userList = Arrays.asList(u1, u2, u3);
    }

    /**
     * Test for createUser method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createUserTest() throws Exception {
        mvc.perform(post("/createUser?username=test&password=hello&type=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).insertUser(anyString(), anyString(), anyInt());
        mvc.perform(post("/createUser?username=test&password=hello&type=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateUser method (with password).
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateUserWithPasswordTest() throws Exception {
        mvc.perform(post("/updateUser2?username=test&type=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).updateType(anyString(), anyInt());
        mvc.perform(post("/updateUser2?username=test&type=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for updateUser method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void updateUserTest() throws Exception {
        mvc.perform(post("/updateUser?username=test&password=hello&type=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).updateType(anyString(), anyInt());
        mvc.perform(post("/updateUser?username=test&password=hello&type=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteUser method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteUserTest() throws Exception {
        when(reservationRepo.getUserReservations(anyString())).thenReturn(Arrays.asList(r1));
        when(itemRepo.getUserItems(anyString())).thenReturn(Arrays.asList(i1));
        when(bikeResRepo.getUserBikeReservations(anyString())).thenReturn(Arrays.asList(br1));
        mvc.perform(post("/deleteUser?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        doThrow(new TestAbortedException()).when(repo).deleteUser(anyString());
        mvc.perform(post("/deleteUser?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getUser method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getUserTest() throws Exception {
        when(repo.getUser(anyString())).thenReturn(u3);

        mvc.perform(get("/getUser?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(u3.getUsername())));

        when(repo.getUser(anyString())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getUser?username=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllUsers method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllUsers() throws Exception {
        when(repo.getAllUsers()).thenReturn(userList);

        mvc.perform(get("/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].password", is(u2.getPassword())));

        when(repo.getAllUsers()).thenThrow(new TestAbortedException());

        mvc.perform(get("/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}