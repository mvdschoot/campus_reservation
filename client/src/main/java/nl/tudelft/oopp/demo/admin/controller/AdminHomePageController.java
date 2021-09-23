package nl.tudelft.oopp.demo.admin.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminBikeReservationView;
import nl.tudelft.oopp.demo.views.AdminManageBuildingView;
import nl.tudelft.oopp.demo.views.AdminManageFoodView;
import nl.tudelft.oopp.demo.views.AdminManageReservationView;
import nl.tudelft.oopp.demo.views.AdminManageRoomView;
import nl.tudelft.oopp.demo.views.AdminManageUserView;
import nl.tudelft.oopp.demo.views.LoginView;


public class AdminHomePageController {

    private Logger logger = Logger.getLogger("GlobalLogger");

    @FXML
    private Button signOutButton;

    @FXML
    public void initialize() {
        signOutButton.getStyleClass().clear();
        signOutButton.getStyleClass().add("signout-button");
    }

    /**
     * This button lets the admin sign out and redirects the admin back to the login page.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        LoginView loginView = new LoginView();
        loginView.start(stage);
    }

    /**
     * This button redirects the admin to a page where the admin can create/edit/delete rooms.
     * This page also displays all the rooms in the database in a tabular view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void addRoomClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageRoomView adminManageRoomView = new AdminManageRoomView();
        adminManageRoomView.start(stage);
    }

    /**
     * This button redirects the admin to a page where the admin can create/edit/delete a
     * building. This page also displays all the buildings present in a database in a tabular view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void addBuildingClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageBuildingView adminManageBuildingView = new AdminManageBuildingView();
        adminManageBuildingView.start(stage);
    }

    /**
     * This button redirects the admin to a page where the admin can create/edit/delete
     * reservations made by a user. This page also displays all the reservations made by
     * all the users in a tabular view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void addReservationClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageReservationView amrv = new AdminManageReservationView();
        amrv.start(stage);
    }

    /**
     * This button would redirects the admin to a page where the admin can create/edit/delete an
     * users' information or look at their reservation history. This button also displays all the
     * users and information related to them in a tabular view.
     *
     * @param event is passed
     * @throws IOException is thrown
     */
    public void manageUserClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageUserView amuv = new AdminManageUserView();
        amuv.start(stage);
    }

    /**
     * This button would redirects the admin to a page where the admin can create/edit/delete
     * food information. This button also displays all the foods and information related to
     * them in a tabular view.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    public void manageFoodClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminManageFoodView amfv = new AdminManageFoodView();
        amfv.start(stage);
    }

    /**
     * This button would redirects the admin to a page where the admin can create/edit/delete
     * bike reservation information. This button also displays all the bike reservation information
     * in a tabular view.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    public void manageBikeClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminBikeReservationView abrv = new AdminBikeReservationView();
        abrv.start(stage);
    }

    /**
     * This button would allow admin to download the log files.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    public void downloadButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(stage);

        if (dir != null) {
            if (downloadLogFiles(dir)) {
                GeneralMethods.alertBox("Download complete", "Log files downloaded",
                        "The log files have successfully been downloaded!", Alert.AlertType.INFORMATION);
            } else {
                GeneralMethods.alertBox("Download incomplete", "Log files downloaded error",
                        "An error occured while trying to download your log files, please try again",
                        Alert.AlertType.ERROR);
            }
        }

    }

    /**
     * Downloads the log files to the chosen directory.
     *
     * @param dir the chosen directory
     * @throws MalformedURLException thrown when URL is incorrect
     */
    private boolean downloadLogFiles(File dir) throws MalformedURLException {
        // get URL of local log files
        URL clientLog = new File("logs/client.log").getAbsoluteFile().toURI().toURL();
        URL serverLog = new File("logs/server.log").getAbsoluteFile().toURI().toURL();

        // get File objects for destination of download
        File destinationClient = new File(dir.getAbsolutePath() + "/client.log");
        File destinationServer = new File(dir.getAbsolutePath() + "/server.log");

        // write content to destination
        try {
            BufferedInputStream in = new BufferedInputStream(clientLog.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destinationClient);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            in.close();
            fileOutputStream.close();

            BufferedInputStream in2 = new BufferedInputStream(serverLog.openStream());
            FileOutputStream fileOutputStream2 = new FileOutputStream(destinationServer);
            byte[] dataBuffer2 = new byte[1024];
            int bytesRead2;
            while ((bytesRead2 = in2.read(dataBuffer2, 0, 1024)) != -1) {
                fileOutputStream2.write(dataBuffer2, 0, bytesRead2);
            }
            in2.close();
            fileOutputStream2.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString());
            return false;
        }
        return true;
    }
}
