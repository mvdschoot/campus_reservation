package nl.tudelft.oopp.demo.entities;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import nl.tudelft.oopp.demo.communication.RoomServerCommunication;

import org.json.JSONArray;
import org.json.JSONObject;

public class Room {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty roomId;
    private StringProperty roomName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return getRoomId().get() == room.getRoomId().get();
    }

    private IntegerProperty roomBuilding;
    private BooleanProperty roomTeacherOnly;
    private IntegerProperty roomCapacity;
    private StringProperty roomPhoto;
    private StringProperty roomDescription;
    private StringProperty roomType;

    /**
     * Constructor with some initial data.
     */
    public Room() {
        this.roomId = new SimpleIntegerProperty(-1);
        this.roomName = new SimpleStringProperty(null);
        this.roomBuilding = new SimpleIntegerProperty(-1);
        this.roomTeacherOnly = new SimpleBooleanProperty(false);
        this.roomCapacity = new SimpleIntegerProperty(-1);
        this.roomPhoto = new SimpleStringProperty(null);
        this.roomDescription = new SimpleStringProperty(null);
        this.roomType = new SimpleStringProperty(null);
    }

    /**
     * Constructor.
     * Simple string property is used because it provides data binding.
     *
     * @param roomId          int
     * @param roomName        String
     * @param roomBuilding    int
     * @param roomTeacherOnly boolean
     * @param roomCapacity    int
     * @param roomPhoto       String
     * @param roomDescription String
     * @param roomType        String
     */
    public Room(int roomId, String roomName,
                int roomBuilding, boolean roomTeacherOnly,
                int roomCapacity, String roomPhoto,
                String roomDescription, String roomType) {
        this.roomId = new SimpleIntegerProperty(roomId);
        this.roomName = new SimpleStringProperty(roomName);
        this.roomBuilding = new SimpleIntegerProperty(roomBuilding);
        this.roomTeacherOnly = new SimpleBooleanProperty(roomTeacherOnly);
        this.roomCapacity = new SimpleIntegerProperty(roomCapacity);
        this.roomPhoto = new SimpleStringProperty(roomPhoto);
        this.roomDescription = new SimpleStringProperty(roomDescription);
        this.roomType = new SimpleStringProperty(roomType);
    }

    /**
     * Convert server response into an ObservableList of rooms.
     */
    public static ObservableList<Room> getRoomData() {
        try {
            ObservableList<Room> roomData = FXCollections.observableArrayList();
            JSONArray jsonArrayRooms = new JSONArray(RoomServerCommunication.getAllRooms());
            for (int i = 0; i < jsonArrayRooms.length(); i++) {
                Room r = new Room();
                r.setRoomId(jsonArrayRooms.getJSONObject(i).getInt("id"));
                r.setRoomName(jsonArrayRooms.getJSONObject(i).getString("name"));
                r.setRoomBuilding(jsonArrayRooms.getJSONObject(i).getInt("building"));
                r.setTeacherOnly(jsonArrayRooms.getJSONObject(i).getBoolean("teacherOnly"));
                r.setRoomCapacity(jsonArrayRooms.getJSONObject(i).getInt("capacity"));
                r.setRoomPhoto(jsonArrayRooms.getJSONObject(i).getString("photos"));
                r.setRoomDescription(jsonArrayRooms.getJSONObject(i).getString("description"));
                r.setRoomType(jsonArrayRooms.getJSONObject(i).getString("type"));
                roomData.add(r);
            }
            return roomData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @param id int
     * @return Room object.
     */
    public static Room getRoomById(int id) {
        try {
            JSONObject jsonObject = new JSONObject(RoomServerCommunication.getRoom(id));
            Room r = new Room();
            r.setRoomId(jsonObject.getInt("id"));
            r.setRoomName(jsonObject.getString("name"));
            r.setRoomBuilding(jsonObject.getInt("building"));
            r.setTeacherOnly(jsonObject.getBoolean("teacherOnly"));
            r.setRoomCapacity(jsonObject.getInt("capacity"));
            r.setRoomPhoto(jsonObject.getString("photos"));
            r.setRoomDescription(jsonObject.getString("description"));
            r.setRoomType(jsonObject.getString("type"));
            return r;
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
    public IntegerProperty getRoomId() {
        return roomId;
    }

    /**
     * Setter.
     *
     * @param roomId int, new
     */
    public void setRoomId(int roomId) {
        this.roomId.set(roomId);
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getRoomName() {
        return roomName;
    }

    /**
     * Setter.
     *
     * @param roomName String, new
     */
    public void setRoomName(String roomName) {
        this.roomName.set(roomName);
    }

    /**
     * Getter.
     *
     * @return Bool, in the form of BooleanProperty.
     */
    public BooleanProperty getTeacherOnly() {
        return roomTeacherOnly;
    }

    /**
     * Setter.
     *
     * @param roomTeacherOnly boolean, new
     */
    public void setTeacherOnly(boolean roomTeacherOnly) {
        this.roomTeacherOnly.set(roomTeacherOnly);
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty.
     */
    public IntegerProperty getRoomBuilding() {
        return roomBuilding;
    }

    /**
     * Setter.
     *
     * @param roomBuilding int, new
     */
    public void setRoomBuilding(int roomBuilding) {
        this.roomBuilding.set(roomBuilding);
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty.
     */
    public IntegerProperty getRoomCapacity() {
        return roomCapacity;
    }

    /**
     * Setter.
     *
     * @param roomCapacity int, new
     */
    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity.set(roomCapacity);
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getRoomPhoto() {
        return roomPhoto;
    }

    /**
     * Setter.
     *
     * @param roomPhoto String, new
     */
    public void setRoomPhoto(String roomPhoto) {
        this.roomPhoto.set(roomPhoto);
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getRoomDescription() {
        return roomDescription;
    }

    /**
     * Setter.
     *
     * @param roomDescription String, new
     */
    public void setRoomDescription(String roomDescription) {
        this.roomDescription.set(roomDescription);
    }

    /**
     * Getter.
     *
     * @return String, in the form of StringProperty.
     */
    public StringProperty getRoomType() {
        return roomType;
    }

    /**
     * Setter.
     *
     * @param roomType String, new
     */
    public void setRoomType(String roomType) {
        this.roomType.set(roomType);
    }

}
