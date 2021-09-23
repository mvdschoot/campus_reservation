package nl.tudelft.oopp.demo.views;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.general.GeneralMethods;

public class BikeNewDialogView extends Application {

    private Logger logger = Logger.getLogger("GlobalLogger");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/bikeEditDialog.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            try {
                Image i = new Image("file:" + getClass().getResource("/TULogo.jpg").getPath());
                dialogStage.getIcons().add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            GeneralMethods.view(dialogStage, primaryStage, "New Bike Reservation", root);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
}
