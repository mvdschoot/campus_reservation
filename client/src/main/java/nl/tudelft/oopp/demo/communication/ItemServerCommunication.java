package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

/**
 * .
 * Class that controls client-server communication for Item objects
 */
public class ItemServerCommunication {

    /**
     * Gets all the items in the database.
     *
     * @return all the items
     */
    public static String getAllItems() {
        return sendGet("getAllItems", "");
    }

    /**
     * Gets one Item with the given id.
     *
     * @param id id of the wanted Item
     * @return item object
     */
    public static String getItem(int id)  {
        String params = "id=" + id;
        return sendGet("getItem", params);
    }

    /**
     * Create a new Item in the database.
     *
     * @param user         user who item belongs to
     * @param title        title of item
     * @param date         date of item
     * @param startingTime startingTime of item
     * @param endingTime   endingTime of item
     * @param description  description of item
     * @return true if communication was successful, false otherwise
     */
    public static boolean createItem(String user, String title, String date, String startingTime,
                                     String endingTime, String description)  {
        String params = "user=" + user + "&title=" + title + "&date=" + date + "&startingTime=" + startingTime
                + "&endingTime=" + endingTime + "&description=" + description;

        return sendPost("createItem", params);
    }

    /**
     * Deletes the Item identified by the given id.
     *
     * @param id id of item that must be deleted
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteItem(int id)  {
        String params = "id=" + id;
        return sendPost("deleteItem", params);
    }

    /**
     * Gets the id of the last inserted item in the database.
     *
     * @return the current id
     */
    public static String getCurrentId() {
        return sendGet("currentId", "");
    }

    /**
     * Gets all the items of a particular user.
     *
     * @param user user who's items are needed
     * @return all items of this user
     */
    public static String getUserItems(String user)  {
        String params = "user=" + user;
        return sendGet("getUserItems", params);
    }
}
