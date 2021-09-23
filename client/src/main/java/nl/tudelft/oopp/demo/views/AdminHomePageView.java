package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AdminHomePageView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/AdminHomePageView.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        /*
         * Making sure that the page doesn't resize when we switch between scenes
         */
        Scene oldScene = primaryStage.getScene();
        primaryStage.setScene(oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));
        primaryStage.setMinHeight(610);
        primaryStage.setMinWidth(1050);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

