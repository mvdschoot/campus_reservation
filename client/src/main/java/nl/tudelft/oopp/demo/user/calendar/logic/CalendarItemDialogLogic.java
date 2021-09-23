package nl.tudelft.oopp.demo.user.calendar.logic;

import java.awt.Color;
import java.io.UnsupportedEncodingException;

import nl.tudelft.oopp.demo.communication.BikeReservationCommunication;
import nl.tudelft.oopp.demo.communication.ItemServerCommunication;
import nl.tudelft.oopp.demo.communication.ReservationServerCommunication;

public class CalendarItemDialogLogic {

    // public fields that are used to determine the type of an item
    public static boolean bikeReservation = false;
    public static boolean reservation = false;
    public static boolean item = false;

    /**
     * Sets the correct color of an item.
     *
     * @param fillColor the color
     */
    public static void setItemType(Color fillColor) {
        if (fillColor.equals(Color.CYAN)) {
            reservation = true;
        } else if (fillColor.equals(Color.MAGENTA)) {
            bikeReservation = true;
        } else if (fillColor.equals(Color.ORANGE)) {
            item = true;
        }
    }

    /**
     * Deletes a personal calendar item from the database.
     *
     * @param id the id of the item
     * @return true if communication was successful, false otherwise
     * @throws UnsupportedEncodingException when wrong encoding is used
     */
    public static boolean deleteItem(int id) throws UnsupportedEncodingException {
        // reset all fields
        item = false;
        reservation = false;
        bikeReservation = false;
        return ItemServerCommunication.deleteItem(id);
    }

    /**
     * Deletes a reservation from the database.
     *
     * @param id the id of the reservation
     * @return true if communication was successful, false otherwise
     * @throws UnsupportedEncodingException when wrong encoding is used
     */
    public static boolean deleteReservation(int id) throws UnsupportedEncodingException {
        // check what type of reservation it is
        if (reservation) {
            // reset field
            reservation = false;
            return ReservationServerCommunication.deleteReservation(id);
        } else if (bikeReservation) {
            // reset field
            bikeReservation = false;
            return BikeReservationCommunication.deleteBikeReservation(id);
        } else {
            // reset all fields
            item = false;
            return false;
        }
    }
}
