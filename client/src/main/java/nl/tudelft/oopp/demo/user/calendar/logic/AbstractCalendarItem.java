package nl.tudelft.oopp.demo.user.calendar.logic;

import com.mindfusion.common.DateTime;

import java.awt.Color;

/**
 * Interface that makes sure objects that implement this interface can easily be injected in the calendar.
 */
public interface AbstractCalendarItem {

    /**
     * Gets the id of the calendar item.
     *
     * @return String value of id
     */
    String getId();

    /**
     * Gets the header of the calendar item.
     *
     * @return String value of header
     */
    String getHeader();

    /**
     * Gets the starting time of the calendar item.
     *
     * @return DateTime date of the item
     */
    DateTime getStartTime();

    /**
     * Gets the ending time of the calendar item.
     *
     * @return DateTime date of the item
     */
    DateTime getEndTime();

    /**
     * Gets the description of the calendar item.
     *
     * @return String value of description
     */
    String getDescription();

    /**
     * Gets the color of the calendar item.
     *
     * @return Color of the item
     */
    Color getColor();

}
