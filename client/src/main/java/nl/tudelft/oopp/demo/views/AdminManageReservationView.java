package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AdminManageReservationView extends Application {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    /**.
     * Constructor
     */
    public AdminManageReservationView() {
    }

    /**
     * This method is to start the view.
     * @param primaryStage - is passed as parameter
     */
    public void start(Stage primaryStage) {

        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/adminManageReservationView.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

            //Making sure that the page doesn't resize when we switch between scenes
            Scene oldScene = primaryStage.getScene();
            primaryStage.setScene(oldScene == null
                    ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())
                    : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(710);
            primaryStage.show();

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
