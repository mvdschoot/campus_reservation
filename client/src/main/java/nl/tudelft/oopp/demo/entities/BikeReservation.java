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

import nl.tudelft.oopp.demo.communication.BikeReservationCommunication;
import nl.tudelft.oopp.demo.user.calendar.logic.AbstractCalendarItem;
import nl.tudelft.oopp.demo.user.calendar.logic.CalendarPaneLogic;

import org.json.JSONArray;
import org.json.JSONObject;

public class BikeReservation implements AbstractCalendarItem {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty bikeReservationId;
    private IntegerProperty bikeReservationBuilding;
    private StringProperty bikeReservationUser;
    private IntegerProperty bikeReservationQuantity;
    private StringProperty bikeReservationDate;
    private StringProperty bikeReservationStartingTime;
    private StringProperty bikeReservationEndingTime;

    /**
     * Constructor to initialize without parameters.
     */
    public BikeReservation() {
        bikeReservationId = new SimpleIntegerProperty(-1);
        bikeReservationBuilding = new SimpleIntegerProperty(-1);
        bikeReservationUser = new SimpleStringProperty(null);
        bikeReservationQuantity = new SimpleIntegerProperty(-1);
        bikeReservationDate = new SimpleStringProperty(null);
        bikeReservationStartingTime = new SimpleStringProperty(null);
        bikeReservationEndingTime = new SimpleStringProperty(null);
    }

    /**
     * Constructor with parameters.
     *
     * @param id           New ID
     * @param building     New BuildingID
     * @param user         New username
     * @param date         New date
     * @param startingTime New starting time
     * @param endingTime   New ending time
     */
    public BikeReservation(int id, int building, String user, int quantity, String date,
                           String startingTime, String endingTime) {
        bikeReservationId = new SimpleIntegerProperty(id);
        bikeReservationBuilding = new SimpleIntegerProperty(building);
        bikeReservationUser = new SimpleStringProperty(user);
        bikeReservationQuantity = new SimpleIntegerProperty(quantity);
        bikeReservationDate = new SimpleStringProperty(date);
        bikeReservationStartingTime = new SimpleStringProperty(startingTime);
        bikeReservationEndingTime = new SimpleStringProperty(endingTime);
    }

