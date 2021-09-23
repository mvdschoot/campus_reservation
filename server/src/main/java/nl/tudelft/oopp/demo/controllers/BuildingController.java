package nl.tudelft.oopp.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;

import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.BikeReservationRepository;
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
public class BuildingController {

    @Autowired
    private BuildingRepository buildingRepo;

    @Autowired
    private BikeReservationRepository bikeResRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private ReservationsRepository reservationsRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Adds a building to the database.
     *
     * @param name        The name of the building.
     * @param roomCount   The amount of of rooms inside the building.
     * @param address     The address of the building.
     * @param maxBikes    The max number of bikes, int.
     * @param openingTime the opening time of the building, String.
     * @param closingTime the closing time of the building, String.
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("createBuilding")
    @ResponseBody
    public void createBuilding(@RequestParam String name, @RequestParam int roomCount,
                               @RequestParam String address, @RequestParam int maxBikes,
                               @RequestParam String openingTime, @RequestParam String closingTime)
            throws UnsupportedEncodingException {

        name = CommunicationMethods.decodeCommunication(name);
        address = CommunicationMethods.decodeCommunication(address);
        openingTime = CommunicationMethods.decodeCommunication(openingTime);
        closingTime = CommunicationMethods.decodeCommunication(closingTime);

        try {
            buildingRepo.insertBuilding(name, roomCount, address, maxBikes, openingTime, closingTime);
            logger.info("Building: -create- Name: " + name + " - Room count: " + roomCount
                    + " - Address: " + address + " - Max bikes: " + maxBikes
                    + " - Opening hours: " + openingTime + " - " + closingTime);
        } catch (Exception e) {
            logger.error("Building: -create- ERROR", e);
        }
    }


    /**
     * Changes the existing building with the provided ID in the database with the provides parameters.
     *
     * @param name        The name of the building.
     * @param roomCount   The amount of of rooms inside the building.
     * @param address     The address of the building.
     * @param maxBikes    The max number of bikes, int.
     * @param openingTime the opening time of the building, String.
     * @param closingTime the closing time of the building, String.
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding
     */
    @PostMapping("updateBuilding")
    @ResponseBody
    public void updateBuilding(@RequestParam int id, @RequestParam String name,
                               @RequestParam int roomCount, @RequestParam String address,
                               @RequestParam int maxBikes, @RequestParam String openingTime,
                               @RequestParam String closingTime) throws UnsupportedEncodingException {

        name = CommunicationMethods.decodeCommunication(name);
        address = CommunicationMethods.decodeCommunication(address);
        openingTime = CommunicationMethods.decodeCommunication(openingTime);
        closingTime = CommunicationMethods.decodeCommunication(closingTime);

        try {
            buildingRepo.updateAddress(id, address);
            buildingRepo.updateName(id, name);
            buildingRepo.updateMaxBikes(id, maxBikes);
            buildingRepo.updateOpeningHours(id, openingTime, closingTime);
            int count = roomRepo.getRoomByBuilding(id).size();
            buildingRepo.updateRoomCount(id, count);
            logger.info("Building: -update- Building ID: " + id + " - NEW data -> Name: "
                    + name + " - Address: " + address + " - Max bikes: " + maxBikes + " - Opening hours: "
                    + openingTime + " - " + closingTime);
        } catch (Exception e) {
            logger.error("Building: -update- ERROR", e);
        }
    }

    /**
     * Deletes the building from the database.
     *
     * @param id The ID of the building you delete.
     */
    @PostMapping("deleteBuilding")
    @ResponseBody
    public void deleteBuilding(@RequestParam int id) {
        try {
            final List<Room> rooms = roomRepo.getRoomByBuilding(id);
            final List<BikeReservation> bikeReservations = bikeResRepo.getBuildingBikeReservations(id);

            int counter;
            for (counter = 0; counter < rooms.size(); counter++) {
                int roomID = rooms.get(counter).getId();
                logger.info("Room -delete- Room ID: " + roomID);
                final List<Reservations> reservations = reservationsRepo.getReservationByRoom(roomID);
                for (int counter2 = 0; counter2 < reservations.size(); counter2++) {
                    logger.info("Reservation: -delete- ID: " + reservations.get(counter2).getId());
                }
            }

            for (counter = 0; counter < bikeReservations.size(); counter++) {
                logger.info("Bike Reservation: -delete- ID: " + bikeReservations.get(counter).getId());
            }

            buildingRepo.deleteBuilding(id);
            logger.info("Building: -delete- Building ID: " + id);
        } catch (Exception e) {
            logger.error("Building -delete- ERROR", e);
        }
    }

    /**
     * Returns the database entry of the building.
     *
     * @param id The ID of the building you requested info of.
     * @return Returns a Json representation of a building
     */
    @GetMapping("getBuilding")
    @ResponseBody
    public Building getBuilding(@RequestParam int id) {
        try {
            return buildingRepo.getBuilding(id);
        } catch (Exception e) {
            logger.error("Building -get- ERROR", e);
        }
        return null;
    }

    /**
     * Returns a list of buildings that sell a particular food.
     *
     * @param id the Food id.
     * @return Returns a list
     */
    @GetMapping("getBuildingByFoodId")
    @ResponseBody
    public List<Building> getBuildingByFoodId(@RequestParam int id) {
        try {
            return buildingRepo.getBuildingByFoodId(id);
        } catch (Exception e) {
            logger.error("Building -getBuildingByFoodId- ERROR", e);
        }
        return null;
    }

    /**
     * Returns all the buildings.
     *
     * @return Returns a List of Building in Json
     */
    @GetMapping("getAllBuildings")
    @ResponseBody
    public List<Building> getAllBuildings() {
        try {
            return buildingRepo.getAllBuildings();
        } catch (Exception e) {
            logger.error("Building -getAll- ERROR", e);
        }
        return null;
    }

}
