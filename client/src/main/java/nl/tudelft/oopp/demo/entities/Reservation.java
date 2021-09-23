package nl.tudelft.oopp.demo.entities;

import com.mindfusion.common.DateTime;

import java.awt.Color;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.admin.controller.AdminManageUserViewController;
import nl.tudelft.oopp.demo.communication.ReservationServerCommunication;
import nl.tudelft.oopp.demo.user.CurrentUserManager;
import nl.tudelft.oopp.demo.user.calendar.logic.AbstractCalendarItem;
import nl.tudelft.oopp.demo.user.calendar.logic.CalendarPaneLogic;

import org.json.JSONArray;


public class Reservation implements AbstractCalendarItem {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty id;
    private StringProperty username;
    //Room means the room-id of the particular room.
    private IntegerProperty room;
    private StringProperty date;
    private StringProperty startingTime;
    private StringProperty endingTime;

    /**
     * constructor with some initial data.
     */
    public Reservation() {
        this.id = new SimpleIntegerProperty(-1);
        this.username = new SimpleStringProperty(null);
        this.room = new SimpleIntegerProperty(-1);
        this.date = new SimpleStringProperty(null);
        this.startingTime = new SimpleStringProperty(null);
        this.endingTime = new SimpleStringProperty(null);
    }

