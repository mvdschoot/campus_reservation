package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Class that tests the repository for Bike Reservations.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class BikeReservationRepositoryTest {

    @Mock
    private BikeReservationRepository brRepo;

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
     * Test for getAllBikeReservations method.
     */
    @Test
    void getAllBikeReservations() {
        when(brRepo.getAllBikeReservations()).thenReturn(brList);
        assertEquals(brList, brRepo.getAllBikeReservations());
    }

    /**
     * Test for getBikeReservation method.
     */
    @Test
    void getBikeReservationTest() {
        when(brRepo.getBikeReservation(anyInt())).thenReturn(br1);
        assertEquals(br2, brRepo.getBikeReservation(2));
    }

    /**
     * Test for getBuildingBikeReservations method.
     */
    @Test
    void getBuildingBikeReservationsTest() {
        when(brRepo.getBuildingBikeReservations(anyInt())).thenReturn(Arrays.asList(br1, br3));
        assertEquals(Arrays.asList(br1, br3), brRepo.getBuildingBikeReservations(20));
    }

    /**
     * Test for getUserBikeReservations method.
     */
    @Test
    void getUserBikeReservationsTest() {
        when(brRepo.getUserBikeReservations(anyString())).thenReturn(Arrays.asList(br2, br3));
        assertEquals(Arrays.asList(br2, br3), brRepo.getUserBikeReservations("test"));
    }

    /**
     * Test for insertBikeReservation method.
     */
    @Test
    void insertBikeReservationTest() {
        brRepo.insertBikeReservation(15, "test", 20, "2020-03-07",
                "12:00", "21:00");
        verify(brRepo, times(1)).insertBikeReservation(15, "test",
                20, "2020-03-07", "12:00", "21:00");
    }

    /**
     * Test for deleteBikeReservation method.
     */
    @Test
    void deleteBikeReservationTest() {
        brRepo.deleteBikeReservation(15);
        verify(brRepo, times(1)).deleteBikeReservation(15);
    }

    /**
     * Test for updateBikeNum method.
     */
    @Test
    void updateBikeNumTest() {
        brRepo.updateBikeNum(15, 20);
        verify(brRepo, times(1)).updateBikeNum(15, 20);
    }

    /**
     * Test for updateBuilding method.
     */
    @Test
    void updateBuildingTest() {
        brRepo.updateBuilding(15, 20);
        verify(brRepo, times(1)).updateBuilding(15, 20);
    }

    /**
     * Test for updateUser method.
     */
    @Test
    void updateUserTest() {
        brRepo.updateUser(15, "hello");
        verify(brRepo, times(1)).updateUser(15, "hello");
    }

    /**
     * Test for updateDate method.
     */
    @Test
    void updateDateTest() {
        brRepo.updateDate(15, "2020-09-09");
        verify(brRepo, times(1)).updateDate(15, "2020-09-09");
    }

    /**
     * Test for updateStartingTime method.
     */
    @Test
    void updateStartingTimeTest() {
        brRepo.updateStartingTime(15, "08:00");
        verify(brRepo, times(1)).updateStartingTime(15, "08:00");
    }

    /**
     * Test for updateEndingTime method.
     */
    @Test
    void updateEndingTimeTest() {
        brRepo.updateEndingTime(15, "09:00");
        verify(brRepo, times(1)).updateEndingTime(15, "09:00");
    }
}