package nl.tudelft.oopp.demo.controllers;

import java.util.List;

import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.repositories.BikeReservationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class BikeReservationController {

    @Autowired
    private BikeReservationRepository bikeResRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Adds a bike reservation to the database.
     *
     * @param building     The Building id to which the bikes belong
     * @param user         The username of the user making the reservation
     * @param numBikes     The number of bikes reserved
     * @param date         The date of the reservation
     * @param startingTime The starting time of the reservation
     * @param endingTime   The ending time of the reservation
     * @return boolean true if database query was successful, false otherwise
     */
    @PostMapping("createBikeReservation")
    @ResponseBody
    public boolean createBikeReservation(@RequestParam int building, @RequestParam String user,
                                         @RequestParam int numBikes, @RequestParam String date,
                                         @RequestParam String startingTime, @RequestParam String endingTime) {
        try {
            user = CommunicationMethods.decodeCommunication(user);
            date = CommunicationMethods.decodeCommunication(date);
            startingTime = CommunicationMethods.decodeCommunication(startingTime);
            endingTime = CommunicationMethods.decodeCommunication(endingTime);
            bikeResRepo.insertBikeReservation(building, user, numBikes, date, startingTime, endingTime);
            logger.info("Bike Reservation: -create- Building: " + building + " - User: " + user
                    + " - Number of bikes: " + numBikes + " - date: " + date + " - Starting time: " + startingTime
                    + " - Ending time: " + endingTime);
        } catch (Exception e) {
            logger.error("Bike Reservation: -create- ERROR", e);
            return false;
        }
        return true;
    }

    /**
     * Updates a previously made bike reservation.
     *
     * @param id           The id of the reservation
     * @param building     The new building of the reservation
     * @param user         The new user of the reservation
     * @param numBikes     The new number of bikes reserved
     * @param date         The new date of the reservation
     * @param startingTime The new starting time of the reservation
     * @param endingTime   The new ending time of the reservation
     * @return boolean true if database query was successful, false otherwise
     */
    @PostMapping("updateBikeReservation")
    @ResponseBody
    public boolean updateBikeReservation(@RequestParam int id, @RequestParam int building,
                                         @RequestParam String user, @RequestParam int numBikes,
                                         @RequestParam String date, @RequestParam String startingTime,
                                         @RequestParam String endingTime) {
        try {
            user = CommunicationMethods.decodeCommunication(user);
            date = CommunicationMethods.decodeCommunication(date);
            startingTime = CommunicationMethods.decodeCommunication(startingTime);
            endingTime = CommunicationMethods.decodeCommunication(endingTime);
            bikeResRepo.updateBikeNum(id, numBikes);
            bikeResRepo.updateBuilding(id, building);
            bikeResRepo.updateUser(id, user);
            bikeResRepo.updateDate(id, date);
            bikeResRepo.updateStartingTime(id, startingTime);
            bikeResRepo.updateEndingTime(id, endingTime);
            logger.info("Bike Reservation: -update- BikeReservation ID: " + id + " - NEW data -> Building ID"
                    + building + " - User: " + user + " - Number of bikes: " + numBikes + " - date: "
                    + date + " - Starting time: " + startingTime + " - Ending time: " + endingTime);
        } catch (Exception e) {
            logger.error("Bike Reservation: -update- ERROR", e);
            return false;
        }
        return true;
    }


    /**
     * Deletes a bike reservation from the database.
     *
     * @param id The id of the bike reservation
     * @return boolean true if database query was successful, false otherwise
     */
    @PostMapping("deleteBikeReservation")
    @ResponseBody
    public boolean deleteBikeReservation(@RequestParam int id) {
        try {
            bikeResRepo.deleteBikeReservation(id);
            logger.info("Bike Reservation: -delete- ID: " + id);
        } catch (Exception e) {
            logger.error("Bike Reservation: - delete- ERROR", e);
            return false;
        }
        return true;
    }

    /**
     * Rretreives a bike reservation from the database by id.
     *
     * @param id The id of the bike reservation
     * @return Returns a BikeReservation object
     */
    @GetMapping("getBikeReservation")
    @ResponseBody
    public BikeReservation getBikeReservation(@RequestParam int id) {
        try {
            return bikeResRepo.getBikeReservation(id);
        } catch (Exception e) {
            logger.error("Bike Reservation -get- ERROR", e);
        }
        return null;
    }


    /**
     * Retrieves all bike reservations from the database.
     *
     * @return Returns a list of BikeReservations
     */
    @GetMapping("getAllBikeReservation")
    @ResponseBody
    public List<BikeReservation> getBikeReservations() {
        try {
            return bikeResRepo.getAllBikeReservations();
        } catch (Exception e) {
            logger.error("Bike Reservation -getAll- ERROR", e);
        }
        return null;
    }


    /**
     * Retrieves all the bike reservations of the bikes that belong to the building of buildingId.
     *
     * @param building The id of the building
     * @return Returns a list of bike reservations.
     */
    @GetMapping("getBuildingBikeReservations")
    @ResponseBody
    public List<BikeReservation> getBuildingBikeReservation(@RequestParam int building) {
        try {
            return bikeResRepo.getBuildingBikeReservations(building);
        } catch (Exception e) {
            logger.error("Bike Reservation -getBuildingBikeReservations- ERROR", e);
        }
        return null;
    }


    /**
     * Retrieves all bike reservations that have been made by a user.
     *
     * @param user The username of the particular user
     * @return Returns a list of bike reservations
     */
    @GetMapping("getUserBikeReservations")
    @ResponseBody
    public List<BikeReservation> getUserBikeReservations(@RequestParam String user) {
        try {
            user = CommunicationMethods.decodeCommunication(user);
            return bikeResRepo.getUserBikeReservations(user);
        } catch (Exception e) {
            logger.error("Bike Reservation -getUserBikeReservations- ERROR", e);
        }
        return null;
    }
}
