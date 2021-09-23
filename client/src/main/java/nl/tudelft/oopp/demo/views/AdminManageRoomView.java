package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AdminManageRoomView extends Application {

    /**
     * Constructor.
     */
    public AdminManageRoomView() {
    }

    /**
     * This method is to start the view.
     * @param primaryStage - is passed as parameter
     * @throws IOException //TODO
     */
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/adminManageRoomView.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        Scene oldScene = primaryStage.getScene();
        primaryStage.setScene(oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));

        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}

