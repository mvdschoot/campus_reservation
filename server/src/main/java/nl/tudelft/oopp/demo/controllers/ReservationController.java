package nl.tudelft.oopp.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.entities.Reservations;
import nl.tudelft.oopp.demo.repositories.ReservationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReservationController {

    @Autowired
    private ReservationsRepository reservationsRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Puts a new reservation in the database.
     *
     * @param username     The username of the person reserving.
     * @param room         The room being reserved.
     * @param date         The day the reservation starts //TODO date format
     * @param startingTime The starting time of the reservation //TODO time format
     * @param endingTime   The ending time of the reservation //TODO time format
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("createReservation")
    @ResponseBody
    public void createReservation(@RequestParam String username, @RequestParam int room,
                                  @RequestParam String date, @RequestParam String startingTime,
                                  @RequestParam String endingTime) throws UnsupportedEncodingException {

        username = CommunicationMethods.decodeCommunication(username);
        date = CommunicationMethods.decodeCommunication(date);
        startingTime = CommunicationMethods.decodeCommunication(startingTime);
        endingTime = CommunicationMethods.decodeCommunication(endingTime);

        try {
            reservationsRepo.insertReservation(username, room, date, startingTime, endingTime);
            logger.info("Reservation: -create- User: " + username + " - Room ID: " + room + " - Date: "
                    + date + " - Starting time: " + startingTime + " - Ending time: " + endingTime);
        } catch (Exception e) {
            logger.error("Reservation: -create- ERROR", e);
        }
    }

    /**
     * Gets the id of the last created reservation.
     * @return int the id of the reservation
     */
    @GetMapping("currentReservationId")
    @ResponseBody
    public int getCurrentId() {
        try {
            return reservationsRepo.getCurrentId();
        } catch (Exception e) {
            logger.error("Reservation: -getCurrentId- ERROR", e);
        }
        return -1;
    }

    /**
     * Replaces the existing info with the newly provided info.
     *
     * @param id           The reservation being updated.
     * @param room         The new value for room.
     * @param date         The new value for date. //TODO date-format
     * @param startingTime The new value for startingTime. //TODO time-format
     * @param endingTime   The new value for endingTime. //TODO time-format
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("updateReservation")
    @ResponseBody
    public void updateReservation(@RequestParam int id, @RequestParam int room, @RequestParam String date,
                                  @RequestParam String startingTime,
                                  @RequestParam String endingTime) throws UnsupportedEncodingException {

        date = CommunicationMethods.decodeCommunication(date);
        startingTime = CommunicationMethods.decodeCommunication(startingTime);
        endingTime = CommunicationMethods.decodeCommunication(endingTime);

        try {
            reservationsRepo.updateDate(id, date);
            reservationsRepo.updateRoom(id, room);
            reservationsRepo.updateStartingTime(id, startingTime);
            reservationsRepo.updateEndingTime(id, endingTime);
            String user = reservationsRepo.getReservation(id).getUsername();
            logger.info("Reservation: -update- ID: " + id + " - User: " + user + " - NEW data -> Room ID: "
                    + room + " - Date: " + date + " - Starting time: " + startingTime
                    + " - Ending time: " + endingTime);
        } catch (Exception e) {
            logger.error("Reservation: -update- ERROR", e);
        }
    }

    /**
     * Reservation gets removed.
     *
     * @param id The ID of the to be removed reservation.
     */
    @PostMapping("deleteReservation")
    @ResponseBody
    public void deleteReservation(@RequestParam int id) {
        try {
            reservationsRepo.deleteReservation(id);
            logger.info("Reservation: -delete- ID: " + id);
        } catch (Exception e) {
            logger.error("Reservation: -delete- ERROR", e);
        }
    }

    /**
     * Retrieves the reservation with the provided ID from the database.
     *
     * @param id The ID of the to be retrieved reservation.
     * @return a Reservations object in Json format.
     */
    @GetMapping("getReservation")
    @ResponseBody
    public Reservations getReservation(@RequestParam int id) {
        try {
            return reservationsRepo.getReservation(id);
        } catch (Exception e) {
            logger.error("Reservation: -get- ERROR", e);
        }
        return null;
    }

    /**
     * Retrieves all the reservations.
     *
     * @return a list of Reservations object in Json format.
     */
    @GetMapping("getAllReservations")
    @ResponseBody
    public List<Reservations> getAllReservations() {
        try {
            return reservationsRepo.getAllReservations();
        } catch (Exception e) {
            logger.error("Reservation: -getAllReservations- ERROR", e);
        }
        return null;
    }

    /**
     * Retrieves the reservations of the selected user.
     *
     * @param username the username of the selected user.
     * @return a list of Reservations object in Json format.
     */
    @GetMapping("getUserReservations")
    @ResponseBody
    public List<Reservations> getUserReservations(@RequestParam String username) {
        try {
            return reservationsRepo.getUserReservations(username);
        } catch (Exception e) {
            logger.error("Reservation: -getUserReservations- ERROR", e);
        }
        return null;
    }

}