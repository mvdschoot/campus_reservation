package nl.tudelft.oopp.demo.user.calendar.controller;

import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.CalendarView;
import com.mindfusion.scheduling.model.Appointment;

import java.awt.Point;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.calendar.CustomCalendar;
import nl.tudelft.oopp.demo.user.calendar.logic.CalendarPaneLogic;
import nl.tudelft.oopp.demo.views.CalenderEditItemDialogView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.SearchView;

/**
 * .
 * Class that controls the view which contains the calendar with booking history
 * and custom calendar items
 */
public class CalendarPaneController implements Initializable {

    public static Stage thisStage;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    public static volatile Calendar calendar;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    /**
     * Custom initialization of JavaFX components.
     * This method is automatically called after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // set css style classes
            backButton.getStyleClass().clear();
            signOutButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().add("signout-button");
            // SwingNode gives the ability to inject Swing components in the JavaFX environment
            SwingNode node = new SwingNode();
            // configure the swing node + inject in JavaFX environment
            configureNode(node);
            // scroll the calendar to start at 8am
            setScrollPosition();
            // Add all reservations and items from database to the calendar
            addAllItemsToCalendar();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Sets the scroll position of the calendar to start at 8am instead of 12am.
     */
    private void setScrollPosition() {
        // The calendar gets scrolled to 08:00 instead of 00:00
        // wait until calendar is initialized and then scroll
        while (calendar == null) {
            Thread.onSpinWait();
        }
        calendar.setScrollPosition(new Point(0, 16));
    }

    /**
     * Initializes the SwingNode with Swing components. This needs to happen in a separate thread.
     *
     * @param node the node that needs to be configured
     */
    private void configureNode(SwingNode node) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Calendar c = new CustomCalendar();
                configureCalendar(c);
                // assign configured custom calendar to class attribute for later use
                calendar = c;
                // set the content in the node
                node.setContent(c);
                // add the node to the anchor pane.
                pane.getChildren().add(node);
            }

            /**
             * Configures the calendar to the javafx Anchor pane.
             * @param c the calendar
             */
            private void configureCalendar(Calendar c) {
                // set size of custom calendar
                c.setSize((int) pane.getWidth(), (int) pane.getHeight());
                // set anchor constraints on node
                AnchorPane.setLeftAnchor(node, 0d);
                AnchorPane.setTopAnchor(node, 0d);
                AnchorPane.setBottomAnchor(node, 0d);
                AnchorPane.setRightAnchor(node, 0d);
            }
        });
    }

    /**
     * Adds all the calendar items (room reservations, bike reservations, etc.) to the calendar.
     */
    private void addAllItemsToCalendar() {
        // initialize list with all the items and add them to the calendar
        List<Appointment> appList = CalendarPaneLogic.getAllCalendarItems();
        calendar.getSchedule().getItems().addAll(appList);
    }


    /**
     * This button opens a dialog box to get the details of an item like date, time and description and adds it to
     * the calender.
     *
     * @param event which is used to get the current stage
     */
    @FXML
    private void addItemClicked(ActionEvent event) {
        try {
            // get current stage
            thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // load and start the dialog box
            CalenderEditItemDialogView iv = new CalenderEditItemDialogView();
            iv.start(thisStage);

            Appointment app = CalendarEditItemDialogController.item;
            if (CalendarPaneLogic.serverCreateItem(app)) {
                GeneralMethods.alertBox("Add item", "Success",
                        "Successfully added item to calendar", Alert.AlertType.INFORMATION);
                // add the item to the calendar
                calendar.getSchedule().getItems().add(app);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Switches the calendar to a 7-day week view.
     */
    @FXML
    private void weeklyViewClicked() {
        try {
            calendar.setCurrentView(CalendarView.Timetable);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Switches the calendar to a month view.
     */
    @FXML
    private void monthlyViewClicked() {
        try {
            calendar.setCurrentView(CalendarView.SingleMonth);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Loads the login view (logs the user out).
     *
     * @param event is passed
     */
    @FXML
    private void signOutClicked(javafx.event.ActionEvent event) {
        try {
            // reset calendar
            calendar = null;
            // get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // load and start login view
            LoginView loginView = new LoginView();
            loginView.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * This button sends the user back to the search view page.
     *
     * @param event is passed
     */
    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            // reset calendar
            calendar = null;
            // get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // load and start search view
            SearchView searchView = new SearchView();
            searchView.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

}
