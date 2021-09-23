package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Class that tests the repository for Food.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class FoodRepositoryTest {

    @Mock
    private FoodRepository foodRepo;

    private Food f1;
    private Food f2;
    private Food f3;
    private List<Food> foodList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        f1 = new Food(1, "Pizza", 10.0);
        f2 = new Food(2, "Water", 0.99);
        f3 = new Food(3, "Chips", 2.50);
        foodList = Arrays.asList(f1, f2, f3);
    }

    /**
     * Test for getAllFood method.
     */
    @Test
    void getAllFoodTest() {
        when(foodRepo.getAllFood()).thenReturn(foodList);
        assertEquals(foodList, foodRepo.getAllFood());
    }

    /**
     * Test for getFood method.
     */
    @Test
    void getFoodTest() {
        when(foodRepo.getFood(anyInt())).thenReturn(f1);
        assertEquals(f1, foodRepo.getFood(1));
    }

    /**
     * Test for getFoodByBuildingId method.
     */
    @Test
    void getFoodByBuildingIdTest() {
        when(foodRepo.getFoodByBuildingId(anyInt())).thenReturn(Arrays.asList(f3));
        assertEquals(Arrays.asList(f3), foodRepo.getFoodByBuildingId(11));
    }

    /**
     * Test for getFoodByReservationId method.
     */
    @Test
    void getFoodByReservationIdTest() {
        when(foodRepo.getFoodByReservationId(anyInt())).thenReturn(Arrays.asList(f1, f2));
        assertEquals(Arrays.asList(f1, f2), foodRepo.getFoodByReservationId(1));
    }

    /**
     * Test for addFoodToBuilding method.
     */
    @Test
    void addFoodToBuildingTest() {
        foodRepo.addFoodToBuilding(2, 11);
        verify(foodRepo, times(1)).addFoodToBuilding(2, 11);
    }

    /**
     * Test for addFoodToReservation method.
     */
    @Test
    void addFoodToReservationTest() {
        foodRepo.addFoodToReservation(12, 2, 3);
        verify(foodRepo, times(1)).addFoodToReservation(12, 2, 3);
    }

    /**
     * Test for insertFood method.
     */
    @Test
    void insertFoodTest() {
        foodRepo.insertFood("Steak", 22.00);
        verify(foodRepo, times(1)).insertFood("Steak", 22.00);
    }

    /**
     * Test for deleteFood method.
     */
    @Test
    void deleteFoodTest() {
        foodRepo.deleteFood(2);
        verify(foodRepo, times(1)).deleteFood(2);
    }

    /**
     * Test for deleteFoodReservation method.
     */
    @Test
    void deleteFoodReservationTest() {
        foodRepo.deleteFoodReservation(12, 2);
        verify(foodRepo, times(1)).deleteFoodReservation(12, 2);
    }

    /**
     * Test for deleteFoodBuilding method.
     */
    @Test
    void deleteFoodBuildingTest() {
        foodRepo.deleteFoodBuilding(11, 3);
        verify(foodRepo, times(1)).deleteFoodBuilding(11, 3);
    }

    /**
     * Test for updateFoodReservationQuantity method.
     */
    @Test
    void updateFoodReservationQuantityTest() {
        foodRepo.updateFoodReservationQuantity(12, 2, 10);
        verify(foodRepo, times(1)).updateFoodReservationQuantity(12, 2, 10);
    }

    /**
     * Test for updateName method.
     */
    @Test
    void updateNameTest() {
        foodRepo.updateName(2, "Sparkling water");
        verify(foodRepo, times(1)).updateName(2, "Sparkling water");
    }

    /**
     * Test for updatePrice method.
     */
    @Test
    void updatePriceTest() {
        foodRepo.updatePrice(3, 100.67);
        verify(foodRepo, times(1)).updatePrice(3, 100.67);
    }
}