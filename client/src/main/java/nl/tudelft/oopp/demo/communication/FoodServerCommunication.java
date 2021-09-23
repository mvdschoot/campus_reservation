package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class FoodServerCommunication {

    /**
     * Creates an HTTP request to add a food in the database.
     *
     * @param name  The name of the new food
     * @param price The price of the new food
     * @return true if communication was successful, false otherwise
     */
    public static boolean createFood(String name, double price) {
        String params = "name=" + name + "&price=" + price;
        return sendPost("createFood", params);
    }

    /**
     * Creates an HTTP request to add a particular food to be available at a building.
     *
     * @param foodId     The ID of the food
     * @param buildingId The ID of the building
     * @return true if communication was successful, false otherwise
     */
    public static boolean addFoodToBuilding(int foodId, int buildingId) {
        String params = "food=" + foodId + "&building=" + buildingId;
        return sendPost("addFoodToBuilding", params);
    }

    /**
     * Creates an HTTP request to add Food to a particular reservation without specifying quantity.
     *
     * @param foodId        The id of the food
     * @param reservationId The id of the reservation
     * @return true if communication was successful, false otherwise
     */
    public static boolean addFoodToReservation(int foodId, int reservationId) {
        String params = "food=" + foodId + "&reservation=" + reservationId + "&quantity=1";
        return sendPost("addFoodToReservation", params);
    }

    /**
     * Creates an HTTP request to add Food to a particular reservation with specifying quantity.
     *
     * @param foodId        The id of the food
     * @param reservationId The id of the reservation
     * @param quantity      The quantity of the chosen food
     * @return true if communication was successful, false otherwise
     */
    public static boolean addFoodToReservation(int foodId, int reservationId, int quantity) {
        String params = "food=" + foodId + "&reservation=" + reservationId + "&quantity=" + quantity;
        return sendPost("addFoodToReservation", params);
    }

    /**
     * Creates an HTTP request to update a food in the database.
     *
     * @param id    The food ID
     * @param name  The new name of the food
     * @param price The new price of the food
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateFood(int id, String name, double price) {
        String params = "id=" + id + "&name=" + name + "&price=" + price;
        return sendPost("updateFood", params);
    }

    /**
     * Creates an HTTP request to remove food from a reservation.
     *
     * @param foodId        The ID of the food to be removed.
     * @param reservationId The ID of the reservation to remove the food from
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteFoodFromReservation(int foodId, int reservationId) {
        String params = "food=" + foodId + "&reservation=" + reservationId;
        return sendPost("deleteFoodFromReservation", params);
    }

    /**
     * Creates an HTTP request to delete a food from being available at a building.
     *
     * @param foodId     The ID of the food
     * @param buildingId The ID of the building
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteFoodFromBuilding(int foodId, int buildingId) {
        String params = "food=" + foodId + "&building=" + buildingId;
        return sendPost("deleteFoodFromBuilding", params);
    }

    /**
     * Creates an HTTP request to update the quantity of a food ordered at a reservation.
     *
     * @param foodId        The ID of the food
     * @param reservationId The ID of the reservation
     * @param quantity      The new quantity of the food
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateFoodReservationQuantity(int foodId, int reservationId, int quantity) {
        String params = "food=" + foodId + "&reservation=" + reservationId + "&quantity=" + quantity;
        return sendPost("updateFoodReservationQuantity", params);
    }

    /**
     * Creates an HTTP request to remove a food from the database.
     *
     * @param id The ID of the food
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteFood(int id) {
        String params = "id=" + id;
        return sendPost("deleteFood", params);
    }

    /**
     * Returns a food object in JSON from the database based on the food ID.
     *
     * @param id The food ID
     * @return Returns the food object (JSON)
     */
    public static String getFood(int id) {
        String params = "id=" + id;
        return sendGet("getFood", params);
    }

    /**
     * Returns a list of food objects in JSON from the database that are ordered with a reservation<br>
     * based on the reservation ID.
     *
     * @param reservation The reservation ID
     * @return Returns list of food objects (JSON)
     */
    public static String getFoodReservationByReservation(int reservation) {
        String params = "reservation=" + reservation;
        return sendGet("getFoodReservationByReservation", params);
    }

    /**
     * Returns a list of food objects in JSON from the database that are available at a building<br>
     * based on the building ID.
     *
     * @param buildingId The building ID
     * @return Returns a list of food objects (JSON)
     */
    public static String getFoodByBuildingId(int buildingId) {
        String params = "building=" + buildingId;
        return sendGet("getFoodByBuildingId", params);
    }

    /**
     * Returns a list of all foods from the database in JSON format.
     *
     * @return Returns a list of food objects (JSON)
     */
    public static String getAllFood() {
        return sendGet("getAllFood", "");
    }

    /**
     * Returns a list of all food reservations from the database in JSON format.
     *
     * @return Returns a list of food reservation objects (JSON)
     */
    public static String getAllFoodReservations() {
        return sendGet("getAllFoodReservations", "");
    }
}
