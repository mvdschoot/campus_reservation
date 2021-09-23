package nl.tudelft.oopp.demo.user.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SearchViewLogicTest {

    private static List<Room> rooms;
    private static Room r1;
    private static Room r2;
    private static Room r3;
    private static Room r4;
    private static Room r5;

    private static List<Building> buildings;
    private static Building b1;
    private static Building b2;
    private static Building b3;

    private static List<Reservation> reservations;
    private static Reservation rs1;
    private static Reservation rs2;
    private static Reservation rs3;
    private static Reservation rs4;
    private static Reservation rs5;
    private static Reservation rs6;
    private static Reservation rs7;


    /**
     * makes a List of rooms to filter on.
     */
    @BeforeEach
    public void makeRooms() {
        rooms = new ArrayList<Room>();
        r1 = new Room(1, "Room 1", 1, true, 4, "picture.jpg", "nice room", "lecture hall");
        r2 = new Room(2, "Room 2", 1, true, 12, "picture.jpg", "nice room", "lecture hall");
        r3 = new Room(3, "Room 3", 2, false, 7, "picture.jpg", "nice room", "lecture hall");
        r4 = new Room(4, "Room 4", 2, false, 20, "picture.jpg", "nice room", "lecture hall");
        r5 = new Room(5, "Room 5", 3, false, 45, "picture.jpg", "nice room", "lecture hall");

        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
        rooms.add(r4);
        rooms.add(r5);
    }

    /**
     * makes buildings to filter on.
     *
     * @return
     */
    public List<Building> makeBuildings() {
        buildings = new ArrayList<>();
        b1 = new Building(1, "Building 1", 2,
                "BuildingStreet 1", 5, "08:00", "22:00");
        b2 = new Building(2, "Building 2", 2,
                "BuildingStreet 2", 10, "08:00", "22:00");
        b3 = new Building(3, "Building 3", 1,
                "BuildingStreet 3",
                20, "09:30:00", "21:30:00");

        buildings.add(b1);
        buildings.add(b2);
        buildings.add(b3);

        return buildings;
    }

    /**
     * Makes a list of reservations to filter on.
     *
     * @return
     */
    public List<Reservation> makeReservations() {
        reservations = new ArrayList<Reservation>();

        rs1 = new Reservation(1, "Test", 1, "2020-05-05",
                "08:00:00", "23:59:00");
        rs2 = new Reservation(2, "Test", 2, "2020-05-05",
                "09:30:00", "23:59:00");
        rs3 = new Reservation(3, "Test", 2, "2020-05-05",
                "08:00:00", "08:30:00");
        rs4 = new Reservation(4, "Test", 3, "2020-05-05",
                "08:00:00", "23:59:00");
        rs5 = new Reservation(5, "Test", 3, "2020-06-05",
                "08:30:00", "23:59:00");
        rs6 = new Reservation(6, "Test", 3, "2020-06-05",
                "08:00:00", "08:30:00");
        rs7 = new Reservation(7, "Test", 5, "2020-06-05",
                "10:30:00", "11:30:00");


        reservations.add(rs1);
        reservations.add(rs2);
        reservations.add(rs3);
        reservations.add(rs4);
        reservations.add(rs5);
        reservations.add(rs6);
        reservations.add(rs7);

        return reservations;
    }

    /**
     * tests the filtering of rooms by buildingId.
     */
    @Test
    void filterRoomByBuilding() {
        List<Room> expected = new ArrayList<Room>();

        expected.add(r1);
        expected.add(r2);

        assertEquals(null, SearchViewLogic.filterRoomByBuilding(null, 1));
        List<Room> empty = new ArrayList<Room>();
        assertEquals(empty, SearchViewLogic.filterRoomByBuilding(empty, 1));

        assertEquals(expected, SearchViewLogic.filterRoomByBuilding(rooms, 1));

    }

    /**
     * tests the filtering of rooms by if they are teacherOnly.
     */
    @Test
    void filterRoomByTeacherOnly() {
        List<Room> expected = new ArrayList<Room>();

        expected.add(r1);
        expected.add(r2);

        assertEquals(null, SearchViewLogic.filterRoomByTeacherOnly(null, false));
        List<Room> empty = new ArrayList<Room>();
        assertEquals(empty, SearchViewLogic.filterRoomByTeacherOnly(empty, true));

        assertEquals(expected, SearchViewLogic.filterRoomByTeacherOnly(rooms, true));
    }

    /**
     * tests the filtering of rooms by capacity.
     */
    @Test
    void filterRoomByCapacity() {
        List<Room> expected1 = new ArrayList<Room>();
        expected1.add(r1);
        assertEquals(expected1, SearchViewLogic.filterRoomByCapacity(rooms, "1-5"));

        List<Room> expected2 = new ArrayList<Room>();
        makeRooms();
        expected2.add(r3);
        assertEquals(expected2, SearchViewLogic.filterRoomByCapacity(rooms, "5-10"));

        List<Room> expected3 = new ArrayList<Room>();
        makeRooms();
        expected3.add(r2);
        expected3.add(r4);
        assertEquals(expected3, SearchViewLogic.filterRoomByCapacity(rooms, "10-20"));

        List<Room> expected4 = new ArrayList<Room>();
        makeRooms();
        expected4.add(r4);
        expected4.add(r5);
        assertEquals(expected4, SearchViewLogic.filterRoomByCapacity(rooms, "20+"));

        makeRooms();
        assertEquals(rooms, SearchViewLogic.filterRoomByCapacity(rooms, "this is not calculated"));

        assertEquals(null, SearchViewLogic.filterRoomByCapacity(null, "Hello"));
        List<Room> empty = new ArrayList<Room>();
        assertEquals(empty, SearchViewLogic.filterRoomByCapacity(empty, "Hello"));
    }

    /**
     * tests the filtering of rooms and buildings by searchInput.
     */
    @Test
    void filterBySearch() {
        makeBuildings();
        makeRooms();
        Room r6 = new Room(6, "Room 63", 3,
                false, 45, "picture.jpg",
                "nice room", "lecture hall");
        rooms.add(r6);

        List<Room> expected = new ArrayList<Room>();
        expected.add(r5);
        expected.add(r6);
        expected.add(r3);

        assertEquals(expected, SearchViewLogic.filterBySearch(rooms, "3", buildings));
    }

    /**
     * tests the filtering of rooms by if they are available on given date.
     */
    @Test
    void filterRoomsByDate() {
        makeBuildings();
        makeReservations();

        List<Room> expected = new ArrayList<Room>();
        expected.add(r2);
        expected.add(r4);
        expected.add(r5);

        SearchViewLogic.filterRoomsByDate(rooms, "2020-05-05", reservations, buildings);
        assertEquals(expected, rooms);

        makeRooms();
        makeBuildings();
        makeReservations();
        expected = new ArrayList<Room>();
        expected.add(r1);
        expected.add(r2);
        expected.add(r4);
        expected.add(r5);

        SearchViewLogic.filterRoomsByDate(rooms, "2020-06-05", reservations, buildings);
        assertEquals(expected, rooms);
    }

    /**
     * tests the filtering of rooms by if there are enough bikes on the building.
     */
    @Test
    void filterByBike() {
        makeBuildings();

        List<Room> expected = new ArrayList<Room>();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        expected.add(r4);
        expected.add(r5);


        assertEquals(expected, SearchViewLogic.filterByBike(rooms, buildings, "1+"));

        expected = new ArrayList<Room>();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        expected.add(r4);
        expected.add(r5);

        assertEquals(expected, SearchViewLogic.filterByBike(rooms, buildings, "5+"));

        expected = new ArrayList<Room>();
        expected.add(r3);
        expected.add(r4);
        expected.add(r5);

        assertEquals(expected, SearchViewLogic.filterByBike(rooms, buildings, "10+"));

        expected = new ArrayList<Room>();
        expected.add(r5);

        assertEquals(expected, SearchViewLogic.filterByBike(rooms, buildings, "20+"));

        expected = new ArrayList<Room>();
        makeRooms();
        expected.add(r1);
        expected.add(r2);
        expected.add(r3);
        expected.add(r4);
        expected.add(r5);

        assertEquals(expected, SearchViewLogic.filterByBike(rooms, buildings, "pizza"));
    }

    @Test
    /**
     * tests if  the filtering by food works.
     */
    public void filterByFoodTest() {
        makeRooms();
        makeBuildings();

        buildings.remove(b1);
        List<Integer> buildingsWithFood = new ArrayList<Integer>();
        for (int i = 0; i != buildings.size(); i++) {
            buildingsWithFood.add(buildings.get(i).getBuildingId().get());
        }


        List<Room> expected = new ArrayList<Room>();
        expected.add(r3);
        expected.add(r4);
        expected.add(r5);

        assertEquals(expected, SearchViewLogic.filterByFood(rooms, buildingsWithFood));

    }
}