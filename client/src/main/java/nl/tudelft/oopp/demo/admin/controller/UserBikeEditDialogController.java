package nl.tudelft.oopp.demo.admin.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;

import nl.tudelft.oopp.demo.views.AdminUserBikeView;
import org.controlsfx.control.RangeSlider;

public class UserBikeEditDialogController {

    public static BikeReservation bikeReservation;
    public static boolean edit;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Logger logger = Logger.getLogger("GlobalLogger");

    @FXML
    private ComboBox<Building> bikeBuildingComboBox;
    @FXML
    private Spinner<Integer> quantity;
    @FXML
    private DatePicker bikeDate;
    @FXML
    private Text bikeStartingTime;
    @FXML
    private Text bikeEndingTime;
    @FXML
    private GridPane grid;
    @FXML
    private Label timeslot;
    @FXML
    private Text availableBikes;
    private RangeSlider timeslotSlider;
    private Stage dialogStage;

    /**
     * .
     * Default constructor of the BikeEditDialogController class.
     */
    public UserBikeEditDialogController() {
    }

    /**
     * Create a new bike reservation when called.
     */
    private static void emptyReservation() {
        bikeReservation = new BikeReservation();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        try {
            final BikeReservation bikeReservation =
                    AdminUserBikeViewController.currentSelectedBikeReservation;
            this.bikeReservation = null;

            // set the value factory for the quantity spinner
            setQuantitySpinnerValueFactory();

            bikeDate.setConverter(getDateConverter());

            // Initialize the building combobox
            ObservableList<Building> buildingList = Building.getBuildingData();
            bikeBuildingComboBox.setItems(buildingList);
            this.setBikeBuildingComboBoxConverter(buildingList);

            //This method sets up the slider which determines the time of reservation in the dialog view.
            configureRangeSlider();


            // sets the cell factory for the date picker
            bikeDate.setDayCellFactory(getDayCellFactory());
            // set the string converter of the date picker
            bikeDate.setConverter(getDateConverter());

            // when date gets changed, adjust available bikes text
            bikeDate.valueProperty().addListener(((observable, oldValue, newValue) -> {
                if (bikeBuildingComboBox.getValue() != null) {
                    availableBikes.setText(String.valueOf(getAvailableBikes()));
                }
            }));

            // when selected building changes, adjust available bikes text
            bikeBuildingComboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
                setTimeSlotSlider();
                if (bikeDate.getValue() != null) {
                    availableBikes.setText(String.valueOf(getAvailableBikes()));
                }
            }));

            // if admin is editing a reservation, fill all the fields with the reservation info
            if (bikeReservation != null) {

                // set the quantity
                quantity.getValueFactory().setValue(bikeReservation.getBikeReservationQuantity().getValue());

                if (buildingList == null) {
                    return;
                }
                // set the building
                bikeBuildingComboBox.getSelectionModel().select(buildingList.stream()
                        .filter(x -> x.getBuildingId().get() == bikeReservation.getBikeReservationBuilding().get())
                        .collect(Collectors.toList()).get(0));

                // set the date
                bikeDate.setValue(LocalDate.parse(bikeReservation.getBikeReservationDate().get(), formatter));
                String[] startTimeSplit = bikeReservation.getBikeReservationStartingTime().get().split(":");

                // set the timeslot thumbs
                timeslotSlider.setLowValue(Double.parseDouble(startTimeSplit[0]) * 60.0
                        + Double.parseDouble(startTimeSplit[1]));
                String[] endTimeSplit = bikeReservation.getBikeReservationEndingTime().get().split(":");
                timeslotSlider.setHighValue(Double.parseDouble(endTimeSplit[0]) * 60.0
                        + Double.parseDouble(endTimeSplit[1]));

                // set the start and end time texts
                bikeStartingTime.setText("Start: " + getRangeSliderConverter()
                        .toString(timeslotSlider.getLowValue()));
                bikeEndingTime.setText("End: " + getRangeSliderConverter()
                        .toString(timeslotSlider.getHighValue()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private void setTimeSlotSlider() {
        try {
            Building selectedBuilding = bikeBuildingComboBox.getSelectionModel().getSelectedItem();
            StringConverter<Number> converter = getRangeSliderConverter();

            double opening;
            double closing;

            if (selectedBuilding != null) {
                opening = (double) converter.fromString(selectedBuilding.getOpeningTime().get());
                closing = (double) converter.fromString(selectedBuilding.getClosingTime().get());
                if (closing == 1439) {
                    timeslotSlider.setMax(1440);
                    timeslotSlider.setMajorTickUnit((1440 - opening) / 3);
                } else {
                    timeslotSlider.setMax(closing);
                    timeslotSlider.setMajorTickUnit((closing - opening) / 3);
                }
                timeslotSlider.setMin(opening);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
     * Creates and assigns a Spinner Value Factory to the quantity spinner which restricts the
     * minimum, maximum, initial value and step size
     */
    private void setQuantitySpinnerValueFactory() {
        try {
            // create new value factory
            SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    1, Integer.MAX_VALUE, 1, 1);
            // set the factory
            quantity.setValueFactory(factory);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * gets the amount of available bikes at a given moment (timeslot).
     *
     * @return int amount of available bikes
     */
    private int getAvailableBikes() {
        try {
            // get the amount of bikes that the chosen building has
            int availableBikes = bikeBuildingComboBox.getValue().getBuildingMaxBikes().get();

            // get all the bike reservations for the chosen building
            int buildingId = bikeBuildingComboBox.getValue().getBuildingId().get();
            ObservableList<BikeReservation> reservationsList =
                    BikeReservation.getBikeReservationsByBuilding(buildingId);

            // filter to keep reservations on chosen date
            List<BikeReservation> filteredList = reservationsList.stream()
                    .filter(x -> x.getBikeReservationDate().get()
                            .equals(getDateConverter().toString(bikeDate.getValue())))
                    .collect(Collectors.toList());

            // get current slider values
            double currentStart = timeslotSlider.getLowValue();
            double currentEnd = timeslotSlider.getHighValue();

            // subtract from availableBikes the bike quantity from
            // all the reservations (if reservations falls in current timeslot)
            for (BikeReservation br : filteredList) {
                // if admin is editing a reservation don't subtract amount of the current reservation
                if (edit && AdminUserBikeViewController.currentSelectedBikeReservation
                        .getBikeReservationId().get() == br.getBikeReservationId().get()) {
                    continue;
                }
                // get start and end time
                double startTime = (double) getRangeSliderConverter()
                        .fromString(br.getBikeReservationStartingTime().get());
                double endTime = (double) getRangeSliderConverter()
                        .fromString(br.getBikeReservationEndingTime().get());

                // check if timeslots overlap
                if (!(startTime < currentStart && endTime < currentStart)
                        && !(startTime > currentEnd && endTime > currentEnd)) {
                    // subtract amount of bikes
                    availableBikes -= br.getBikeReservationQuantity().get();
                }
            }
            return availableBikes;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return 0;
    }

    /**
     * Constructor for the cellFactory of the DatePicker.
     * Makes sure no day before today + weekend days are available.
     *
     * @return a ready to use CallBack that invalidates unwanted dates in the date picker
     */
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        try {
            return new Callback<DatePicker, DateCell>() {

                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            // Disable all days before today + weekend days
                            if (item.isBefore(LocalDate.now()) || item.getDayOfWeek() == DayOfWeek.SATURDAY
                                    || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
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
     * Configures the RangeSlider.
     * Sets the CSS, converter and listeners as well.
     */
    private void configureRangeSlider() {
        try {
            // initialize the RangeSlider (values are handled as minutes) and the positions of the thumbs
            timeslotSlider = new RangeSlider(480, 1440, 600, 1080);
            timeslotSlider.setLowValue(600);
            timeslotSlider.setMinWidth(100);
            timeslotSlider.setMaxWidth(200);
            timeslotSlider.setShowTickLabels(true);
            timeslotSlider.setShowTickMarks(true);
            timeslotSlider.setMajorTickUnit(120);

            // get and set the StringConverter to show hh:mm format
            StringConverter<Number> converter = getRangeSliderConverter();
            timeslotSlider.setLabelFormatter(converter);

            // add listeners to show the current thumb values in separate Text objects
            configureRangeSliderListeners(converter);

            // initialize the Text objects with the current values of the thumbs
            bikeStartingTime.setText("Start: " + converter.toString(timeslotSlider.getLowValue()));
            bikeEndingTime.setText("End: " + converter.toString(timeslotSlider.getHighValue()));

            // inject the RangeSlider in the JavaFX layout
            grid.add(timeslotSlider, 1, 4);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Configure RangeSlider listeners that change the Start and End text values.
     * This method also makes sure that the user can only pick timeslots of 30 min
     *
     * @param converter StringConverter that converts slider value to String hh:mm format
     */
    private void configureRangeSliderListeners(StringConverter<Number> converter) {
        try {
            // listeners to adjust start and end Text objects when thumbs get moved
            timeslotSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
                bikeEndingTime.setText("End: " + converter.toString(newValue));
            });
            timeslotSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
                bikeStartingTime.setText("Start: " + converter.toString(newValue));
            });

            timeslotSlider.highValueChangingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                                    Boolean wasChanging, Boolean changing) {
                    if (!changing && bikeBuildingComboBox.getValue() != null && bikeDate.getValue() != null) {
                        availableBikes.setText(String.valueOf(getAvailableBikes()));
                    }
                }
            });

            timeslotSlider.lowValueChangingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                                    Boolean wasChanging, Boolean changing) {
                    if (!changing && bikeBuildingComboBox.getValue() != null && bikeDate.getValue() != null) {
                        availableBikes.setText(String.valueOf(getAvailableBikes()));
                    }
                }
            });

            // listeners that make sure the user can only select intervals of 30 minutes
            timeslotSlider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    timeslotSlider.setLowValue((newValue.intValue() / 30) * 30));
            timeslotSlider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    timeslotSlider.setHighValue((newValue.intValue() / 30) * 30));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Constructor for the RangeSlider converter that converts the slider values to String hh:mm format.
     *
     * @return constructed StringConverter
     */
    private StringConverter<Number> getRangeSliderConverter() {
        try {
            return new StringConverter<Number>() {
                @Override
                public String toString(Number n) {
                    // calculate hours and remaining minutes to get a correct hh:mm format
                    long minutes = n.longValue();
                    long hours = TimeUnit.MINUTES.toHours(minutes);
                    long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
                    // '%02d' means that there will be a 0 in front if its only 1 number + it's a long number
                    return String.format("%02d", hours) + ":" + String.format("%02d", remainingMinutes);
                }


                @Override
                public Number fromString(String string) {
                    String[] split = string.split(":");
                    double hours = Double.parseDouble(split[0]);
                    double minutes = Double.parseDouble(split[1]);
                    return hours * 60 + minutes;
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Constructor for the converter that converts LocalDate objects to String yyyy-MM-dd format.
     *
     * @return constructed StringConverter
     */
    private StringConverter<LocalDate> getDateConverter() {
        try {
            return new StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate dateSelected) {
                    if (dateSelected != null) {
                        return formatter.format(dateSelected);
                    }
                    return "null";
                }

                @Override
                public LocalDate fromString(String string) {
                    // The date is formatted in yyyy-MM-dd format from the datePicker.
                    if (string != null && !string.trim().isEmpty()) {
                        return LocalDate.parse(string, formatter);
                    }
                    return null;
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Set the building combobox converter.
     *
     * @param olb an observable list of buildings.
     */
    public void setBikeBuildingComboBoxConverter(ObservableList<Building> olb) {
        StringConverter<Building> converter = new StringConverter<Building>() {
            @Override
            public String toString(Building object) {
                if (object == null) {
                    return "";
                } else {
                    return object.getBuildingName().get();
                }
            }

            @Override
            public Building fromString(String id) {
                return olb.stream().filter(x -> String.valueOf(x.getBuildingId()) == id)
                        .collect(Collectors.toList()).get(0);
            }
        };
        bikeBuildingComboBox.setConverter(converter);
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOkClicked(ActionEvent event) {
        // Check the validity of user input
        if (isInputValid()) {
            emptyReservation();
            // Set the user input to the reservation
            bikeReservation.setBikeReservationUser(
                    AdminManageUserViewController.currentSelectedUser.getUsername().get());
            bikeReservation.setBikeReservationBuilding(
                    this.bikeBuildingComboBox.getSelectionModel().getSelectedItem().getBuildingId().get());
            bikeReservation.setBikeReservationQuantity(quantity.getValue());
            bikeReservation.setBikeReservationDate(this.bikeDate.getValue().toString());
            bikeReservation.setBikeReservationStartingTime(bikeStartingTime.getText().replace("Start: ", ""));
            bikeReservation.setBikeReservationEndingTime(
                    bikeEndingTime.getText().replace("End: ", "").equals("24:00")
                            ? "23:59" : bikeEndingTime.getText().replace("End: ", ""));
            // Close the dialog window
            this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancelClicked(ActionEvent event) {
        bikeReservation = null;
        // Close the dialog window
        this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.close();
    }

    /**
     * Validates the user input.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (bikeBuildingComboBox.getSelectionModel().getSelectedIndex() < 0) {
            errorMessage += "No valid building selected!\n";
        }
        if (bikeDate.getValue() == null) {
            errorMessage += "No valid date selected!\n";
        }
        if (availableBikes.getText().equals("-")) {
            // do nothing
        } else if (Integer.parseInt(availableBikes.getText()) < quantity.getValue()) {
            errorMessage += "You cannot book " + quantity.getValue() + " bike(s) when there are/is only "
                    + availableBikes.getText() + " bike(s) left";
        }
        if (timeslotSlider.getLowValue() == timeslotSlider.getHighValue()) {
            errorMessage += "No valid timeslot selected!\n";
        }
        if (errorMessage.equals("")) {
            return true;
        } else {
            // Show the error message.
            GeneralMethods.alertBox("Invalid Fields", "Please correct the invalid fields",
                    errorMessage, Alert.AlertType.ERROR);

            return false;
        }
    }

}
