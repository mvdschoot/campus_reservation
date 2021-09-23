package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class BuildingServerCommunication {

    /**
     * This is a server-client communication method which is used to receive all the buildings
     * present in the database.
     *
     * @return all buildings present in the database (JSON).
     */
    public static String getAllBuildings() {
        return sendGet("getAllBuildings", "");
    }

    /**
     * This is a client-server communication class which receives a certain building using the building id.
     *
     * @param id id of the building wanted
     * @return building (JSON)
     */
    public static String getBuilding(int id) {
        String params = "id=" + id;
        return sendGet("getBuilding", params);
    }

    /**
     * This is a client-server communication class which receives all the buildings that serve a specific food.
     *
     * @param id the food id
     * @return all buildings that serve this food (JSON)
     */
    public static String getBuildingByFoodId(int id) {
        String params = "id=" + id;
        return sendGet("getBuildingByFoodId", params);
    }

    /**
     * This is a client-server communication method which is used to create a new building.
     *
     * @param name        - Name of the building
     * @param roomCount   - Number of rooms the building has
     * @param address     - Address of the building
     * @param openingTime - Opening time of building
     * @param closingTime - Closing time of building
     * @return true if communication was successful, false otherwise
     */
    public static boolean createBuilding(String name, int roomCount, String address, int maxBikes,
                                         String openingTime, String closingTime) {
        String params = "name=" + name + "&roomCount=" + roomCount + "&address=" + address
                + "&maxBikes=" + maxBikes + "&openingTime=" + openingTime + "&closingTime=" + closingTime;

        return sendPost("createBuilding", params);
    }


    /**
     * This is a client-server communication method which is used to delete a certain building.
     *
     * @param id id of the building
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteBuilding(int id) {
        String params = "id=" + id;
        return sendPost("deleteBuilding", params);
    }

    /**
     * This is a client-server communication method which is used to update the attributes of a building.
     *
     * @param id          - Building id
     * @param name        - Name of the building
     * @param roomCount   - Number of rooms the building has
     * @param address     - Address of the building
     * @param maxBikes    - Maximum number of bikes available in the building
     * @param openingTime - Opening time of building
     * @param closingTime - Closing time of building
     * @return Boolean value which is used to display a message to the client if the building is updated.
     */
    public static boolean updateBuilding(int id, String name, int roomCount, String address, int maxBikes,
                                         String openingTime, String closingTime) {
        String params = "id=" + id + "&name=" + name + "&roomCount=" + roomCount + "&address=" + address
                + "&maxBikes=" + maxBikes + "&openingTime=" + openingTime + "&closingTime=" + closingTime;

        return sendPost("updateBuilding", params);
    }
}
