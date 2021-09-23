package nl.tudelft.oopp.demo.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class that tests the login controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepo;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {

    }

    /**
     * Test for getUser method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getUserTest() throws Exception {
        User u = new User("test", "d74ff0ee8da3b9806b18c877dbf29bbde5"
                + "0b5bd8e4dad7a3a725000feb82e8f1", 0);
        User u2 = new User("test", "d74ff0ee8da3b9806b18c877dbf29bbde5"
                + "0b5bd8e4dad7a3a725000feb82e8f1", 1);
        User u3 = new User("test", "d74ff0ee8da3b9806b18c877dbf29bbde5"
                + "0b5bd8e4dad7a3a725000feb82e8f1", 2);
        User u4 = new User("test", "pass", 0);
        User u5 = new User("test", "d74ff0ee8da3b9806b18c877dbf29bbde5"
                + "0b5bd8e4dad7a3a725000feb82e8f1", 4);
        when(userRepo.getUser(anyString())).thenReturn(null);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("not_found")));

        when(userRepo.getUser(anyString())).thenReturn(u4);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("wrong_password")));

        when(userRepo.getUser(anyString())).thenReturn(u);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("admin")));

        when(userRepo.getUser(anyString())).thenReturn(u2);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("teacher")));

        when(userRepo.getUser(anyString())).thenReturn(u3);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("student")));

        when(userRepo.getUser(anyString())).thenReturn(u5);
        mvc.perform(get("/login?username=test&password=pass")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("error")));
    }
}