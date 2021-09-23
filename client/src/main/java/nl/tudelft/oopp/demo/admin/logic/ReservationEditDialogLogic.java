package nl.tudelft.oopp.demo.admin.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.admin.controller.AdminManageReservationViewController;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;


public class ReservationEditDialogLogic {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    public static String isInputValid(User username, Room room, LocalDate date,
                                       double currentStartValue, double currentEndValue,
                                       StringConverter<LocalDate> temp) {
        String errorMessage = "";

        if (username == null) {
            errorMessage += "No valid username provided!\n";
        }
        if (room == null) {
            errorMessage += "No valid Room provided! \n";
        }
        if (date == null) {
            errorMessage += "No date provided!\n";
        }

        if (!checkTimeSlotValidity(room, date, currentStartValue, currentEndValue, temp)
                || currentStartValue == currentEndValue) {
            errorMessage += "No valid timeslot selected!\n";
        }

        return errorMessage;
    }

    /**
     * Constructor for the converter that converts LocalDate objects to String yyyy-MM-dd format.
     *
     * @return constructed StringConverter
     */
    public static StringConverter<Number> getRangeSliderConverter() {
        try {
            return new StringConverter<Number>() {
                @Override
                public String toString(Number n) {
                    // calculate hours and remaining minutes to get a correct hh:mm format
                    long minutes = n.longValue();
                    long hours = TimeUnit.MINUTES.toHours(minutes);
                    long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
                    // '%02d' means that there will be a 0 in front if its only 1 number + it's a long number
                    return String.format("%02d", hours) + ":" + String.format("%02d", remainingMinutes);
                }


                @Override
                public Number fromString(String string) {
                    String[] split = string.split(":");
                    double hours = Double.parseDouble(split[0]);
                    double minutes = Double.parseDouble(split[1]);
                    return hours * 60 + minutes;
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Method that checks if the chosen timeslot is free.
     *
     * @return true if the timeslot is free, false otherwise
     */
    public static boolean checkTimeSlotValidity(Room room, LocalDate date, double currentStartValue,
                                                double currentEndValue, StringConverter<LocalDate> temp) {
        try {
            // get currently selected room
            Room selectedRoom = room;
            if (selectedRoom == null) {
                return false;
            }
            // get all reservations for the current room on the chosen date
            List<Reservation> roomReservations = Reservation.getRoomReservationsOnDate(
                    selectedRoom.getRoomId().get(), date, temp);

            // if something went wrong with the server communication return false
            if (roomReservations == null) {
                return false;
            }

            // get converter to convert date value to String format hh:mm
            StringConverter<Number> timeConverter = getRangeSliderConverter();

            // if there are no reservations the timeslot is valid
            if (roomReservations.size() == 0) {
                return true;
            }

            Reservation res = AdminManageReservationViewController.currentSelectedReservation;

            for (Reservation r : roomReservations) {
                // if reservation equals the one we are editing, don't consider it
                if (res != null) {
                    if (r.getReservationId().get() == res.getReservationId().get()) {
                        continue;
                    }
                }

                // get rangeslider values + reservation values
                double startValue = (double) timeConverter.fromString(r.getReservationStartingTime().get());
                double endValue = (double) timeConverter.fromString(r.getReservationEndingTime().get());

                // check if the values overlap
                if (!((currentStartValue <= startValue && currentEndValue <= startValue)
                        || (currentStartValue >= endValue && currentEndValue >= endValue))) {
                    return false;
                }

            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return false;
    }
}
