package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Reservations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Test Class that tests the repository for Reservations.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class ReservationsRepositoryTest {

    @Mock
    private ReservationsRepository resRepo;

    private Reservations r1;
    private Reservations r2;
    private Reservations r3;
    private List<Reservations> resList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Reservations(1, "test", 33, "2020-09-02", "08:00",
                "12:00");
        r2 = new Reservations(2, "test", 36, "2020-09-02", "08:00",
                "12:00");
        r3 = new Reservations(3, "test2", 13, "2020-09-02", "08:00",
                "12:00");
        resList = Arrays.asList(r1, r2, r3);
    }

    /**
     * Test for getAllReservations method.
     */
    @Test
    void getAllReservationsTest() {
        when(resRepo.getAllReservations()).thenReturn(resList);
        assertEquals(resList, resRepo.getAllReservations());
    }

    /**
     * Test for getReservation method.
     */
    @Test
    void getReservationTest() {
        when(resRepo.getReservation(anyInt())).thenReturn(r1);
        assertEquals(r1, resRepo.getReservation(1));
    }

    /**
     * Test for getCurrentId method.
     */
    @Test
    void getCurrentIdTest() {
        when(resRepo.getCurrentId()).thenReturn(4);
        assertEquals(4, resRepo.getCurrentId());
    }

    /**
     * Test for insertReservation method.
     */
    @Test
    void insertReservationTest() {
        resRepo.insertReservation("Hello", 33, "2020-09-14", "08:00",
                "12:00");
        verify(resRepo, times(1)).insertReservation("Hello", 33,
                "2020-09-14", "08:00", "12:00");
    }

    /**
     * Test for deleteReservation method.
     */
    @Test
    void deleteReservationTest() {
        resRepo.deleteReservation(2);
        verify(resRepo, times(1)).deleteReservation(2);
    }

    /**
     * Test for updateDate method.
     */
    @Test
    void updateDateTest() {
        resRepo.updateDate(3, "2020-07-04");
        verify(resRepo, times(1)).updateDate(3, "2020-07-04");
    }

    /**
     * Test for updateStartingTime method.
     */
    @Test
    void updateStartingTimeTest() {
        resRepo.updateStartingTime(3, "12:00");
        verify(resRepo, times(1)).updateStartingTime(3, "12:00");
    }

    /**
     * Test for updateEndingTime method.
     */
    @Test
    void updateEndingTimeTest() {
        resRepo.updateEndingTime(3, "15:00");
        verify(resRepo, times(1)).updateEndingTime(3, "15:00");
    }

    /**
     * Test for updateUsername method.
     */
    @Test
    void updateUsernameTest() {
        resRepo.updateUsername(3, "testHello");
        verify(resRepo, times(1)).updateUsername(3, "testHello");
    }

    /**
     * Test for updateRoom method.
     */
    @Test
    void updateRoomTest() {
        resRepo.updateRoom(3, 12);
        verify(resRepo, times(1)).updateRoom(3, 12);
    }

    /**
     * Test for getUserReservations method.
     */
    @Test
    void getUserReservationsTest() {
        when(resRepo.getUserReservations(anyString())).thenReturn(Arrays.asList(r1, r3));
        assertEquals(Arrays.asList(r1, r3), resRepo.getUserReservations("test"));
    }
}
