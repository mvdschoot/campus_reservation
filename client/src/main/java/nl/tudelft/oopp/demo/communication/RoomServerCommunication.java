package nl.tudelft.oopp.demo.communication;

import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendGet;
import static nl.tudelft.oopp.demo.communication.GeneralCommunication.sendPost;

public class RoomServerCommunication {

    /**
     * This client-server method is used to create a new room.
     *
     * @param name        - Room name
     * @param building    - Building name
     * @param teacherOnly - Teacher only condition
     * @param capacity    - Room capacity
     * @param photos      - Photo of the room
     * @param description - Room description
     * @param type        - Room type (Lecture hall, project room, etc)
     * @return true if communication was successful, false otherwise
     */
    public static boolean createRoom(String name, int building, boolean teacherOnly,
                                     int capacity, String photos, String description, String type) {
        String params = "name=" + name + "&building=" + building + "&teacherOnly="
                + teacherOnly + "&capacity=" + capacity + "&photos=" + photos + "&description="
                + description + "&type=" + type;

        return sendPost("createRoom", params);
    }

    /**
     * This client-server method is used to update a room.
     *
     * @param id          - Room id
     * @param name        - Room name
     * @param building    - Building name
     * @param teacherOnly - Teacher only condition
     * @param capacity    - Room capacity
     * @param photos      - Photos of the room
     * @param description - Room description
     * @param type        - Room type (Lecture hall, project room, etc)
     * @return true if communication was successful, false otherwise
     */
    public static boolean updateRoom(int id, String name, int building, boolean teacherOnly,
                                     int capacity, String photos, String description, String type) {
        String params = "id=" + id + "&name=" + name + "&building=" + building + "&teacherOnly="
                + teacherOnly + "&capacity=" + capacity + "&photos=" + photos + "&description="
                + description + "&type=" + type;
        return sendPost("updateRoom", params);
    }

    /**
     * This client-server method is used to delete a room.
     *
     * @param id id of the room
     * @return true if communication was successful, false otherwise
     */
    public static boolean deleteRoom(int id) {
        String params = "id=" + id;
        return sendPost("deleteRoom", params);
    }

    /**
     * This client-server method is used to get a room by id.
     *
     * @param id id of the room
     * @return room object
     */
    public static String getRoom(int id) {
        String params = "id=" + id;
        return sendGet("getRoom", params);
    }

    /**
     * This client-server method is used to get all the rooms.
     *
     * @return all rooms
     */
    public static String getAllRooms() {
        return sendGet("getAllRooms", "");
    }
}
