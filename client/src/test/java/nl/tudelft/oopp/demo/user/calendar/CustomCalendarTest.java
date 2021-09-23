package nl.tudelft.oopp.demo.user.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindfusion.common.Duration;
import com.mindfusion.scheduling.CalendarView;

import org.junit.jupiter.api.Test;

/**
 * Tests the CustomCalendar class.
 */
class CustomCalendarTest {

    /**
     * Tests the constructor and pokes to see if settings are correctly set.
     */
    @Test
    void constructorTest() {
        CustomCalendar cc = new CustomCalendar();
        assertEquals(7, cc.getTimetableSettings().getDates().size());
        assertFalse(cc.getAllowAutoScroll());
        assertEquals(CalendarView.Timetable, cc.getCurrentView());
        assertEquals(Duration.fromHours(0.5), cc.getTimetableSettings().getCellTime());
        assertTrue(cc.getTimetableSettings().getShowCurrentTime());
    }
}