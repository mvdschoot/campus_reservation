package nl.tudelft.oopp.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.BuildingRepository;
import nl.tudelft.oopp.demo.repositories.ReservationsRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RoomController {

    @Autowired
    private RoomRepository roomRepo;
    
    @Autowired
    private BuildingRepository buildingRepo;

    @Autowired
    private ReservationsRepository reservationRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Creates a Room entry in the database.
     *
     * @param name        Name of the room
     * @param building    Name of the building (must be an existing building)
     * @param teacherOnly True if it's teacher only, False otherwise
     * @param capacity    capacity of the room
     * @param photos      URL's to photo's of the room
     * @param description Piece of text that describes the room
     * @param type        The type of the room (e.g. lecture hall)
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("createRoom")
    @ResponseBody
    public void createRoom(@RequestParam String name, @RequestParam int building,
                           @RequestParam boolean teacherOnly, @RequestParam int capacity,
                           @RequestParam String photos, @RequestParam String description,
                           @RequestParam String type) throws UnsupportedEncodingException {

        name = CommunicationMethods.decodeCommunication(name);
        photos = CommunicationMethods.decodeCommunication(photos);
        description = CommunicationMethods.decodeCommunication(description);
        type = CommunicationMethods.decodeCommunication(type);

        try {
            roomRepo.insertRoom(name, building, teacherOnly, capacity, photos, description, type);
            int count = roomRepo.getRoomByBuilding(building).size();
            buildingRepo.updateRoomCount(building, count);
            
            logger.info("Room: -create- Name: " + name + " - Building ID: " + building + " - Teacher only: "
                    + String.valueOf(teacherOnly) + " - Capacity: " + capacity + " - Photo URL: " + photos
                    + " - Type: " + type + " - Description: " + description);
        } catch (Exception e) {
            logger.error("Room: -create- ERROR", e);
        }
    }

    /**
     * Replaces the values in the database with the provided ones.
     *
     * @param id          The ID of the entry to be updated
     * @param name        The new room name
     * @param building    The new building name (must be an existing building)
     * @param teacherOnly The new teacherOnly
     * @param capacity    The new capacity
     * @param photos      New URL's to photos of the room
     * @param description New description
     * @param type        New room-type
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("updateRoom")
    @ResponseBody
    public void updateRoom(@RequestParam int id, @RequestParam String name,
                           @RequestParam int building, @RequestParam boolean teacherOnly,
                           @RequestParam int capacity, @RequestParam String photos,
                           @RequestParam String description, @RequestParam String type)
            throws UnsupportedEncodingException {

        name = CommunicationMethods.decodeCommunication(name);
        photos = CommunicationMethods.decodeCommunication(photos);
        description = CommunicationMethods.decodeCommunication(description);
        type = CommunicationMethods.decodeCommunication(type);

        try {
            int oldBuilding = roomRepo.getRoom(id).getBuilding();
            if (oldBuilding != building) {
                int count = roomRepo.getRoomByBuilding(oldBuilding).size();
                buildingRepo.updateRoomCount(oldBuilding, count - 1);

                count = roomRepo.getRoomByBuilding(building).size();
                buildingRepo.updateRoomCount(building, count + 1);
            }
            roomRepo.updateCapacity(id, capacity);
            roomRepo.updateDescription(id, description);
            roomRepo.updateBuilding(id, building);
            roomRepo.updateName(id, name);
            roomRepo.updatePhotos(id, photos);
            roomRepo.updateTeacherOnly(id, teacherOnly);
            roomRepo.updateType(id, type);
            logger.info("Room: -update- ID: " + id + " - NEW data -> Name: " + name + " - Building ID: "
                    + building + " - Teacher only: " + String.valueOf(teacherOnly) + " - Capacity: "
                    + capacity + " - Photo URL: " + photos + " - Type: " + type
                    + " - Description: " + description);
        } catch (Exception e) {
            logger.error("Room: -update- ERROR", e);
        }
    }

    /**
     * removes the room with the provided id from the database.
     *
     * @param id The ID of the to be removed room.
     */
    @PostMapping("deleteRoom")
    @ResponseBody
    public void deleteRoom(@RequestParam int id) {
        try {
            final List<Reservations> reservations = reservationRepo.getReservationByRoom(id);
            int buildingId = roomRepo.getRoom(id).getBuilding();
            roomRepo.deleteRoom(id);
            int roomCount = roomRepo.getRoomByBuilding(buildingId).size();
            buildingRepo.updateRoomCount(buildingId, roomCount);
            logger.info("Room: -delete- ID: " + id);

            for (int counter = 0; counter < reservations.size(); counter++) {
                logger.info("Reservation: -delete- ID: " + reservations.get(counter).getId());
            }
        } catch (Exception e) {
            logger.error("Room: -delete- ERROR", e);
        }
    }

    /**
     * Retrieves a room with the specified id.
     *
     * @param id The id of the to be retrieved room.
     * @return A Room objects in Json
     */
    @GetMapping("getRoom")
    @ResponseBody
    public Room getRoom(@RequestParam int id) {
        try {
            return roomRepo.getRoom(id);
        } catch (Exception e) {
            logger.error("Room: -get- ERROR", e);
        }
        return null;
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return A list of Room objects in Json
     */
    @GetMapping("getAllRooms")
    @ResponseBody
    public List<Room> getAllRooms() {
        try {
            return roomRepo.getAllRooms();
        } catch (Exception e) {
            logger.error("Room: -getAllRooms- ERROR", e);
        }
        return null;
    }

}
