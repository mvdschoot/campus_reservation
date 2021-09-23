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
import javafx.stage.WindowEvent;

import nl.tudelft.oopp.demo.admin.controller.BookingEditDialogController;


public class BookingEditDialogView extends Application {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/bookingEditDialog.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Reservation");
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.getScene().getWindow().addEventFilter(
                    WindowEvent.WINDOW_CLOSE_REQUEST, event ->
                    BookingEditDialogController.reservation = null);
            // Set the dialog stage properties
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            try {
                Image i = new Image("file:" + getClass().getResource("/TULogo.jpg").getPath());
                dialogStage.getIcons().add(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
}
