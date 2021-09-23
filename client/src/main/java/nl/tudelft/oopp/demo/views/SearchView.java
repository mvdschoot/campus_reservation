package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.user.controller.SearchViewController;

/**
 * View class for the search view.
 */
public class SearchView extends Application {

    /**
     * Starts the thread to load the fxml as a view in the given stage.
     *
     * @param primaryStage stage to load view in
     * @throws Exception exception to be catched if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        SearchViewController.thisStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/SearchView.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        Scene oldScene = primaryStage.getScene();
        primaryStage.setScene(oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));
        primaryStage.setMinHeight(510);
        primaryStage.setMinWidth(840);

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