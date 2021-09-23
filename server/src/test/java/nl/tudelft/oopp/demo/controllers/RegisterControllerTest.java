package nl.tudelft.oopp.demo.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.oopp.demo.entities.User;
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
 * Test class that tests the register controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

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
     * Test for register method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void registerTest() throws Exception {

        when(userRepo.getUser(anyString())).thenReturn(new User("test", "pass", 2));

        mvc.perform(post("/register?username=test&password=pass&userType=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(userRepo.getUser(anyString())).thenReturn(null);

        mvc.perform(post("/register?username=test&password=pass&userType=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        when(userRepo.getUser(anyString())).thenThrow(new TestAbortedException());

        mvc.perform(post("/register?username=test&password=pass&userType=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }
}