package nl.tudelft.oopp.demo.user.calendar.logic;

import com.mindfusion.common.DateTime;
import com.mindfusion.scheduling.model.Appointment;

import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.util.StringConverter;

public class CalendarEditItemDialogLogic {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    /**
     * Method that constructs a StringConverter for the time format hh:mm.
     *
     * @return the completed StringConverter
     */
    public static StringConverter<Number> getRangeSliderConverter() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number n) {
                try {
                    // calculate hours and remaining minutes to get a correct hh:mm format
                    long minutes = n.longValue();
                    long hours = TimeUnit.MINUTES.toHours(minutes);
                    long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
                    // '%02d' means that there will be a 0 in front if its only 1 number + it's a long number
                    return String.format("%02d", hours) + ":" + String.format("%02d", remainingMinutes);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.toString());
                }
                return null;
            }

            @Override
            public Number fromString(String string) {
                try {
                    String[] split = string.split(":");
                    return Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.toString());
                }
                return null;
            }
        };
    }

    /**
     * Checks if inputs are valid and returns error messages if needed.
     *
     * @param dateInput        the date input
     * @param headerInput      the header input
     * @param descriptionInput the description input
     * @return String error messages
     */
    public static String checkInputValidity(LocalDate dateInput, String headerInput, String descriptionInput) {
        String errorMessage = "";
        // add error message for every error found
        if (dateInput == null) {
            errorMessage += "No date provided!\n";
        }
        if (headerInput.equals("")) {
            errorMessage += "No header provided!\n";
        }
        if (descriptionInput.equals("")) {
            errorMessage += "No description provided!\n";
        }
        return errorMessage;
    }

    /**
     * Checks if any error messages were produced.
     *
     * @param errorMessage the error messages string
     * @return true if no errors were produced, false otherwise
     */
    public static boolean checkIfAnyError(String errorMessage) {
        return errorMessage.equals("");
    }

    /**
     * Constructs the StringConverter for the datepicker to format the date to yyyy-MM-dd.
     *
     * @return StringConverter
     */
    public static StringConverter<LocalDate> getDatePickerConverter() {
        return new StringConverter<LocalDate>() {
            // set the wanted pattern (format)
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    // get correctly formatted String
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    // get correct LocalDate from String format
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
    }

    /**
     * Creates a calendar item.
     *
     * @param header      the item header
     * @param description the item description
     * @param date        the item date
     * @param start       the item start time
     * @param end         the item end time
     * @return a new calendar item
     */
    public static Appointment createItem(String header, String description,
                                         LocalDate date, String start, String end) {
        // create Appointment object and set the values
        Appointment app = new Appointment();
        app.setHeaderText(header);
        app.setDescriptionText(description);
        // get date in String format
        String dateString = date.toString();

        // set start and end time
        setStartEndTime(app, dateString, start, end);
        // make sure the user cannot move around the item
        app.setLocked(true);
        app.setAllowMove(false);
        // set orange side color
        app.getStyle().setFillColor(Color.ORANGE);
        return app;
    }

    /**
     * Calculates and sets the start and end time of the calendar item.
     *
     * @param app   the calendar item
     * @param start item start time
     * @param end   item end time
     */
    private static void setStartEndTime(Appointment app, String dateString, String start, String end) {
        // split date in [yyyy, MM, dd]
        String[] dateSplit = dateString.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        // split time in [hh:mm:ss]
        String[] startSplit = start.replace("Start: ", "").split(":");
        String[] endSplit = end.replace("End: ", "")
                .split(":")[0].equals("24") ? new String[]{"23", "59"} : end
                .replace("End: ", "").split(":");

        app.setStartTime(new DateTime(year, month, day, Integer.parseInt(startSplit[0]),
                Integer.parseInt(startSplit[1]), 0));
        app.setEndTime(new DateTime(year, month, day, Integer.parseInt(endSplit[0]),
                Integer.parseInt(endSplit[1]), 0));
    }


}