    /**
     * Constructor.
     * Simple string property is used because it provides data binding.
     *
     * @param id           int
     * @param username     String
     * @param room         int
     * @param date         String
     * @param startingTime String
     * @param endingTime   String
     */
    public Reservation(int id, String username, int room, String date, String startingTime, String endingTime) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.room = new SimpleIntegerProperty(room);
        this.date = new SimpleStringProperty(date);
        this.startingTime = new SimpleStringProperty(startingTime);
        this.endingTime = new SimpleStringProperty(endingTime);

    }

    /**
     * Method that returns all reservations for a particular room on a particular date.
     *
     * @param roomId        the id of the room
     * @param date          the date to be filtered on
     * @param dateConverter converts date value to String format hh:mm
     * @return List of filtered reservations
     */
    public static List<Reservation> getRoomReservationsOnDate(int roomId, LocalDate date,
                                                              StringConverter<LocalDate> dateConverter) {
        try {
            return Reservation.getAllReservations().stream()
                    .filter(x -> x.getRoom().get() == roomId)
                    .filter(x -> x.getDate().get().equals(dateConverter.toString(date)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Convert the server sent code into an Observable List of Reservation.
     *
     * @return Observable List of Reservations.
     */
    public static ObservableList<Reservation> getAllReservations() {
        try {
            ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
            JSONArray jsonArrayReservation = new JSONArray(
                    ReservationServerCommunication.getAllReservations());
            for (int i = 0; i < jsonArrayReservation.length(); i++) {
                Reservation r = new Reservation();
                r.setId(jsonArrayReservation.getJSONObject(i).getInt("id"));
                r.setUsername(jsonArrayReservation.getJSONObject(i).getString("username"));
                r.setDate(jsonArrayReservation.getJSONObject(i).getString("date"));
                r.setRoom(jsonArrayReservation.getJSONObject(i).getInt("room"));
                r.setStartingTime(jsonArrayReservation.getJSONObject(i).getString("startingTime"));
                r.setEndingTime(jsonArrayReservation.getJSONObject(i).getString("endingTime"));
                reservationList.add(r);
            }
            return reservationList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Convert the server sent code into an Observable List of Reservation for the particular user!!.
     *
     * @return Observable List of Reservations.
     */
    public static ObservableList<Reservation> getUserReservation() {
        try {
            ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
            JSONArray jsonArrayReservation = new JSONArray(
                    ReservationServerCommunication.getUserReservations(CurrentUserManager.getUsername()));
            for (int i = 0; i < jsonArrayReservation.length(); i++) {
                Reservation r = new Reservation();
                r.setId(jsonArrayReservation.getJSONObject(i).getInt("id"));
                r.setUsername(jsonArrayReservation.getJSONObject(i).getString("username"));
                r.setDate(jsonArrayReservation.getJSONObject(i).getString("date"));
                r.setRoom(jsonArrayReservation.getJSONObject(i).getInt("room"));
                r.setStartingTime(jsonArrayReservation.getJSONObject(i).getString("startingTime"));
                r.setEndingTime(jsonArrayReservation.getJSONObject(i).getString("endingTime"));
                reservationList.add(r);
            }
            return reservationList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @return ObservableList containing reservations from the selected user.
     */
    public static ObservableList<Reservation> getSelectedUserReservation() {
        try {
            ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
            JSONArray jsonArrayReservation = new JSONArray(
                    ReservationServerCommunication.getUserReservations(
                            AdminManageUserViewController.currentSelectedUser.getUsername().get()));
            for (int i = 0; i < jsonArrayReservation.length(); i++) {
                Reservation r = new Reservation();
                r.setId(jsonArrayReservation.getJSONObject(i).getInt("id"));
                r.setUsername(jsonArrayReservation.getJSONObject(i).getString("username"));
                r.setDate(jsonArrayReservation.getJSONObject(i).getString("date"));
                r.setRoom(jsonArrayReservation.getJSONObject(i).getInt("room"));
                r.setStartingTime(jsonArrayReservation.getJSONObject(i).getString("startingTime"));
                r.setEndingTime(jsonArrayReservation.getJSONObject(i).getString("endingTime"));
                reservationList.add(r);
            }
            return reservationList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty.
     */
    public IntegerProperty getReservationId() {
        return id;
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getUsername() {
        return username;
    }

    /**
     * Setter.
     *
     * @param username String, new
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty.
     */
    public IntegerProperty getRoom() {
        return room;
    }

    /**
     * Setter.
     *
     * @param room int, new
     */
    public void setRoom(int room) {
        this.room.set(room);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getDate() {
        return date;
    }

    /**
     * Setter.
     *
     * @param date String, new.
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getReservationStartingTime() {
        return startingTime;
    }

    /**
     * Setter.
     *
     * @param startingTime String, new
     */
    public void setStartingTime(String startingTime) {
        this.startingTime.set(startingTime);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getReservationEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime.set(endingTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation that = (Reservation) o;
        return this.getReservationId().get() == that.getReservationId().get();
    }

    @Override
    public String getId() {
        return String.valueOf(this.getReservationId().get());
    }

    /**
     * Setter.
     *
     * @param id int.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String getHeader() {
        return "Reservation";
    }

    @Override
    public DateTime getStartTime() {
        DateTime dt = null;
        // split date in [yyyy, MM, dd]
        String[] date = this.getDate().get().split("-");
        // split time in [hh, mm, ss]
        String[] startTime = this.getReservationStartingTime().get().split(":");
        dt = new DateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]),
                Integer.parseInt(startTime[2]));
        return dt;
    }

    @Override
    public DateTime getEndTime() {
        DateTime dt = null;
        // split date in [yyyy, MM, dd]
        String[] date = this.getDate().get().split("-");
        // split time in [hh, mm, ss]
        String[] endTime = this.getReservationEndingTime().get().split(":");

        dt = new DateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]),
                Integer.parseInt(endTime[2]));
        return dt;
    }

    @Override
    public String getDescription() {
        String[] startTime = this.getReservationStartingTime().get().split(":");
        String[] endTime = this.getReservationEndingTime().get().split(":");
        Room room = CalendarPaneLogic.roomList.stream()
                .filter(x -> x.getRoomId().get() == this.getRoom().get())
                .collect(Collectors.toList()).get(0);
        Building b = CalendarPaneLogic.buildingList.stream()
                .filter(x -> x.getBuildingId().get() == room.getRoomBuilding().get())
                .collect(Collectors.toList()).get(0);

        String description = room.getRoomName().get() + "\n" + b.getBuildingName().get() + "\n"
                + startTime[0] + ":" + startTime[1] + " - " + endTime[0] + ":" + endTime[1] + "\n";

        double totalPrice = 0;
        List<FoodReservation> frList = CalendarPaneLogic.foodReservationList.stream()
                .filter(x -> x.getReservationId().get() == this.getReservationId().get())
                .collect(Collectors.toList());

        for (FoodReservation fr : frList) {
            Food f = CalendarPaneLogic.foodList.stream()
                    .filter(x -> x.getFoodId().get() == fr.getFoodId().get())
                    .collect(Collectors.toList()).get(0);
            description += fr.getFoodQuantity().get() + "x " + f.getFoodName().get() + "\n";
            totalPrice += fr.getFoodQuantity().get() * f.getFoodPrice().get();
        }
        if (frList.size() != 0) {
            description += "total price = " + Math.round(totalPrice * 100.0) / 100.0 + " euro(s)";
        }
        return description;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    /**
     * Get all the reservations for given user.
     * @param user passed user object
     * @return ObservableList of reservation of the user
     */
    public static ObservableList<Reservation> getUserReservation2(User user) {
        try {
            ObservableList<Reservation> reservationList = FXCollections.observableArrayList();
            JSONArray jsonArrayReservation = new JSONArray(
                    ReservationServerCommunication.getUserReservations(
                            user.getUsername().get()));
            for (int i = 0; i < jsonArrayReservation.length(); i++) {
                Reservation r = new Reservation();
                r.setId(jsonArrayReservation.getJSONObject(i).getInt("id"));
                r.setUsername(jsonArrayReservation.getJSONObject(i).getString("username"));
                r.setDate(jsonArrayReservation.getJSONObject(i).getString("date"));
                r.setRoom(jsonArrayReservation.getJSONObject(i).getInt("room"));
                r.setStartingTime(jsonArrayReservation.getJSONObject(i).getString("startingTime"));
                r.setEndingTime(jsonArrayReservation.getJSONObject(i).getString("endingTime"));
                reservationList.add(r);
            }
            return reservationList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }
}
