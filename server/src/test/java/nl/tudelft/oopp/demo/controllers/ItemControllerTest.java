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

import nl.tudelft.oopp.demo.entities.Item;
import nl.tudelft.oopp.demo.repositories.ItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Test class that tests the item controller.
 * It makes use of Mockito MVC which is a part of the Mockito framework.
 */
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRepository repo;

    private Item i1;
    private Item i2;
    private Item i3;
    private List<Item> itemList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        i1 = new Item(1, "test", "title", "2020-09-08", "08:00",
                "09:00", "description");
        i2 = new Item(2, "test2", "title2", "2020-09-12", "08:00",
                "19:00", "description2");
        i3 = new Item(3, "test", "title3", "2020-09-13", "19:00",
                "22:00", "description3");
        itemList = Arrays.asList(i1, i2, i3);
    }

    /**
     * Test for getItem method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getItemTest() throws Exception {
        when(repo.getItem(anyInt())).thenReturn(i1);

        mvc.perform(get("/getItem?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is(i1.getDate())));

        when(repo.getItem(anyInt())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getItem?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getAllItems method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getAllItemsTest() throws Exception {
        when(repo.getAllItems()).thenReturn(itemList);
        mvc.perform(get("/getAllItems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].date", is(i2.getDate())));
        when(repo.getAllItems()).thenThrow(new TestAbortedException());
        mvc.perform(get("/getAllItems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for createItem method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void createItemTest() throws Exception {
        mvc.perform(post("/createItem?user=test&title=title&date=2020-08-08"
                + "&startingTime=12:00&endingTime=16:00&description=description2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).insertItem(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString());
        mvc.perform(post("/createItem?user=test&title=title&date=2020-08-08"
                + "&startingTime=12:00&endingTime=16:00&description=description2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for deleteItem method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void deleteItemTest() throws Exception {
        mvc.perform(post("/deleteItem?id=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        doThrow(new TestAbortedException()).when(repo).deleteItem(anyInt());
        mvc.perform(post("/deleteItem?id=2")
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
        when(repo.getCurrentId()).thenReturn(55);

        mvc.perform(get("/currentId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(55)));

        when(repo.getCurrentId()).thenThrow(new TestAbortedException());

        mvc.perform(get("/currentId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test for getUserItems method.
     *
     * @throws Exception if any exception with the connection (or other) occurs
     */
    @Test
    void getUserItemsTest() throws Exception {
        when(repo.getUserItems(anyString())).thenReturn(itemList);

        mvc.perform(get("/getUserItems?user=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].date", is(i3.getDate())));

        when(repo.getUserItems(anyString())).thenThrow(new TestAbortedException());

        mvc.perform(get("/getUserItems?user=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}