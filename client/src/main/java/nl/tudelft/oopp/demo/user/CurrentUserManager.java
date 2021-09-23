package nl.tudelft.oopp.demo.user;

public class CurrentUserManager {

    private static String username;
    private static int type;

    public CurrentUserManager() {

    }

    public CurrentUserManager(String usernameP, int typeP) {
        username = usernameP;
        type = typeP;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUserManager.username = username;
    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        CurrentUserManager.type = type;
    }
}
