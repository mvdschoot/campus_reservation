package nl.tudelft.oopp.demo.admin.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.admin.logic.BuildingEditDialogLogic;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.logic.RoomViewLogic;

import org.controlsfx.control.RangeSlider;

public class BuildingEditDialogController {

    public static Building building;
    private Logger logger = Logger.getLogger("GlobalLogger");
    @FXML
    private GridPane grid;
    @FXML
    private TextField buildingNameField;
    @FXML
    private TextField buildingAddressField;
    @FXML
    private Spinner<Integer> maxBikesField;
    @FXML
    private Text openingTime;
    @FXML
    private Text closingTime;
    // double thumb slider
    private RangeSlider openingHoursSlider;
    private Stage dialogStage;

    private static void emptyBuilding() {
        building = new Building();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        Building building = AdminManageBuildingViewController.currentSelectedBuilding;
        StringConverter<Number> converter = getRangeSliderConverter();

        setMaxBikesSpinnerValueFactory();
        configureRangeSlider();

        if (building == null) {
            return;
        }

        String closing = building.getClosingTime().get();
        String opening = building.getOpeningTime().get();

        if (closing.contains("59")) {
            openingHoursSlider.setHighValue(1440);
        } else {
            openingHoursSlider.setHighValue((double) converter.fromString(closing));
        }
        openingHoursSlider.setLowValue((double) converter.fromString(opening));
        maxBikesField.getValueFactory().setValue(building.getBuildingMaxBikes().getValue());
        buildingNameField.setText(building.getBuildingName().get());
        buildingAddressField.setText(building.getBuildingAddress().get());
    }

    private void configureRangeSlider() {
        try {
            // initialize the RangeSlider (values are handled as minutes) and the positions of the thumbs
            openingHoursSlider = new RangeSlider(0, 1440, 480, 1760);
            openingHoursSlider.setLowValue(480);
            openingHoursSlider.setMinWidth(100);
            openingHoursSlider.setMaxWidth(270);
            openingHoursSlider.setShowTickLabels(true);
            openingHoursSlider.setShowTickMarks(true);
            openingHoursSlider.setMajorTickUnit(120);
            openingHoursSlider.setMinorTickCount(4);

            // get and set the StringConverter to show hh:mm format
            StringConverter<Number> converter = getRangeSliderConverter();
            openingHoursSlider.setLabelFormatter(converter);

            // add listeners to show the current thumb values in separate Text objects
            configureRangeSliderListeners(converter);

            // initialize the Text objects with the current values of the thumbs
            openingTime.setText(converter.toString(openingHoursSlider.getLowValue()));
            closingTime.setText(converter.toString(openingHoursSlider.getHighValue()));

            // inject the RangeSlider in the JavaFX layout
            grid.add(openingHoursSlider, 1, 3);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private void configureRangeSliderListeners(StringConverter<Number> converter) {
        try {
            // listeners to adjust start and end Text objects when thumbs get moved
            openingHoursSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
                closingTime.setText(converter.toString(newValue));
            });
            openingHoursSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
                openingTime.setText(converter.toString(newValue));
            });

            // listeners that make sure the user can only select intervals of 30 minutes
            openingHoursSlider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    openingHoursSlider.setLowValue((newValue.intValue() / 30) * 30));
            openingHoursSlider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    openingHoursSlider.setHighValue((newValue.intValue() / 30) * 30));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private StringConverter<Number> getRangeSliderConverter() {
        try {
            return new StringConverter<Number>() {
                @Override
                public String toString(Number n) {
                    return RoomViewLogic.toStringNum(n);
                }

                @Override
                public Number fromString(String time) {
                    return RoomViewLogic.fromStringTime(time);
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * .
     * Creates and assigns a Spinner Value Factory to the max bikes spinner which restricts the
     * minimum, maximum, initial value and step size
     */
    private void setMaxBikesSpinnerValueFactory() {
        try {
            // create new value factory
            SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, Integer.MAX_VALUE, 0, 1);
            // set the factory
            maxBikesField.setValueFactory(factory);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOkClicked(ActionEvent event) {
        if (isInputValid()) {
            emptyBuilding();
            StringConverter<Number> converter = getRangeSliderConverter();
            building.setBuildingName(buildingNameField.getText());
            building.setBuildingMaxBikes(maxBikesField.getValue());
            building.setBuildingAddress(buildingAddressField.getText());
            building.setOpeningTime(converter.toString(openingHoursSlider.getLowValue()));
            // if building closes at 12am it should be put in the database as 11:59pm
            if (openingHoursSlider.getHighValue() == 1440) {
                building.setClosingTime("23:59");
            } else {
                building.setClosingTime(converter.toString(openingHoursSlider.getHighValue()));
            }
            this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancelClicked(ActionEvent event) {
        building = null;
        this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String buildingName = buildingNameField.getText();
        String buildingAddress = buildingAddressField.getText();
        double openingHoursLow = openingHoursSlider.getLowValue();
        double openingHoursHigh = openingHoursSlider.getHighValue();

        String errorMessage = BuildingEditDialogLogic.isValidInput(buildingName, buildingAddress,
                openingHoursHigh, openingHoursLow);

        if (errorMessage.equals("")) {
            return true;
        } else {
            // Show the error message.
            GeneralMethods.alertBox("Invalid Fields", "Please correct invalid fields",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }

    }
}
