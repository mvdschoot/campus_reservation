package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class BikeReservationCommunication {

    /**
     * Adds a bike reservation to the database.
     *
     * @param building     The Building id to which the bikes belong
     * @param user         The username of the user making the reservation
     * @param numBikes     The number of bikes reserved
     * @param date         The date of the reservation
     * @param startingTime The starting time of the reservation
     * @param endingTime   The ending time of the reservation
     * @return boolean true if communication was successful, false otherwise
     */
    public static boolean createBikeReservation(int building, String user, int numBikes, String date,
                                                String startingTime, String endingTime) {
        String params = "building=" + building + "&user=" + user + "&numBikes=" + numBikes
                + "&date=" + date + "&startingTime=" + startingTime
                + "&endingTime=" + endingTime;
        return sendPost("createBikeReservation", params);
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
     * @return boolean true if communication was successful, false otherwise
     */
    public static boolean updateBikeReservation(int id, int building, String user, int numBikes, String date,
                                                String startingTime, String endingTime) {
        String params = "id=" + id + "&building=" + building + "&user=" + user
                + "&numBikes=" + numBikes + "&date=" + date
                + "&startingTime=" + startingTime + "&endingTime=" + endingTime;
        return sendPost("updateBikeReservation", params);
    }

    /**
     * Deletes a bike reservation from the database.
     *
     * @param id The id of the bike reservation
     * @return boolean true if communication was successful, false otherwise
     */
    public static boolean deleteBikeReservation(int id) {
        String params = "id=" + id;
        return sendPost("deleteBikeReservation", params);
    }

    /**
     * Retrieves a bike reservation from the database by id.
     *
     * @param id The id of the bike reservation
     * @return Returns a BikeReservation object
     */
    public static String getBikeReservation(int id) {
        String params = "id=" + id;
        return sendGet("getBikeReservation", params);
    }

    /**
     * Retrieves all bike reservations from the database.
     *
     * @return Returns a list of BikeReservations
     */
    public static String getAllBikeReservation() {
        return sendGet("getAllBikeReservation", "");
    }

    /**
     * Retrieves all the bike reservations of the bikes that belong to a building.
     *
     * @param building The id of the building
     * @return Returns a list of bike reservations.
     */
    public static String getBuildingBikeReservations(int building) {
        String params = "building=" + building;
        return sendGet("getBuildingBikeReservations", params);
    }

    /**
     * Retrieves all bike reservations that have been made by a user.
     *
     * @param user The username of the particular user
     * @return Returns a list of bike reservations
     */
    public static String getUserBikeReservations(String user) {
        String params = "user=" + user;
        return sendGet("getUserBikeReservations", params);
    }


}