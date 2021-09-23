package nl.tudelft.oopp.demo.user.logic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.Reservation;



public class RoomViewLogic {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    /**
     * Method that checks if the chosen timeslot is free.
     *
     * @return true if the timeslot is free, false otherwise
     */
    public static boolean checkTimeSlotValidity(double currentStartValue, double currentEndValue,
                                                double startValue, double endValue) {
        // check if the values overlap
        return (currentStartValue <= startValue && currentEndValue <= startValue)
                || (currentStartValue >= endValue && currentEndValue >= endValue);
    }

    /**.
     * This compares the two reservations provides as parameter
     * @param o1 First reservation
     * @param o2 Other reservation
     * @return Integer which determines the comparison of the two reservations
     */
    public static int compare(Reservation o1, Reservation o2) {
        String[] o1StartSplit = o1.getReservationStartingTime().get().split(":");
        int o1StartHour = Integer.parseInt(o1StartSplit[0]);
        int o1StartMinute = Integer.parseInt(o1StartSplit[1]);

        String[] o2StartSplit = o2.getReservationStartingTime().get().split(":");
        int o2StartHour = Integer.parseInt(o2StartSplit[0]);
        int o2StartMinute = Integer.parseInt(o2StartSplit[1]);

        // compare hours and minutes
        if (o1StartHour < o2StartHour) {
            return -1;
        } else if (o1StartHour > o2StartHour) {
            return 1;
        }
        if (o1StartMinute < o2StartMinute) {
            return -1;
        } else {
            return 1;
        }
    }

    /**.
     * This method converts the Number in time format which is stored as a string
     * @param n - the number which is intended to be a time parameter
     * @return String which contains the time
     */
    public static String toStringNum(Number n) {
        // calculate hours and remaining minutes to get a correct hh:mm format
        long minutes = n.longValue();
        long hours = TimeUnit.MINUTES.toHours(minutes);
        long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
        // '%02d' means that there will be a 0 in front if its only 1 number + it's a long number
        return String.format("%02d", hours) + ":" + String.format("%02d", remainingMinutes);
    }

    /**.
     * Reverts the functionality of the above method. This methods converts the string back to the
     * number format for the time
     * @param time - Time provided as a string
     * @return A number which represents the time in the time format
     */
    public static Number fromStringTime(String time) {
        if (time != null) {
            String[] split = time.split(":");
            return Double.parseDouble(split[0]) * 60 + Double.parseDouble(split[1]);
        }
        return null;
    }

    /**.
     * Using the dateFormatter, this method converts the string into a local Date type
     * @param string - The date as a string
     * @param dateFormatter - DateFormatter (yyyy-MM-dd)
     * @return Local date type object which contains the equivalent form of the string
     */
    public static LocalDate fromStringDate(String string, DateTimeFormatter dateFormatter) {
        if (string != null && !string.isEmpty()) {
            // get correct LocalDate from String format
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }


    /**.
     * Converts the string into a list of food entities
     * @param string - The string which contains all the food
     * @param foodList - The list of food
     * @return - Food equivalent of the string
     */
    public static Food fromStringFood(String string, List<Food> foodList) {
        if (string != null) {
            return foodList.stream()
                    .filter(x -> x.getFoodName().get().equals(string))
                    .collect(Collectors.toList()).get(0);
        } else {
            return null;
        }
    }

    /**.
     * Converts a food object to a string for readability and testability
     * @param object - The food object to be converted
     * @return String which contains the food name
     */
    public static String toStringFood(Food object) {
        if (object != null) {
            return object.getFoodName().get();
        } else {
            return null;
        }
    }

    /**.
     * Converts the date from a localDate type to a string using a dateFormatter
     * @param date - The date provided in a loclaDate format
     * @param dateFormatter - DateFormatter (yyyy-MM-dd)
     * @return The string equivalence of the provided date
     */
    public static String toStringDate(LocalDate date, DateTimeFormatter dateFormatter) {
        if (date != null) {
            // get correctly formatted String
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

}
