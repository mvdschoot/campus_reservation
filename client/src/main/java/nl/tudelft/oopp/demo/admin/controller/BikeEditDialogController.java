package nl.tudelft.oopp.demo.admin.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;
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
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.general.GeneralMethods;

import org.controlsfx.control.RangeSlider;

/**
 * Class that controls the dialog box when an admin edits or creates a bike reservation.
 */
public class BikeEditDialogController {

    public static BikeReservation bikeReservation;
    public static boolean edit;
    private Logger logger = Logger.getLogger("GlobalLogger");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private ComboBox<User> bikeUsernameComboBox;
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
    public BikeEditDialogController() {
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
                    AdminManageBikeReservationViewController.currentSelectedBikeReservation;
            this.bikeReservation = null;

            // set the value factory for the quantity spinner
            setQuantitySpinnerValueFactory();

            // Initialize the username combobox
            ObservableList<User> userList = User.getUserData();
            bikeUsernameComboBox.setItems(userList);
            this.setBikeUsernameComboBoxConverter(userList);

            // if reservation is being edited, disable the user combobox
            if (edit) {
                bikeUsernameComboBox.setDisable(true);
            }

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
                if (bikeDate.getValue() != null) {
                    setTimeSlotSlider();
                    availableBikes.setText(String.valueOf(getAvailableBikes()));
                }
            }));
            timeslotSlider.setOnMouseReleased(event -> {
                availableBikes.setText(String.valueOf(getAvailableBikes()));
            });

            // if admin is editing a reservation, fill all the fields with the reservation info
            if (bikeReservation != null) {
                if (userList == null) {
                    return;
                }
                // set the user
                bikeUsernameComboBox.getSelectionModel().select(userList.stream()
                        .filter(x -> x.getUsername().get().equals(
                                bikeReservation.getBikeReservationUser().get().toLowerCase()))
                        .collect(Collectors.toList()).get(0));
                // disbale the user combobox
                bikeUsernameComboBox.setDisable(true);

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
            String selectedStartTime = Objects.requireNonNull(getRangeSliderConverter())
                    .toString(timeslotSlider.getLowValue());
            String selectedEndTime = getRangeSliderConverter().toString(timeslotSlider.getHighValue());
            String selectedDate =
                    Objects.requireNonNull(getDatePickerConverter()).toString(bikeDate.getValue());

            // filter to keep reservations on chosen date, building and starting/ending times
            List<BikeReservation> filteredList = reservationsList.stream()
                    .filter(x -> x.getBikeReservationBuilding().get() == buildingId)
                    .filter(x -> x.getBikeReservationDate().get().equals(selectedDate))
                    .filter(x -> parseTime(x.getBikeReservationStartingTime().get()) < parseTime(selectedEndTime))
                    .filter(x -> parseTime(x.getBikeReservationEndingTime().get()) > parseTime(selectedStartTime))
                    .collect(Collectors.toList());


            // subtract from availableBikes the bike quantity from
            // all the reservations (if reservations falls in current timeslot)
            for (BikeReservation br : filteredList) {
                if (!(edit && AdminManageBikeReservationViewController.currentSelectedBikeReservation
                        .getBikeReservationId().get() == br.getBikeReservationId().get())) {
                    availableBikes = availableBikes - br.getBikeReservationQuantity().get();
                }
            }
            if (availableBikes < 0) {
                availableBikes = 0;
            }
            return availableBikes;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());

        }
        return 0;
    }

    /**
     * Takes in time as String and parses, converts to double.
     * @param time in String
     * @returns the time in double taking 0.5 into account
     */
    public double parseTime(String time) {
        try {
            if (time.contains("23:59:00")) {
                return 24;
            }
            if (time.contains("23:59")) {
                return 24;
            }
            double hour = 0;
            int minute = 0;
            //Checks the format of given String
            if (time.length() == 5) {
                //create localtime object in the given form
                LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
                hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
            } else if (time.length() == 8) {
                LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                hour = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                minute = localTime.get(ChronoField.MINUTE_OF_HOUR);
            }
            //Slider is compatible til 30 minutes checks if minutes is 30
            if (minute == 30) {
                hour = hour + 0.5;
            }

            return hour;
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
            grid.add(timeslotSlider, 1, 5);
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
            return new StringConverter<>() {
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
                public Number fromString(String time) {
                    if (time != null) {
                        String[] split = time.split(":");
                        return Double.parseDouble(split[0]) * 60 + Double.parseDouble(split[1]);
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
     * Set the username combobox converter.
     *
     * @param olu an observable list of users.
     */
    public void setBikeUsernameComboBoxConverter(ObservableList<User> olu) {
        StringConverter<User> converter = new StringConverter<User>() {
            @Override
            public String toString(User object) {
                if (object == null) {
                    return "";
                } else {
                    return object.getUsername().get();
                }
            }

            @Override
            public User fromString(String username) {
                return olu.stream().filter(x -> String.valueOf(x.getUsername()).equals(username))
                        .collect(Collectors.toList()).get(0);
            }
        };
        bikeUsernameComboBox.setConverter(converter);
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
                    this.bikeUsernameComboBox.getSelectionModel().getSelectedItem().getUsername().get());
            bikeReservation.setBikeReservationBuilding(
                    this.bikeBuildingComboBox.getSelectionModel().getSelectedItem().getBuildingId().get());
            bikeReservation.setBikeReservationQuantity(quantity.getValue());
            bikeReservation.setBikeReservationDate(this.bikeDate.getValue().toString());
            bikeReservation.setBikeReservationStartingTime(
                    bikeStartingTime.getText().replace("Start: ", ""));
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

        if (bikeUsernameComboBox.getSelectionModel().getSelectedIndex() < 0) {
            errorMessage += "No valid username selected!\n";
        }
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

    private StringConverter<LocalDate> getDatePickerConverter() {
        try {
            return new StringConverter<>() {
                // set the wanted pattern (format)
                final String pattern = "yyyy-MM-dd";
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                {
                    // set placeholder text for the datePicker
                    bikeDate.setPromptText(pattern.toLowerCase());
                }

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        // get correctly formatted String
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        // get correct LocalDate from String format
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }


}
