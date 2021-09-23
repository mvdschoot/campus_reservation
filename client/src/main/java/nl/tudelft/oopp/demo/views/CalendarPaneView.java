package nl.tudelft.oopp.demo.views;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * View class for the calendar booking history.
 */
public class CalendarPaneView extends Application {

    /**
     * Starts the thread to load the fxml as a view in the given stage.
     *
     * @param primaryStage stage to load view in
     * @throws Exception exception to be catched if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/calendarPane.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        Scene oldScene = primaryStage.getScene();
        Scene newScene = oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight());
        primaryStage.setMinHeight(510);
        primaryStage.setMinWidth(840);

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    /**
     * Main method.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        launch(args);
    }
}
