package nl.tudelft.oopp.demo.user.calendar;

import com.mindfusion.common.DateTime;
import com.mindfusion.common.Duration;
import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.CalendarView;
import com.mindfusion.scheduling.TimetableSettings;

/**
 * Class that customizes the Calendar object.
 */
public class CustomCalendar extends Calendar {

    /**
     * Custom calendar constructor.
     */
    public CustomCalendar() {
        // add the listeners to the calendar
        this.addCalendarListener(new CalendarListener());
        // configure all settings
        configureCalendarSettings();
        // set the initial view of the calendar to week view (timetable)
        this.setCurrentView(CalendarView.Timetable);
    }

    /**
     * Configures all the settings for the calendar.
     */
    private void configureCalendarSettings() {
        // Beginning of configuration
        this.beginInit();
        //get settings of the timetable
        TimetableSettings settings = this.getTimetableSettings();
        setDaysShown(settings);
        configureTable(settings);
        configureRestrictions();

        // end of configuration
        this.endInit();
    }

    /**
     * Restricts the ability of the user to do certain things (editing items in-place etc).
     */
    private void configureRestrictions() {
        // disallow ability to edit items in calendar
        this.setAllowInplaceEdit(false);
        // restrict the auto scrolling
        this.setAllowAutoScroll(false);
    }

    /**
     * Configures the way the timetable looks (cell size etc.).
     *
     * @param settings settings of the timetable
     */
    private void configureTable(TimetableSettings settings) {
        // make cell size/time 30 minutes
        settings.setCellTime(Duration.fromHours(0.5));
        settings.setCellSize(30);
        // show the current time (gets shown as orange line)
        settings.setShowCurrentTime(true);
        // grant ability to select whole day when clicking the header
        settings.setSelectWholeDayOnHeaderClick(true);
        // grant the ability to navigate through different weeks or months
        settings.setShowNavigationButtons(true);
        // skip 7 days when clicking the arrows (switch weeks)
        settings.setScrollStep(7);
    }

    /**
     * Configures the way the days are shown in the calendar (7 day view).
     *
     * @param settings settings of the timetable
     */
    private void setDaysShown(TimetableSettings settings) {
        settings.getDates().clear();
        // create a 7 day view
        for (int i = 0; i < 7; i++) {
            settings.getDates().add(DateTime.today().addDays(i));
        }
        // make all 7 days visible
        settings.setVisibleColumns(7);
    }

}
