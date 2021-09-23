package nl.tudelft.oopp.demo.views;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * View class for the dialog box to add items to the calendar booking history.
 */
public class CalenderEditItemDialogView extends Application {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    /**
     * Main method.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the thread to load the fxml as a view in the given stage.
     *
     * @param primaryStage stage to load view in
     * @throws Exception exception to be catched if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/CalenderEditItemDialog.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Add Appointment");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);

            try {
                Image i = new Image("file:" + getClass().getResource("/TULogo.jpg").getPath());
                stage.getIcons().add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set the dialog stage properties
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);

            // Show the dialog and wait until the user closes it
            stage.showAndWait();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

}
