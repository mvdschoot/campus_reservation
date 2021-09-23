package nl.tudelft.oopp.demo.general;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.controlsfx.control.RangeSlider;

/**
 * This method encodes all communication that occurs between the server and the client.
 * This method is used in all the communication classes that need to send or receive information
 * from the server.
 *
 * @return the body of a get request to the server.
 * @throws Exception if communication has unsupported encoding mechanism.
 */
public class GeneralMethods {

    private static Logger logger;

    /**
     * Sets up and returns the standard logger for this application.
     */
    public static void loggerSetup() {
        try {
            LogManager.getLogManager().reset();
            FileHandler handler = new FileHandler("logs/client.log", true);
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.INFO);

            Logger logr = Logger.getLogger("GlobalLogger");
            logr.addHandler(handler);
            GeneralMethods.logger = logr;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to encode all communication across the data stream.
     *
     * @param params are passed.
     * @return :Encoded parameters as string.
     * @throws UnsupportedEncodingException is thrown.
     */
    public static String encodeCommunication(String params) throws UnsupportedEncodingException {
        params = URLEncoder.encode(params, StandardCharsets.UTF_8.toString());
        params = params.replaceAll("%26", "&");
        params = params.replaceAll("%3D", "=");
        return params;
    }


    /**
     * Creates a pop up, aka an alert.
     *
     * @param title   String
     * @param content String containing the text to show to the user.
     * @param owner   Window
     * @param type    AlertType
     * @return Alert An alert containing the provided information.
     */
    public static Alert createAlert(String title, String content, Window owner, Alert.AlertType type) {
        try {
            // Create a new alert object (dialog box)
            Alert alert = new Alert(type);

            try {
                Image i = new Image("file:" + GeneralMethods.class.getResource("/TULogo.jpg").getPath());
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            alert.getDialogPane().getStylesheets().add(GeneralMethods.class
                    .getResource("/GeneralStyle.css").toExternalForm());

            // Setting the title of the alert box.
            alert.setTitle(title);

            // Setting the content of the alert box.
            alert.setContentText(content);

            // Sets the owner of the alert box.
            alert.initOwner(owner);

            // Setting the modality of the window so that the user doesn't interact with the background screen.
            alert.initModality(Modality.WINDOW_MODAL);

            // Return the alert object.
            return alert;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Creates an alert box with a separate set of features compared to the above method.
     *
     * @param title   - Title of the alert box (String)
     * @param header  - Header of the alert box (String)
     * @param content - Content of the alert box (String)
     * @param type    - AlertType
     * @return Alert  An alert containing the provided information.
     */
    public static Alert alertBox(String title, String header, String content, Alert.AlertType type) {

        try {
            // Create a new alert object (dialog box)
            Alert alert = new Alert(type);

            try {
                Image i = new Image("file:" + GeneralMethods.class.getResource("/TULogo.jpg").getPath());
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            alert.getDialogPane().getStylesheets().add(GeneralMethods.class
                    .getResource("/GeneralStyle.css").toExternalForm());

            // Setting the title of the alert box.
            alert.setTitle(title);

            // Setting the header of the alert box.
            alert.setHeaderText(header);

            // Setting the content of the alert box.
            alert.setContentText(content);

            // Setting the modality of the window so that the user doesn't interact with the background screen.
            alert.initModality(Modality.WINDOW_MODAL);

            // Wait for the user to close the alert box.
            alert.showAndWait();

            // return the alert object
            return alert;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Sets the RangeSlider to a standard white track.
     *
     * @param rs  the RangeSlider to configure
     * @param bw  the BufferedWriter which writes to the CSS file
     * @param css the css file that is written to
     */
    public static void setSliderDefaultCss(RangeSlider rs, BufferedWriter bw, String css) {
        try {
            rs.getStylesheets().add(css);
            bw.write(".track {\n"
                    + "\t-fx-background-color: linear-gradient(to right, #f5fdff 0%, #f5fdff 100%);\n"
                    + "    -fx-background-insets: 0 0 -1 0, 0, 1;\n"
                    + "    -fx-background-radius: 0.25em, 0.25em, 0.166667em; /* 3 3 2 */\n"
                    + "    -fx-padding: 0.25em; /* 3 */\n"
                    + "}\n\n"
                    + ".range-bar {\n"
                    + "    -fx-background-color: rgba(0,0,0,0.5);\n"
                    + "}");
            // flush and close writer
            bw.flush();
            bw.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Method that formats a price (double) to a String value of the price with 2 decimals.
     *
     * @param foodPrice the double value of the price
     * @return a String containing the price in the right format
     */
    public static String formatPriceString(double foodPrice) {
        try {
            foodPrice = (double) Math.round(foodPrice * 100.0) / 100.0;
            String[] splitPrice = String.valueOf(foodPrice).split("\\.");

            while (splitPrice[1].length() < 2) {
                splitPrice[1] += "0";
            }

            return splitPrice[0] + "." + splitPrice[1];
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * .
     * Method to set the properties of a dialog box view.
     *
     * @param dialogStage  - Dialog stage
     * @param primaryStage - Primary stage
     * @param title        - Title of the dialog box
     * @param root         - Root parent
     * @throws IOException is thrown
     */
    public static void view(Stage dialogStage, Stage primaryStage, String title, Parent root)
            throws IOException {

        // Setting dialog box properties
        dialogStage.setTitle(title);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        // Set the dialog stage properties
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }
}