    /**
     * Gets all Bike reservations from the database.
     *
     * @return List of BikeReservation
     */
    public static ObservableList<BikeReservation> getBikeReservationData() {
        try {
            ObservableList<BikeReservation> bikeReservationData = FXCollections.observableArrayList();
            String temp = BikeReservationCommunication.getAllBikeReservation();
            JSONArray jsonArrayBikeReservations = new JSONArray(temp);
            for (int i = 0; i < jsonArrayBikeReservations.length(); i++) {
                BikeReservation b = new BikeReservation();
                b.setBikeReservationId(jsonArrayBikeReservations.getJSONObject(i).getInt("id"));
                b.setBikeReservationBuilding(jsonArrayBikeReservations.getJSONObject(i).getInt("building"));
                b.setBikeReservationUser(jsonArrayBikeReservations.getJSONObject(i)
                        .getJSONObject("user").getString("username"));
                b.setBikeReservationQuantity(jsonArrayBikeReservations.getJSONObject(i)
                        .getInt("numBikes"));
                b.setBikeReservationDate(jsonArrayBikeReservations.getJSONObject(i).getString("date"));
                b.setBikeReservationStartingTime(jsonArrayBikeReservations
                        .getJSONObject(i).getString("startingTime"));
                b.setBikeReservationEndingTime(jsonArrayBikeReservations.getJSONObject(i).getString("endingTime"));
                b.setBikeReservationQuantity(jsonArrayBikeReservations.getJSONObject(i).getInt("numBikes"));
                bikeReservationData.add(b);
            }
            return bikeReservationData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Gets all Bike reservations made by a user.
     *
     * @param user The Username
     * @return List of BikeReservation
     */
    public static ObservableList<BikeReservation> getUserBikeReservations(String user) {
        try {
            ObservableList<BikeReservation> bikeReservationData = FXCollections.observableArrayList();
            JSONArray jsonArrayBikeReservations = new JSONArray(
                    BikeReservationCommunication.getUserBikeReservations(user)
            );
            for (int i = 0; i < jsonArrayBikeReservations.length(); i++) {
                BikeReservation b = new BikeReservation();
                b.setBikeReservationId(jsonArrayBikeReservations.getJSONObject(i).getInt("id"));
                b.setBikeReservationBuilding(
                        jsonArrayBikeReservations.getJSONObject(i).getInt("building"));
                b.setBikeReservationUser(jsonArrayBikeReservations.getJSONObject(i)
                        .getJSONObject("user").getString("username"));
                b.setBikeReservationQuantity(jsonArrayBikeReservations.getJSONObject(i)
                        .getInt("numBikes"));
                b.setBikeReservationDate(
                        jsonArrayBikeReservations.getJSONObject(i).getString("date"));
                b.setBikeReservationStartingTime(jsonArrayBikeReservations
                        .getJSONObject(i).getString("startingTime"));
                b.setBikeReservationEndingTime(jsonArrayBikeReservations
                        .getJSONObject(i).getString("endingTime"));
                bikeReservationData.add(b);
            }
            return bikeReservationData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Gets bike reservation with a particular ID.
     *
     * @param id BikeReservation ID
     * @return Returns a BikeReservation object
     */
    public static BikeReservation getBikeReservationById(int id) {
        try {
            JSONObject jsonObject = new JSONObject(BikeReservationCommunication.getBikeReservation(id));
            BikeReservation b = new BikeReservation();
            b.setBikeReservationId(jsonObject.getInt("id"));
            b.setBikeReservationBuilding(jsonObject.getInt("building"));
            b.setBikeReservationUser(jsonObject.getJSONObject("user").getString("username"));
            b.setBikeReservationQuantity(jsonObject.getInt("numBikes"));
            b.setBikeReservationDate(jsonObject.getString("date"));
            b.setBikeReservationStartingTime(jsonObject.getString("startingTime"));
            b.setBikeReservationEndingTime(jsonObject.getString("endingTime"));
            return b;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Gets Bike reservations that are reserved at a particular building.
     *
     * @param id The Building ID
     * @return Returns a list of BikeReservation
     */
    public static ObservableList<BikeReservation> getBikeReservationsByBuilding(int id) {
        try {
            ObservableList<BikeReservation> bikeReservationData = FXCollections.observableArrayList();
            JSONArray jsonArrayBikeReservations = new JSONArray(
                    BikeReservationCommunication.getBuildingBikeReservations(id)
            );
            for (int i = 0; i < jsonArrayBikeReservations.length(); i++) {
                BikeReservation b = new BikeReservation();
                b.setBikeReservationId(jsonArrayBikeReservations
                        .getJSONObject(i).getInt("id"));
                b.setBikeReservationBuilding(jsonArrayBikeReservations
                        .getJSONObject(i).getInt("building"));
                b.setBikeReservationUser(jsonArrayBikeReservations.getJSONObject(i)
                        .getJSONObject("user").getString("username"));
                b.setBikeReservationQuantity(jsonArrayBikeReservations.getJSONObject(i)
                        .getInt("numBikes"));
                b.setBikeReservationDate(jsonArrayBikeReservations
                        .getJSONObject(i).getString("date"));
                b.setBikeReservationStartingTime(jsonArrayBikeReservations
                        .getJSONObject(i).getString("startingTime"));
                b.setBikeReservationEndingTime(jsonArrayBikeReservations
                        .getJSONObject(i).getString("endingTime"));
                bikeReservationData.add(b);
            }
            return bikeReservationData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * <<<<<<< HEAD
     * Method that returns all bike reservations for a particular building on a particular date.
     *
     * @param buildingId    the id of the building
     * @param date          the date to be filtered on
     * @param dateConverter converts date value to String format hh:mm
     * @return List of filtered reservations
     */
    public static List<BikeReservation> getBikeReservationsOnDate(int buildingId, LocalDate date,
                                                                  StringConverter<LocalDate> dateConverter) {
        try {
            List<BikeReservation> list = BikeReservation.getBikeReservationData().stream()
                    .filter(x -> x.getBikeReservationBuilding().get() == buildingId)
                    .filter(x -> x.getBikeReservationDate().get().equals(dateConverter.toString(date)))
                    .collect(Collectors.toList());
            return list;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Gets bike reservation id.
     *
     * @return ID
     */
    public IntegerProperty getBikeReservationId() {
        return bikeReservationId;
    }

    /**
     * Sets Bike reservation id.
     *
     * @param bikeReservationId new ID
     */
    public void setBikeReservationId(int bikeReservationId) {
        this.bikeReservationId.set(bikeReservationId);
    }

    /**
     * Gets bike reservation building.
     *
     * @return Building
     */
    public IntegerProperty getBikeReservationBuilding() {
        return bikeReservationBuilding;
    }

    /**
     * Sets bike reservation building.
     *
     * @param bikeReservationBuilding new Building ID
     */
    public void setBikeReservationBuilding(int bikeReservationBuilding) {
        this.bikeReservationBuilding.set(bikeReservationBuilding);
    }

    /**
     * Gets bike reservation user.
     *
     * @return User
     */
    public StringProperty getBikeReservationUser() {
        return bikeReservationUser;
    }

    /**
     * Sets bike reservation user.
     *
     * @param bikeReservationUser New username
     */
    public void setBikeReservationUser(String bikeReservationUser) {
        this.bikeReservationUser.set(bikeReservationUser);
    }

    /**
     * Gets bike reservation quantity.
     *
     * @return number of bikes
     */
    public IntegerProperty getBikeReservationQuantity() {
        return bikeReservationQuantity;
    }


    /**
     * Sets bike reservation quantity.
     *
     * @param bikeReservationQuantity quantity of bikes
     */
    public void setBikeReservationQuantity(int bikeReservationQuantity) {
        this.bikeReservationQuantity.set(bikeReservationQuantity);
    }

    /**
     * Gets bike reservation date.
     *
     * @return Date
     */
    public StringProperty getBikeReservationDate() {
        return bikeReservationDate;
    }


    /**
     * Sets bike reservation date.
     *
     * @param bikeReservationDate New date in String(yyyy-mm-dd)
     */
    public void setBikeReservationDate(String bikeReservationDate) {
        this.bikeReservationDate.set(bikeReservationDate);
    }

    /**
     * Gets bike reservation starting time.
     *
     * @return Starting time
     */
    public StringProperty getBikeReservationStartingTime() {
        return bikeReservationStartingTime;
    }

    /**
     * Sets bike reservation starting time.
     *
     * @param bikeReservationStartingTime New starting time in String(hh:m:ss)
     */
    public void setBikeReservationStartingTime(String bikeReservationStartingTime) {
        this.bikeReservationStartingTime.set(bikeReservationStartingTime);
    }

    /**
     * Gets bike reservation ending time.
     *
     * @return Ending time
     */
    public StringProperty getBikeReservationEndingTime() {
        return bikeReservationEndingTime;
    }


    /**
     * Sets bike reservation ending time.
     *
     * @param bikeReservationEndingTime New ending time in String(hh-mm-ss)
     */
    public void setBikeReservationEndingTime(String bikeReservationEndingTime) {
        this.bikeReservationEndingTime.set(bikeReservationEndingTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BikeReservation)) {
            return false;
        }
        BikeReservation that = (BikeReservation) o;
        return getBikeReservationId().get() == that.getBikeReservationId().get();
    }

    @Override
    public String getId() {
        return String.valueOf(this.getBikeReservationId().get());
    }

    @Override
    public String getHeader() {
        return "Bike reservation";
    }

    @Override

    public DateTime getStartTime() {
        DateTime dt = null;
        // split date in [yyyy, MM, dd]
        String[] date = this.getBikeReservationDate().get().split("-");
        // split time in [hh, mm, ss]
        String[] startTime = this.getBikeReservationStartingTime().get().split(":");
        dt = new DateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]),
                Integer.parseInt(startTime[2]));
        return dt;
    }

    @Override
    public DateTime getEndTime() {
        DateTime dt = null;
        // split date in [yyyy, MM, dd]
        String[] date = this.getBikeReservationDate().get().split("-");
        // split time in [hh, mm, ss]
        String[] endTime = this.getBikeReservationEndingTime().get().split(":");

        dt = new DateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]),
                Integer.parseInt(endTime[2]));
        return dt;
    }

    @Override
    public String getDescription() {
        // split time in [hh, mm, ss]
        String[] start = this.getBikeReservationStartingTime().get().split(":");
        String[] end = this.getBikeReservationEndingTime().get().split(":");
        // get building
        Building b = CalendarPaneLogic.buildingList.stream()
                .filter(x -> x.getBuildingId().get() == this.getBikeReservationBuilding().get())
                .collect(Collectors.toList()).get(0);
        return b.getBuildingName().get() + "\n"
                + start[0] + ":" + start[1] + " - " + end[0] + ":" + end[1] + "\n"
                + this.getBikeReservationQuantity().get() + " bike(s)";
    }

    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }
}
