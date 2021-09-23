package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.controller.RentABikeController;


public class RentABikeView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        RentABikeController.thisStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/RentABikeView.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        Scene oldScene = primaryStage.getScene();
        Scene newScene = oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())

                : new Scene(root, oldScene.getWidth(), oldScene.getHeight());
        primaryStage.setMinHeight(470);
        primaryStage.setMinWidth(880);
        primaryStage.setScene(newScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

