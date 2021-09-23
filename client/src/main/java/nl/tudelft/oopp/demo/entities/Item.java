package nl.tudelft.oopp.demo.entities;

import com.mindfusion.common.DateTime;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import nl.tudelft.oopp.demo.communication.ItemServerCommunication;
import nl.tudelft.oopp.demo.user.calendar.logic.AbstractCalendarItem;

import org.json.JSONArray;

/**
 * Entity class for calendar items.
 */
public class Item implements AbstractCalendarItem {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty id;
    private StringProperty user;
    private StringProperty title;
    private StringProperty date;
    private StringProperty startingTime;
    private StringProperty endingTime;
    private StringProperty description;

    /**
     * Constructor.
     *
     * @param id           id of item
     * @param user         to whom item belongs
     * @param title        title of the item
     * @param date         date of item
     * @param startingTime startingTime of item
     * @param endingTime   endingTime of item
     * @param description  description of item
     */
    public Item(int id, String user, String title, String date, String startingTime,
                String endingTime, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.user = new SimpleStringProperty(user);
        this.title = new SimpleStringProperty(title);
        this.date = new SimpleStringProperty(date);
        this.startingTime = new SimpleStringProperty(startingTime);
        this.endingTime = new SimpleStringProperty(endingTime);
        this.description = new SimpleStringProperty(description);
    }

    /**
     * Default constructor.
     */
    public Item() {
        this.id = new SimpleIntegerProperty(-1);
        this.user = new SimpleStringProperty(null);
        this.title = new SimpleStringProperty(null);
        this.date = new SimpleStringProperty(null);
        this.startingTime = new SimpleStringProperty(null);
        this.endingTime = new SimpleStringProperty(null);
        this.description = new SimpleStringProperty(null);
    }

    /**
     * Method that gets all the Items in the database and parses the JSON into ObservableList.
     *
     * @return ObservableList with all items in the database
     */
    public static ObservableList<Item> getAllItems() {
        try {
            ObservableList<Item> itemData = FXCollections.observableArrayList();
            JSONArray itemArray = new JSONArray(ItemServerCommunication.getAllItems());
            for (int i = 0; i < itemArray.length(); i++) {
                Item item = new Item();
                item.setId(itemArray.getJSONObject(i).getInt("id"));
                item.setUser(itemArray.getJSONObject(i).getString("user"));
                item.setTitle(itemArray.getJSONObject(i).getString("title"));
                item.setDate(itemArray.getJSONObject(i).getString("date"));
                item.setStartingTime(itemArray.getJSONObject(i).getString("startingTime"));
                item.setEndingTime(itemArray.getJSONObject(i).getString("endingTime"));
                item.setDescription(itemArray.getJSONObject(i).getString("description"));
                itemData.add(item);
            }
            return itemData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Get all items of a particular user in the database.
     *
     * @param user the user from which we need the items
     * @return ObservableList with all items of the user
     */
    public static ObservableList<Item> getUserItems(String user) {
        try {
            ObservableList<Item> itemData = FXCollections.observableArrayList();
            JSONArray array = new JSONArray(ItemServerCommunication.getUserItems(user));
            for (int i = 0; i < array.length(); i++) {
                Item item = new Item();
                item.setId(array.getJSONObject(i).getInt("id"));
                item.setUser(array.getJSONObject(i).getString("user"));
                item.setTitle(array.getJSONObject(i).getString("title"));
                item.setDate(array.getJSONObject(i).getString("date"));
                item.setStartingTime(array.getJSONObject(i).getString("startingTime"));
                item.setEndingTime(array.getJSONObject(i).getString("endingTime"));
                item.setDescription(array.getJSONObject(i).getString("description"));
                itemData.add(item);
            }
            return itemData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Getter.
     *
     * @return int, in the form of IntegerProperty
     */
    public IntegerProperty getItemId() {
        return id;
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getUser() {
        return user;
    }

    /**
     * Setter.
     *
     * @param user String, new value
     */
    public void setUser(String user) {
        this.user.set(user);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getTitle() {
        return title;
    }

    /**
     * Setter.
     *
     * @param title String, new value
     */
    public void setTitle(String title) {
        this.title.set(title);
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
     * @param date String, new value
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getItemStartingTime() {
        return startingTime;
    }

    /**
     * Setter.
     *
     * @param startingTime String, new value.
     */
    public void setStartingTime(String startingTime) {
        this.startingTime.set(startingTime);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getItemEndingTime() {
        return endingTime;
    }

    /**
     * Setter.
     *
     * @param endingTime String, new value.
     */
    public void setEndingTime(String endingTime) {
        this.endingTime.set(endingTime);
    }

    /**
     * Getter.
     *
     * @return String in the form of a StringProperty.
     */
    public StringProperty getItemDescription() {
        return description;
    }

    /**
     * Equals method to compare two Item objects.
     *
     * @param o the item to compare to
     * @return true if id's match, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return this.getItemId().get() == item.getItemId().get();
    }

    @Override
    public String getId() {
        return String.valueOf(this.getItemId().get());
    }

    /**
     * Setter.
     *
     * @param id int, new value
     */
    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String getHeader() {
        return "Item";
    }

    @Override
    public DateTime getStartTime() {
        DateTime dt = null;
        // split date in [yyyy, MM, dd]
        String[] date = this.getDate().get().split("-");
        // split time in [hh, mm, ss]
        String[] startTime = this.getItemStartingTime().get().split(":");
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
        String[] endTime = this.getItemEndingTime().get().split(":");

        dt = new DateTime(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]),
                Integer.parseInt(endTime[2]));
        return dt;
    }

    @Override
    public String getDescription() {
        return this.getItemDescription().get();
    }

    /**
     * Setter.
     *
     * @param description String, new value
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}
