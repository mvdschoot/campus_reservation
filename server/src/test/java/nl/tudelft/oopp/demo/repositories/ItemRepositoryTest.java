package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Class that tests the repository for Items.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class ItemRepositoryTest {

    @Mock
    private ItemRepository itemRepo;

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
     * Test for getAllItems method.
     */
    @Test
    void getAllItemsTest() {
        when(itemRepo.getAllItems()).thenReturn(itemList);
        assertEquals(itemList, itemRepo.getAllItems());
    }

    /**
     * Test for getItem method.
     */
    @Test
    void getItemTest() {
        when(itemRepo.getItem(anyInt())).thenReturn(i1);
        assertEquals(i1, itemRepo.getItem(1));
    }

    /**
     * Test for insertItem method.
     */
    @Test
    void insertItemTest() {
        itemRepo.insertItem("testUser", "title4", "2020-08-07", "07:00",
                "09:30", "description2");
        verify(itemRepo, times(1)).insertItem("testUser", "title4",
                "2020-08-07", "07:00", "09:30", "description2");
    }

    /**
     * Test for deleteItem method.
     */
    @Test
    void deleteItemTest() {
        itemRepo.deleteItem(2);
        verify(itemRepo).deleteItem(2);
    }

    /**
     * Test for getCurrentId method.
     */
    @Test
    void getCurrentIdTest() {
        when(itemRepo.getCurrentId()).thenReturn(4);
        assertEquals(4, itemRepo.getCurrentId());
    }

    /**
     * Test for getUserItems method.
     */
    @Test
    void getUserItemsTest() {
        when(itemRepo.getUserItems(anyString())).thenReturn(Arrays.asList(i2, i3));
        assertEquals(Arrays.asList(i2, i3), itemRepo.getUserItems("Hello"));
    }
}