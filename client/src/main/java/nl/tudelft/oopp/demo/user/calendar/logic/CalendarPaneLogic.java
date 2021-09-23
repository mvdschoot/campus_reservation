package nl.tudelft.oopp.demo.user.calendar.logic;

import com.mindfusion.scheduling.model.Appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.tudelft.oopp.demo.communication.ItemServerCommunication;
import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.FoodReservation;
import nl.tudelft.oopp.demo.entities.Item;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.user.CurrentUserManager;

public class CalendarPaneLogic {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    public static List<Food> foodList;
    public static List<Building> buildingList;
    public static List<Room> roomList;
    public static List<FoodReservation> foodReservationList;

    /**
     * Gets all the calendar items to add them to the calendar.
     *
     * @return list of all calendar items
     */
    public static List<Appointment> getAllCalendarItems() {
        // set lists with database data
        setLists();

        List<Appointment> appList = new ArrayList<>();
        appList.addAll(getAllReservations());
        appList.addAll(getAllItems());
        appList.addAll(getAllBikeReservations());

        // set lists back to null
        setListsNull();

        return appList;
    }

    /**
     * Sets all the static attribute lists with data from the database.
     */
    private static void setLists() {
        // get needed lists from database
        roomList = Room.getRoomData();
        buildingList = Building.getBuildingData();
        foodList = Food.getAllFoodData();
        foodReservationList = FoodReservation.getAllFoodReservations();
    }

    /**
     * Sets all the static lists to null.
     */
    private static void setListsNull() {
        CalendarPaneLogic.roomList = null;
        CalendarPaneLogic.buildingList = null;
        CalendarPaneLogic.foodList = null;
        CalendarPaneLogic.foodReservationList = null;
    }

    /**
     * Gets all the personal items and transforms them into calendar items.
     *
     * @return list of calendar items
     */
    private static List<Appointment> getAllItems() {
        List<Appointment> appointmentList = new ArrayList<>();
        // get all items from database that belong to the current user
        List<Item> itemList = Item.getUserItems(CurrentUserManager.getUsername());
        // null check
        if (itemList == null) {
            return appointmentList;
        }
        // make an Appointment object for every item to inject in calendar
        for (Item i : itemList) {
            Appointment app = makeAppointment(i);
            appointmentList.add(app);
        }
        return appointmentList;
    }

    /**
     * Gets all the room reservations and transforms them into calendar items.
     *
     * @return list of calendar items
     */
    private static List<Appointment> getAllReservations() {
        List<Appointment> appointmentList = new ArrayList<>();
        // get all items from database that belong to the current user
        List<Reservation> reservationList = Reservation.getUserReservation();
        // null check
        if (reservationList == null) {
            return appointmentList;
        }
        // make an Appointment object for every item to inject in calendar
        for (Reservation r : reservationList) {
            Appointment app = makeAppointment(r);
            appointmentList.add(app);
        }
        return appointmentList;
    }

    /**
     * Gets all the bike reservations and transforms them into calendar items.
     *
     * @return list of calendar items
     */
    private static List<Appointment> getAllBikeReservations() {
        List<Appointment> appointmentList = new ArrayList<>();
        // get all items from database that belong to the current user
        List<BikeReservation> bikeReservationList = BikeReservation.getUserBikeReservations(
                CurrentUserManager.getUsername());
        if (bikeReservationList == null) {
            return appointmentList;
        }
        // make an Appointment object for every item to inject in calendar
        for (BikeReservation br : bikeReservationList) {
            Appointment app = makeAppointment(br);
            appointmentList.add(app);
        }
        return appointmentList;
    }

    /**
     * Makes a calendar item from a AbstractCalenderItem (Item, reservation or bike reservation).
     *
     * @param i the AbstractCalendarItem
     * @return new calendar item
     */
    private static Appointment makeAppointment(AbstractCalendarItem i) {
        Appointment app = new Appointment();
        // give the Appointment object the id that is given in the database (for later reference)
        app.setId(i.getId());
        // set header
        app.setHeaderText(i.getHeader());
        // set start time
        app.setStartTime(i.getStartTime());
        // set end time
        app.setEndTime(i.getEndTime());
        // set description
        app.setDescriptionText(i.getDescription());
        // make sure user cannot move items around in calendar
        app.setLocked(true);
        app.setAllowMove(false);
        app.getStyle().setFillColor(i.getColor());
        return app;
    }

    /**
     * Sends a request to the server to create a new calendar item.
     *
     * @param app calendar item to send to the server
     * @return true if communication was successful, false otherwise
     */
    public static boolean serverCreateItem(Appointment app) {
        if (app == null) {
            return false;
        }
        try {
            // get id of last item created
            String currentId = ItemServerCommunication.getCurrentId();
            // get the id of the last inserted item to assign it to the Appointment object
            app.setId(String.valueOf(Integer.parseInt(currentId)));
            // get date and time in correct format for database
            String date = app.getStartTime().getYear() + "-" + app.getStartTime().getMonth() + "-"
                    + app.getStartTime().getDay();
            String startTime = app.getStartTime().getHour() + ":" + app.getStartTime().getMinute() + ":00";
            String endTime = app.getEndTime().getHour() + ":" + app.getEndTime().getMinute() + ":00";
            // send info to server
            ItemServerCommunication.createItem(CurrentUserManager.getUsername(), app.getHeaderText(), date,
                    startTime, endTime, app.getDescriptionText());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            return false;
        }
        return true;
    }
}
