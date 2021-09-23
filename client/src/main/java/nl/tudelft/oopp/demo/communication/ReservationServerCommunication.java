package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class ReservationServerCommunication {

    /**
     * This client-server method is used to create a new reservation.
     *
     * @param username     - Username of the user
     * @param room         - Room id
     * @param date         - Date of reservation
     * @param startingTime - Starting time of reservation
     * @param endingTime   - Ending time of the reservation
     * @return Boolean value which indicates to the user if Reservation creation is successful.
     */
    public static boolean createReservation(String username, int room, String date,
                                            String startingTime, String endingTime) {
        String params = "username=" + username + "&room=" + room + "&date=" + date
                + "&startingTime=" + startingTime + "&endingTime=" + endingTime;
        return sendPost("createReservation", params);
    }

    /**
     * This client-server method is used to update a reservation.
     *
     * @param id           - Reservation id
     * @param room         - Room id
     * @param date         - Date of reservation
     * @param startingTime - Starting time of reservation
     * @param endingTime   - Ending time of the reservation
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateReservation(int id, int room, String date, String startingTime,
                                            String endingTime) {
        String params = "id=" + id + "&room=" + room + "&date=" + date + "&startingTime="
                + startingTime + "&endingTime=" + endingTime;
        return sendPost("updateReservation", params);
    }

    /**
     * This client-server method is used to delete a reservation.
     *
     * @param id - Reservation id
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteReservation(int id) {
        String params = "id=" + id;
        return sendPost("deleteReservation", params);
    }

    /**
     * This client-server method is used to get a reservation by its id.
     *
     * @param id - Reservation id
     * @return Reservation of the user.
     */
    public static String getReservation(int id) {
        String params = "id=" + id;
        return sendGet("getReservation", params);
    }

    /**
     * This client-server method is used to get all the reservations.
     *
     * @return all reservations
     */
    public static String getAllReservations() {
        return sendGet("getAllReservations", "");
    }

    /**
     * This client-server method is used to get the reservation of a particular user who could be
     * identified by their username which is passed as a parameter.
     *
     * @param username - Username of the user requesting the reservation
     * @return Reservations of a user
     */
    public static String getUserReservations(String username) {
        String params = "username=" + username;
        return sendGet("getUserReservations", params);
    }

    /**
     * This client-server method is used to get the id of the last reservation inserted in the database.
     *
     * @return String containing the integer value of the id
     */
    public static String getCurrentId() {
        return sendGet("currentReservationId", "");
    }

}
