package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/loginView.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GeneralStyle.css").toExternalForm());

        final Scene oldScene = primaryStage.getScene();
        final Scene newScene = oldScene == null
                ? new Scene(root, primaryStage.getMinWidth(), primaryStage.getMinHeight())

                : new Scene(root, oldScene.getWidth(), oldScene.getHeight());
        try {
            Image i = new Image("file:" + getClass().getResource("/TULogo.jpg").getPath());
            primaryStage.getIcons().add(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setMinHeight(390);
        primaryStage.setMinWidth(710);
        primaryStage.setScene(newScene);

        primaryStage.show();
    }
}

