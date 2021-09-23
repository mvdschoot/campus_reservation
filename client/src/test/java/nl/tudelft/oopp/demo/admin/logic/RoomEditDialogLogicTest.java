package nl.tudelft.oopp.demo.admin.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.demo.entities.Building;

import org.junit.jupiter.api.Test;

class RoomEditDialogLogicTest {

    /**
     * Test for isValidInput.
     */
    @Test
    void isValidInput() {
        String roomName = "";
        Building b = new Building(1, "Building 1", 2,
                "BuildingStreet 1", 5, "08:00", "22:00");
        boolean radioButtonYes = true;
        boolean radiButtoNo = false;
        String roomCapaciy = "22";
        String roomType = "nice room";
        String roomDescription = "It is a nice room to study in";
        String oldfile = "old";
        String fileName = "new";
        boolean changed = false;

        assertEquals("No valid room name!\n", RoomEditDialogLogic.isValidInput(roomName,
                b, radioButtonYes, radiButtoNo, roomCapaciy, roomType, roomDescription, oldfile,
                changed, fileName));


        roomName = "CoolRoom";
        roomCapaciy = "";
        assertEquals("No valid capacity!\n", RoomEditDialogLogic.isValidInput(roomName, b,
                radioButtonYes, radiButtoNo, roomCapaciy, roomType, roomDescription, oldfile,
                changed, fileName));

        roomCapaciy = "22";
        roomType = "";
        assertEquals("No valid room type!\n", RoomEditDialogLogic.isValidInput(roomName, b,
                radioButtonYes, radiButtoNo, roomCapaciy, roomType, roomDescription, oldfile, changed,
                fileName));


        roomType = "lecture hall";
        roomDescription = "";
        assertEquals("No valid room description!\n", RoomEditDialogLogic.isValidInput(roomName, b,
                radioButtonYes, radiButtoNo, roomCapaciy, roomType, roomDescription, oldfile, changed,
                fileName));

        roomDescription = "Nice room to study in";
        assertEquals("", RoomEditDialogLogic.isValidInput(roomName, b, radioButtonYes, radiButtoNo,
                roomCapaciy, roomType, roomDescription, oldfile, changed, fileName));

        radioButtonYes = false;
        radiButtoNo = false;
        assertEquals("No teacher only button selected!\n", RoomEditDialogLogic.isValidInput(roomName,
                b, radioButtonYes, radiButtoNo, roomCapaciy, roomType, roomDescription, oldfile, changed,
                fileName));
    }
}