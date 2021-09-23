package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Class that tests the repository for Rooms.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class RoomRepositoryTest {

    @Mock
    private RoomRepository roomRepo;

    private Room r1;
    private Room r2;
    private Room r3;
    private List<Room> roomList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        r1 = new Room(1, "room1", 22, true, 20, "photo1.jpg",
                "description", "Project room");
        r2 = new Room(2, "room2", 22, false, 10, "photo2.jpg",
                "description", "Project room");
        r3 = new Room(3, "room3", 24, false, 2, "photo3.jpg",
                "description", "Project room");
        roomList = Arrays.asList(r1, r2, r3);
    }

    /**
     * Test for getAllRooms method.
     */
    @Test
    void getAllRoomsTest() {
        when(roomRepo.getAllRooms()).thenReturn(roomList);
        assertEquals(roomList, roomRepo.getAllRooms());
    }

    /**
     * Test for getRoom method.
     */
    @Test
    void getRoomTest() {
        when(roomRepo.getRoom(anyInt())).thenReturn(r1);
        assertEquals(r1, roomRepo.getRoom(1));
    }

    /**
     * Test for insertRoom method.
     */
    @Test
    void insertRoomTest() {
        roomRepo.insertRoom("room1", 22, true, 20, "photo1.jpg",
                "description", "Project room");
        verify(roomRepo, times(1)).insertRoom("room1", 22, true,
                20, "photo1.jpg", "description", "Project room");
    }

    /**
     * Test for updateName method.
     */
    @Test
    void updateNameTest() {
        roomRepo.updateName(1, "name1");
        verify(roomRepo, times(1)).updateName(1, "name1");
    }

    /**
     * Test for updateType method.
     */
    @Test
    void updateTypeTest() {
        roomRepo.updateName(2, "type1");
        verify(roomRepo, times(1)).updateName(2, "type1");
    }

    /**
     * Test for updateTeacherOnly method.
     */
    @Test
    void updateTeacherOnlyTest() {
        roomRepo.updateTeacherOnly(3, true);
        verify(roomRepo, times(1)).updateTeacherOnly(3, true);
    }

    /**
     * Test for updateCapacity method.
     */
    @Test
    void updateCapacityTest() {
        roomRepo.updateCapacity(1, 22);
        verify(roomRepo, times(1)).updateCapacity(1, 22);
    }

    /**
     * Test for updatePhotos method.
     */
    @Test
    void updatePhotosTest() {
        roomRepo.updatePhotos(1, "photo1.png");
        verify(roomRepo, times(1)).updatePhotos(1, "photo1.png");
    }

    /**
     * Test for updateDescription method.
     */
    @Test
    void updateDescriptionTest() {
        roomRepo.updateDescription(3, "description1");
        verify(roomRepo, times(1)).updateDescription(3, "description1");
    }

    /**
     * Test for updateBuilding method.
     */
    @Test
    void updateBuildingTest() {
        roomRepo.updateBuilding(1, 20);
        verify(roomRepo, times(1)).updateBuilding(1, 20);
    }

    /**
     * Test for deleteRoom method.
     */
    @Test
    void deleteRoomTest() {
        roomRepo.deleteRoom(2);
        verify(roomRepo, times(1)).deleteRoom(2);
    }

    /**
     * Test for getRoomByBuilding method.
     */
    @Test
    void getRoomByBuildingTest() {
        when(roomRepo.getRoomByBuilding(20)).thenReturn(Arrays.asList(r2, r3));
        assertEquals(Arrays.asList(r2, r3), roomRepo.getRoomByBuilding(20));
    }
}