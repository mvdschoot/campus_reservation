package nl.tudelft.oopp.demo.entities;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import nl.tudelft.oopp.demo.communication.BuildingServerCommunication;

import org.json.JSONArray;
import org.json.JSONObject;


public class Building {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty buildingId;
    private StringProperty buildingName;
    private IntegerProperty buildingRoomCount;
    private StringProperty buildingAddress;
    private IntegerProperty buildingMaxBikes;
    private StringProperty openingTime;
    private StringProperty closingTime;

    /**
     * Constructor with default fields.
     */
    public Building() {
        this.buildingId = new SimpleIntegerProperty(-1);
        this.buildingName = new SimpleStringProperty(null);
        this.buildingRoomCount = new SimpleIntegerProperty(0);
        this.buildingAddress = new SimpleStringProperty(null);
        this.buildingMaxBikes = new SimpleIntegerProperty(0);
        this.openingTime = new SimpleStringProperty(null);
        this.closingTime = new SimpleStringProperty(null);
    }


    /**
     * Constructor.
     *
     * @param buildingId        int
     * @param buildingName      String
     * @param buildingRoomCount int
     * @param buildingAddress   String
     * @param buildingMaxBikes  int
     * @param openingTime       String
     * @param closingTime       String
     */
    public Building(int buildingId, String buildingName, int buildingRoomCount,
                    String buildingAddress, int buildingMaxBikes, String openingTime, String closingTime) {
        this.buildingId = new SimpleIntegerProperty(buildingId);
        this.buildingName = new SimpleStringProperty(buildingName);
        this.buildingRoomCount = new SimpleIntegerProperty(0);
        this.buildingAddress = new SimpleStringProperty(buildingAddress);
        this.buildingMaxBikes = new SimpleIntegerProperty(buildingMaxBikes);
        this.openingTime = new SimpleStringProperty(openingTime);
        this.closingTime = new SimpleStringProperty(closingTime);
    }

    /**
     * Convert server response into an ObservableList of rooms.
     */
    public static ObservableList<Building> getBuildingData() {
        try {
            ObservableList<Building> buildingData = FXCollections.observableArrayList();
            JSONArray jsonArrayBuildings = new JSONArray(BuildingServerCommunication.getAllBuildings());
            for (int i = 0; i < jsonArrayBuildings.length(); i++) {
                Building b = new Building();
                b.setBuildingId(jsonArrayBuildings.getJSONObject(i).getInt("id"));
                b.setBuildingName(jsonArrayBuildings.getJSONObject(i).getString("name"));
                b.setBuildingAddress(jsonArrayBuildings.getJSONObject(i).getString("address"));
                b.setBuildingRoomCount(jsonArrayBuildings.getJSONObject(i).getInt("roomCount"));
                b.setBuildingMaxBikes(jsonArrayBuildings.getJSONObject(i).getInt("maxBikes"));
                b.setOpeningTime(jsonArrayBuildings.getJSONObject(i).getString("openingTime"));
                b.setClosingTime(jsonArrayBuildings.getJSONObject(i).getString("closingTime"));
                buildingData.add(b);
            }
            return buildingData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter by building id.
     *
     * @param id int
     * @return Building
     */
    public static Building getBuildingById(int id) {
        try {
            JSONObject jsonObject = new JSONObject(BuildingServerCommunication.getBuilding(id));
            Building b = new Building();
            b.setBuildingId(jsonObject.getInt("id"));
            b.setBuildingName(jsonObject.getString("name"));
            b.setBuildingAddress(jsonObject.getString("address"));
            b.setBuildingRoomCount(jsonObject.getInt("roomCount"));
            b.setBuildingMaxBikes(jsonObject.getInt("maxBikes"));
            b.setOpeningTime(jsonObject.getString("openingTime"));
            b.setClosingTime(jsonObject.getString("closingTime"));
            return b;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        Building building = (Building) o;
        return getBuildingId().get() == building.getBuildingId().get();
    }

    /**
     * Getter by food id.
     *
     * @param foodId int food ID
     * @return Returns a list of Buildings that sell a food
     */
    public static ObservableList<Building> getBuildingByFoodId(int foodId) {
        try {
            ObservableList<Building> buildingData = FXCollections.observableArrayList();
            JSONArray jsonArrayBuildings = new JSONArray(BuildingServerCommunication.getBuildingByFoodId(foodId));
            for (int i = 0; i < jsonArrayBuildings.length(); i++) {
                Building b = new Building();
                b.setBuildingId(jsonArrayBuildings.getJSONObject(i).getInt("id"));
                b.setBuildingName(jsonArrayBuildings.getJSONObject(i).getString("name"));
                b.setBuildingAddress(jsonArrayBuildings.getJSONObject(i).getString("address"));
                b.setBuildingRoomCount(jsonArrayBuildings.getJSONObject(i).getInt("roomCount"));
                b.setBuildingMaxBikes(jsonArrayBuildings.getJSONObject(i).getInt("maxBikes"));
                b.setOpeningTime(jsonArrayBuildings.getJSONObject(i).getString("openingTime"));
                b.setClosingTime(jsonArrayBuildings.getJSONObject(i).getString("closingTime"));
                buildingData.add(b);
            }
            return buildingData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @return int in the form of a IntegerProperty.
     */
    public IntegerProperty getBuildingId() {
        return buildingId;
    }

    /**
     * Setter.
     *
     * @param buildingId int, the new building id.
     */
    public void setBuildingId(int buildingId) {
        this.buildingId.set(buildingId);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getBuildingName() {
        return buildingName;
    }

    /**
     * Setter.
     *
     * @param buildingName String, new name of the building.
     */
    public void setBuildingName(String buildingName) {
        this.buildingName.set(buildingName);
    }

    /**
     * Getter.
     *
     * @return int in the form of a IntegerProperty.
     */
    public IntegerProperty getBuildingRoomCount() {
        return buildingRoomCount;
    }

    /**
     * Setter.
     *
     * @param buildingRoomCount int, new value.
     */
    public void setBuildingRoomCount(int buildingRoomCount) {
        this.buildingRoomCount.set(buildingRoomCount);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getBuildingAddress() {
        return buildingAddress;
    }

    /**
     * Setter.
     *
     * @param buildingAddress String, new value
     */
    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress.set(buildingAddress);
    }

    /**
     * Getter.
     *
     * @return int in the form of a IntegerProperty.
     */
    public IntegerProperty getBuildingMaxBikes() {
        return buildingMaxBikes;
    }

    /**
     * Setter.
     *
     * @param buildingMaxBikes int, new value
     */
    public void setBuildingMaxBikes(int buildingMaxBikes) {
        this.buildingMaxBikes.set(buildingMaxBikes);
    }

    public StringProperty getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime.set(openingTime);
    }

    public StringProperty getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime.set(closingTime);
    }

}
