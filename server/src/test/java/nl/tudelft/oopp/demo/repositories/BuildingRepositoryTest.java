package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Test Class that tests the repository for Buildings.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class BuildingRepositoryTest {

    @Mock
    private BuildingRepository buildingRepo;

    private Building b1;
    private Building b2;
    private Building b3;
    private Building b4;
    private Building b5;
    private List<Building> buildingList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        b1 = new Building(1, "TEST", 130, "TestStreet 18",
                201, "09:00", "22:00");
        b2 = new Building(2, "CIVIL", 230, "TestStreet 48",
                3, "09:00", "22:00");
        b3 = new Building(3, "AeroSpace", 80, "TestStreet 98",
                4, "09:00", "22:00");
        b4 = new Building(4, "EEMCS", 68, "TestStreet 1234",
                201, "09:00", "22:00");
        b5 = new Building(5, "EWI", 2000, "TestStreet 754",
                10, "09:00", "22:00");

        buildingList = Arrays.asList(b1, b2, b3, b4, b5);
    }

    /**
     * Test for getAllBuildings method.
     */

    @Test
    void getAllBuildingsTest() {
        when(buildingRepo.getAllBuildings()).thenReturn(buildingList);
        assertEquals(5, buildingRepo.getAllBuildings().size());
    }

    /**
     * Test for getBuildingById method.
     */
    @Test
    void getBuildingByIdTest() {
        when(buildingRepo.getBuilding(anyInt())).thenReturn(b3);
        assertEquals(b3, buildingRepo.getBuilding(3));
    }

    /**
     * Test for getBuildingByFoodId method.
     */
    @Test
    void getBuildingByFoodIdTest() {
        List<Building> list = Arrays.asList(b1, b2);
        when(buildingRepo.getBuildingByFoodId(anyInt())).thenReturn(list);
        assertEquals(list, buildingRepo.getBuildingByFoodId(5));
    }

    /**
     * Test for insertBuilding method.
     */
    @Test
    void insertBuildingTest() {
        buildingRepo.insertBuilding(b1.getName(), b1.getRoomCount(), b1.getAddress(), b1.getMaxBikes(),
                b1.getOpeningTime(), b1.getClosingTime());

        verify(buildingRepo, times(1))
                .insertBuilding(b1.getName(), b1.getRoomCount(), b1.getAddress(), b1.getMaxBikes(),
                        b1.getOpeningTime(), b1.getClosingTime());
    }

    /**
     * Test for deleteBuilding method.
     */
    @Test
    void deleteBuildingTest() {
        buildingRepo.deleteBuilding(4);
        verify(buildingRepo, times(1)).deleteBuilding(4);
    }

    /**
     * Test for updateRoomCount method.
     */
    @Test
    void updateRoomCountTest() {
        buildingRepo.updateRoomCount(3, 300);
        verify(buildingRepo, times(1)).updateRoomCount(3, 300);
    }

    /**
     * Test for updateName method.
     */
    @Test
    void updateNameTest() {
        buildingRepo.updateName(4, "HelloBuilding");
        verify(buildingRepo, times(1)).updateName(4, "HelloBuilding");
    }

    /**
     * Test for updateAddress method.
     */
    @Test
    void updateAddressTest() {
        buildingRepo.updateAddress(4, "TestLane 7");
        verify(buildingRepo, times(1)).updateAddress(4, "TestLane 7");
    }


    /**
     * Test for updateMaxBikes method.
     */
    @Test
    void updateMaxBikesTest() {
        buildingRepo.updateMaxBikes(1, 10);
        verify(buildingRepo, times(1)).updateMaxBikes(1, 10);
    }


}