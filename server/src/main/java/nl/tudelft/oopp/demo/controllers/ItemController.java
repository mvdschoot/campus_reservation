package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.entities.Item;
import nl.tudelft.oopp.demo.repositories.ItemRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepo;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Endpoint to get item by id from database.
     *
     * @param id id of item to get
     * @return Item
     */
    @GetMapping("getItem")
    @ResponseBody
    public Item getItem(@RequestParam int id) {
        try {
            id = Integer.parseInt(CommunicationMethods.decodeCommunication(String.valueOf(id)));
            return itemRepo.getItem(id);
        } catch (Exception e) {
            logger.error("Calender Item: -get- ERROR", e);
        }
        return null;
    }

    /**
     * Endpoint to get all items from database.
     *
     * @return List of items
     */
    @GetMapping("getAllItems")
    @ResponseBody
    public List<Item> getAllItems() {
        try {
            return itemRepo.getAllItems();
        } catch (Exception e) {
            logger.error("Calender Item: -getAll- ERROR", e);
        }
        return null;
    }

    /**
     * Endpoint to create new item in the database.
     *
     * @param user          user who the item belongs to
     * @param title         title of the item
     * @param date          date of the item
     * @param startingTime starting_time of the item
     * @param endingTime   ending_time of the item
     * @param description   description of the item
     * @return true if success, false otherwise
     */
    @PostMapping("createItem")
    @ResponseBody
    public boolean createItem(@RequestParam String user, @RequestParam String title,
                              @RequestParam String date, @RequestParam String startingTime,
                              @RequestParam String endingTime, @RequestParam String description) {
        try {
            user = CommunicationMethods.decodeCommunication(user);
            title = CommunicationMethods.decodeCommunication(title);
            date = CommunicationMethods.decodeCommunication(date);
            startingTime = CommunicationMethods.decodeCommunication(startingTime);
            endingTime = CommunicationMethods.decodeCommunication(endingTime);
            description = CommunicationMethods.decodeCommunication(description);
            itemRepo.insertItem(user, title, date, startingTime, endingTime, description);
            logger.info("Calender Item: -create- User: " + user + " - Title: " + title + " - Date: "
                    + date + " - Starting Time: " + startingTime + " - Ending time: " + endingTime);
            return true;
        } catch (Exception e) {
            logger.error("Calender Item: -create- ERROR", e);
            return false;
        }
    }

    /**
     * Endpoint to delete item from database.
     *
     * @param id id of item to delete
     * @return true if success, false otherwise
     */
    @PostMapping("deleteItem")
    @ResponseBody
    public boolean deleteItem(@RequestParam int id) {
        try {
            itemRepo.deleteItem(id);
            logger.info("Calender Item: -delete- ID: " + id);
            return true;
        } catch (Exception e) {
            logger.error("Calender Item: -delete- ERROR", e);
            return false;
        }
    }

    /**
     * Endpoint to get id of last inserted item in the database.
     *
     * @return id of last inserted item
     */
    @GetMapping("currentId")
    @ResponseBody
    public int getCurrentId() {
        try {
            return itemRepo.getCurrentId();
        } catch (Exception e) {
            logger.error("Calender Item: -getCurrentId- ERROR", e);
        }
        return -1;
    }

    /**
     * Endpoint to get all the items of a particular user from the database.
     *
     * @param user user to get items from
     * @return List of items
     */
    @GetMapping("getUserItems")
    @ResponseBody
    public List<Item> getUserItems(@RequestParam String user) {
        try {
            return itemRepo.getUserItems(user);
        } catch (Exception e) {
            logger.error("Calender Item: -getUserItems- ERROR", e);
        }
        return null;
    }
}
