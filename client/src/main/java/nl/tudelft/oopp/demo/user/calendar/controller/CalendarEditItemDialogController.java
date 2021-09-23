package nl.tudelft.oopp.demo.user.calendar.controller;

import com.mindfusion.scheduling.model.Appointment;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.calendar.logic.CalendarEditItemDialogLogic;

import org.controlsfx.control.RangeSlider;


/**
 * Class that controls the dialog box to add a calendar item to the users calendar.
 */
public class CalendarEditItemDialogController implements Initializable {

    public static Appointment item;
    public static Stage dialogStage;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    @FXML
    private TextField header;
    @FXML
    private DatePicker date;
    @FXML
    private Text startText;
    @FXML
    private Text endText;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextArea description;

    /**
     * default constructor needed by JavaFX.
     */
    public CalendarEditItemDialogController() {
    }

    /**
     * .
     * Custom initialization of JavaFX components. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        item = null;
        // configure the date picker
        configureDatePicker();
        // construct a range slider
        RangeSlider timeSlot = configureRangeSlider();
        // add range slider to the grid
        gridPane.add(timeSlot, 1, 2);
    }

    /**
     * Method that configures the RangeSlider and returns it ready to use.
     *
     * @return ready to use RangeSlider
     */
    private RangeSlider configureRangeSlider() {
        // initialize the RangeSlider (values are handled as minutes) and the positions of the thumbs
        RangeSlider slider = new RangeSlider(0, 1440, 480, 1080);
        configureRangeSliderVisuals(slider);

        // get and set the StringConverter to show hh:mm format
        StringConverter<Number> converter = CalendarEditItemDialogLogic.getRangeSliderConverter();
        slider.setLabelFormatter(converter);

        // add listeners to show the current thumb values in separate Text objects
        configureRangeSliderListeners(converter, slider);

        // initialize the Text objects with the current values of the thumbs
        startText.setText("Start: " + converter.toString(slider.getLowValue()));
        endText.setText("End: " + converter.toString(slider.getHighValue()));

        return slider;
    }

    /**
     * Sets the layout of the range slider (tick marks etc).
     */
    private void configureRangeSliderVisuals(RangeSlider slider) {
        // set value of lower thumb
        slider.setLowValue(480);
        // show ticks and marks
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        // set a major tick unit every 2 hours on the track
        slider.setMajorTickUnit(120);
    }

    /**
     * Configures the listeners of the RangeSlider needed for the start and end texts.
     *
     * @param converter converts RangeSlider values to hh:mm format
     * @param slider    the RangeSlider that needs configuration
     */
    private void configureRangeSliderListeners(StringConverter<Number> converter, RangeSlider slider) {
        try {
            // listeners to adjust start and end Text objects when thumbs get moved
            slider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    endText.setText("End: " + converter.toString(newValue)));
            slider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    startText.setText("Start: " + converter.toString(newValue)));

            // listeners that make sure the user can only select intervals of 30 minutes
            slider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    slider.setLowValue((newValue.intValue() / 30.0) * 30));
            slider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    slider.setHighValue((newValue.intValue() / 30.0) * 30));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
     * To check if the information filled out by the user is valid.
     *
     * @return true if valid, false otherwise
     */
    private boolean isInputValid() {
        String errorMessage = CalendarEditItemDialogLogic.checkInputValidity(date.getValue(),
                header.getText(), description.getText());

        boolean valid = CalendarEditItemDialogLogic.checkIfAnyError(errorMessage);

        if (!valid) {
            // Show the error message.
            Alert alert = GeneralMethods.createAlert("Invalid fields", errorMessage, dialogStage,
                    Alert.AlertType.ERROR);
            assert alert != null;
            alert.showAndWait();
        }
        return valid;
    }

    /**
     * Configures the date picker to show valid dates and format the dates as needed.
     */
    private void configureDatePicker() {
        try {
            // set the prompt text of the date picker
            date.setPromptText("yyyy-mm-dd");
            // factory to create cell of DatePicker
            Callback<DatePicker, DateCell> dayCellFactory = this.getDayCellFactory();
            date.setDayCellFactory(dayCellFactory);
            // converter to convert value to String and vice versa
            StringConverter<LocalDate> converter = CalendarEditItemDialogLogic.getDatePickerConverter();
            date.setConverter(converter);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Constructs a DayCellFactory for the calendar that only validates future dates.
     *
     * @return Callback to set correct calendar style
     */
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        try {
            return new Callback<>() {

                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item1, boolean empty) {
                            super.updateItem(item1, empty);

                            // Disable all days before today
                            if (item1.isBefore(LocalDate.now())) {
                                // disable the 'button'
                                setDisable(true);
                                // make them red
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Cancels the item creation.
     *
     * @param event to get current stage
     */
    @FXML
    private void cancelClicked(ActionEvent event) {
        // get current stage and close it
        dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.close();
    }

    /**
     * Confirms item creation and sets the class attribute.
     *
     * @param event to get current stage
     */
    @FXML
    private void confirmClicked(ActionEvent event) {
        // check if fields have correct inputs
        if (isInputValid()) {
            // create the item
            // assign value to class attribute
            item = CalendarEditItemDialogLogic.createItem(header.getText(), description.getText(),
                    date.getValue(), startText.getText(), endText.getText());
            // get current stage and close
            dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.close();
        }
    }

}