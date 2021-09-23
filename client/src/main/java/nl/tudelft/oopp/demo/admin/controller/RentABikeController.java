package nl.tudelft.oopp.demo.admin.controller;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.communication.BikeReservationCommunication;
import nl.tudelft.oopp.demo.entities.BikeReservation;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.CurrentUserManager;
import nl.tudelft.oopp.demo.views.RentABikeView;
import nl.tudelft.oopp.demo.views.SearchView;

import org.controlsfx.control.RangeSlider;


public class RentABikeController implements Initializable {
    public static Stage thisStage;
    private static int currentBuilding = 0;
    public ObservableList<Building> buildingList = Building.getBuildingData();
    public ObservableList<BikeReservation> bikeReservationsList = BikeReservation.getBikeReservationData();
    private Logger logger = Logger.getLogger("GlobalLogger");

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBuilding;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private Text endTime;
    @FXML
    private VBox reservationVbox;
    @FXML
    private Text startTime;
    @FXML
    private Text availableBikeText;
    @FXML
    private Text from;
    @FXML
    private Text until;
    @FXML
    private ImageView image;
    @FXML
    private Button reserveBike;
    @FXML
    private Button backButton;
    private RangeSlider timeSlotSlider;

    /**
     * Deals with the back button function.
     *
     * @param event ActionEvent
     */
    @FXML
    private void backButtonClicked(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            SearchView sv = new SearchView();
            sv.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Used to initialize nodes and populate elements.
     * Gets called before anything.
     *
     * @param location  passed
     * @param resources passed
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            // make sure errors are not visible
            startTime.setVisible(false);
            endTime.setVisible(false);
            from.setVisible(false);
            until.setVisible(false);
            availableBikeText.setVisible(false);
            spinner.setVisible(false);
            reserveBike.setVisible(false);


            ObservableList<String> buildList = FXCollections.observableArrayList();
            for (Building b : buildingList) {
                String temp = b.getBuildingName().get();
                buildList.add(temp);
            }
            comboBuilding.setItems(buildList);
            comboBuilding.setVisibleRowCount(6);


            // set up the date picker and date slider
            configureDatePicker();
            configureRangeSlider();
            timeSlotSlider.setVisible(false);

            changeWidthConstraints(thisStage.getWidth());
            image.setFitHeight(100000);

            // listener that adjusts layout when width of stage changes
            thisStage.widthProperty().addListener((obs, oldVal, newVal) -> changeWidthConstraints(newVal));

            //Listener that occurs when the datepicker is changed
            datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
                //Checks if both date and building are selected
                if (!comboBuilding.getSelectionModel().isEmpty()) {
                    //set values visible
                    startTime.setVisible(true);
                    endTime.setVisible(true);
                    timeSlotSlider.setVisible(true);
                    from.setVisible(true);
                    until.setVisible(true);
                    availableBikeText.setVisible(true);
                    spinner.setVisible(true);
                    reserveBike.setVisible(true);

                    String selectedDate =
                            Objects.requireNonNull(getDatePickerConverter()).toString(datePicker.getValue());
                    int buildNum = getBuildingNumber(comboBuilding.getValue());
                    Building b = Building.getBuildingById(buildNum);
                    //get remaining number of bikes at default time slots
                    int i = getRemainder(b, selectedDate, "18:00", "10:00");
                    availableBikeText.setText("Available Bikes: " + i);
                    setTimeSlotSlider(b);
                }
            });
            //Listenr that occurs when Building is selected
            comboBuilding.valueProperty().addListener((ov, oldValue, newValue) -> {
                if (datePicker.getValue() != null) {
                    startTime.setVisible(true);
                    endTime.setVisible(true);
                    timeSlotSlider.setVisible(true);
                    from.setVisible(true);
                    until.setVisible(true);
                    availableBikeText.setVisible(true);
                    spinner.setVisible(true);
                    reserveBike.setVisible(true);

                    String selectedDate =
                            Objects.requireNonNull(getDatePickerConverter()).toString(datePicker.getValue());
                    int buildNum = getBuildingNumber(comboBuilding.getValue());
                    Building b = Building.getBuildingById(buildNum);
                    int i = getRemainder(b, selectedDate, "18:00", "10:00");
                    availableBikeText.setText("Available Bikes: " + i);
                    setTimeSlotSlider(b);
                }
            });

            //Listener than occurs when the time slot on the slider is changed
            timeSlotSlider.setOnMouseReleased(event -> {
                String selectedDate =
                        Objects.requireNonNull(getDatePickerConverter()).toString(datePicker.getValue());
                String selectedStartTime = Objects.requireNonNull(getRangeSliderConverter())
                        .toString(timeSlotSlider.getLowValue());
                String selectedEndTime = getRangeSliderConverter().toString(timeSlotSlider.getHighValue());
                int buildingNum = getBuildingNumber(comboBuilding.getValue());

                Building b = Building.getBuildingById(buildingNum);
                int availableBikes = getRemainder(b, selectedDate, selectedEndTime, selectedStartTime);
                availableBikeText.setText("Available Bikes: " + availableBikes);
                setTimeSlotSlider(b);
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Resets the length of slider depending on the building selected.
     *
     * @param event Event
     */
    @FXML
    private void comboAction(ActionEvent event) {

        //Retrieve the building picked on comboBox
        int buildingNum = getBuildingNumber(comboBuilding.getValue());

        Building b = Building.getBuildingById(buildingNum);

        //Get opening times and parse
        double startTime = parseTime(b.getOpeningTime().get());
        double closingTime = parseTime(b.getClosingTime().get());

        //Set the range of the slider in minutes
        timeSlotSlider.setMin((int) startTime * 60);
        timeSlotSlider.setMax((int) closingTime * 60);


    }

    /**
     * Deals with Reserve Now button clicked
     * Method that retrieves values from the functionalities and returns to the server  if all the.
     * requirements are met.
     *
     * @param event ActionEvent
     * @throws IOException throws exception
     */
    @FXML
    private void reserveNowClicked(ActionEvent event) throws IOException {
        try {
            // retrieve date, bike number and time slot from the corresponding boxes
            String selectedDate =
                    Objects.requireNonNull(getDatePickerConverter()).toString(datePicker.getValue());
            String selectedStartTime = Objects.requireNonNull(getRangeSliderConverter())
                    .toString(timeSlotSlider.getLowValue());
            String selectedEndTime = getRangeSliderConverter().toString(timeSlotSlider.getHighValue());
            String selectedBuildingAndBike = comboBuilding.getValue();
            String selectedBuilding = getSelectedBuilding(selectedBuildingAndBike);
            Integer selectedBike = spinner.getValue();

            int buildNumber = getBuildingNumber(selectedBuilding);
            Building b = Building.getBuildingById(buildNumber);

            // check to see enough bikes for selected building
            if (selectedEndTime.equals(selectedStartTime)) {
                Alert alert = GeneralMethods.createAlert("Same time",
                        "Please select valid time slot!",
                        ((Node) event.getSource()).getScene().getWindow(),
                        Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                alert.showAndWait();
            } else {
                if (getRemainder(b, selectedDate, selectedEndTime, selectedStartTime) - selectedBike >= 0) {
                    Alert alert = GeneralMethods.createAlert("Your Bike Reservation",
                            "Make reservation for " + selectedBike + " bike(s) from " + selectedBuilding
                                    + " on " + selectedDate + " for " + selectedStartTime + "-"
                                    + selectedEndTime + "?",
                            ((Node) event.getSource()).getScene().getWindow(),
                            Alert.AlertType.CONFIRMATION);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.orElse(null) == ButtonType.OK) {
                        if (selectedEndTime.contains("24")) {
                            selectedEndTime = "23:59";
                        }
                        BikeReservationCommunication
                                .createBikeReservation(getBuildingNumber(selectedBuilding),
                                        CurrentUserManager.getUsername(), selectedBike,
                                        selectedDate, selectedStartTime, selectedEndTime);
                        confirmAlert(event);
                    }
                } else {
                    Alert alert = GeneralMethods.createAlert("Insufficient Bikes",
                            "Insufficient Bikes Available. Please check the number of bikes available",
                            ((Node) event.getSource()).getScene().getWindow(), Alert.AlertType.WARNING);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Sends cofirmation alert to the user.
     *
     * @param event handles event
     */
    public void confirmAlert(ActionEvent event) {
        try {
            // inform the user for successful reservation
            Alert alert = GeneralMethods.createAlert("Bike Reserved",
                    "You successfully reserved the bike(s)!",
                    ((Node) event.getSource()).getScene().getWindow(), Alert.AlertType.CONFIRMATION);
            alert.showAndWait();

            // re-open the scene to update new number of bikes left
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            RentABikeView rbv = new RentABikeView();
            rbv.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Create cellFactory for the datePicker that disables all days before today and weekend days.
     * It also marks them red to make sure the user understands why they are disabled.
     *
     * @return a CallBack to set the datePicker in {@link #configureDatePicker()}
     */
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        try {
            return new Callback<>() {

                @Override
                public DateCell call(final DatePicker datePicker1) {
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
     * Methods that sets the dayCellFactory made in {@link #getDayCellFactory()}
     * and the StringConverter made in {@link #getDatePickerConverter()}.
     */
    private void configureDatePicker() {
        try {
            // factory to create cell of DatePicker
            Callback<DatePicker, DateCell> dayCellFactory = this.getDayCellFactory();
            // set the factory
            datePicker.setDayCellFactory(dayCellFactory);
            // converter to convert value to String and vice versa
            StringConverter<LocalDate> converter = getDatePickerConverter();
            // set the converter
            datePicker.setConverter(converter);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Creates a StringConverter that converts the selected value to a usable Date (in String format).
     *
     * @return a StringConverter object
     */
    private StringConverter<LocalDate> getDatePickerConverter() {
        try {
            return new StringConverter<>() {
                // set the wanted pattern (format)
                final String pattern = "yyyy-MM-dd";
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                {
                    // set placeholder text for the datePicker
                    datePicker.setPromptText(pattern.toLowerCase());
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

    /**
     * .
     * This method gets the Building ID based on the
     * buildingName given in String.
     *
     * @param name The selected Building Name
     */
    private int getBuildingNumber(String name) {
        try {
            int buildingNumber = 0;

            //look for specific buildingID with the given String one by one
            for (Building b : buildingList) {
                if (name.equals(b.getBuildingName().get())) {
                    buildingNumber = b.getBuildingId().get();
                    break;
                }
            }
            return buildingNumber;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return 0;
    }

    /**
     * Creates a StringConverter that converts the selected value to an actual time (in String format).
     *
     * @return a StringConverter object
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
     * .
     * Configure the rangeSlider listeners. The listeners make sure that the user jumps
     * intervals of an hour and sets the texts with the correct value.
     *
     * @param converter String converter that is created in {@link #getRangeSliderConverter()}
     */
    private void configureRangeSliderListeners(StringConverter<Number> converter) {
        try {
            // listeners to adjust start and end Text objects when thumbs get moved
            timeSlotSlider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    endTime.setText(converter.toString(newValue)));
            timeSlotSlider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    startTime.setText(converter.toString(newValue)));

            // listeners that make sure the user can only select intervals of 30 minutes
            timeSlotSlider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    timeSlotSlider.setLowValue((newValue.intValue() / 30) * 30));
            timeSlotSlider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    timeSlotSlider.setHighValue((newValue.intValue() / 30) * 30));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
     * Create a range slider (slider with two 'thumbs') adjusted to hours and minutes.
     */
    private void configureRangeSlider() {
        try {
            // initialize the RangeSlider (values are handled as minutes) and the positions of the thumbs
            timeSlotSlider = new RangeSlider(480, 1440, 600, 1080);
            timeSlotSlider.setLowValue(600);
            timeSlotSlider.setMinWidth(100);
            timeSlotSlider.setMaxWidth(200);
            timeSlotSlider.setShowTickLabels(true);
            timeSlotSlider.setShowTickMarks(true);
            timeSlotSlider.setMajorTickUnit(120);
            timeSlotSlider.setMinorTickCount(4);

            // get and set the StringConverter to show hh:mm format
            StringConverter<Number> converter = getRangeSliderConverter();
            timeSlotSlider.setLabelFormatter(converter);

            // add listeners to show the current thumb values in separate Text objects
            configureRangeSliderListeners(converter);

            // initialize the Text objects with the current values of the thumbs
            assert converter != null;
            startTime.setText(converter.toString(timeSlotSlider.getLowValue()));
            endTime.setText(converter.toString(timeSlotSlider.getHighValue()));

            // inject the RangeSlider in the JavaFX layout
            reservationVbox.getChildren().add(4, timeSlotSlider);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Getting value directly from the comboBox includes the number of available bikes, as well as the
     * building name, hence this method is used.
     *
     * @param st Building Name
     * @return Name of the building
     */
    public String getSelectedBuilding(String st) {
        try {
            String result = "";
            for (int i = 0; i < buildingList.size(); i++) {
                if (st.contains(buildingList.get(i).getBuildingName().get())) {
                    result = buildingList.get(i).getBuildingName().get();
                    currentBuilding = i;
                    break;
                }
            }
            return result;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Automatically resizes the image as the window size changes.
     *
     * @param newWidth width of the window
     */
    private void changeWidthConstraints(Number newWidth) {
        try {
            // set the width of some nodes based on the calculated ratio between their width and the stages width
            image.setFitWidth((newWidth.doubleValue() - 188) / 1.41550696);
            reservationVbox.setPrefWidth((newWidth.doubleValue() - 188) / 3.3969);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }


    /**
     * Parses time into hours. Deals with both the formats.
     *
     * @param time Provided time
     * @return only hour of given string.
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
     * Gets remaining number of bikes by reducing bikes rented from maximum bike of a building.
     *
     * @param b             given object
     * @param selectedDate  given date
     * @param selectedEnd   given end time of rent
     * @param selectedStart give start time of rent
     * @return remaining value for each building
     */
    private int getRemainder(Building b, String selectedDate, String selectedEnd, String selectedStart) {
        try {

            int remainder = b.getBuildingMaxBikes().get();

            //Using stream to filter out given conditions
            List<BikeReservation> filteredReservations = bikeReservationsList.stream()
                    .filter(x -> x.getBikeReservationBuilding().get() == b.getBuildingId().get())
                    .filter(x -> x.getBikeReservationDate().get().equals(selectedDate))
                    .filter(x -> parseTime(x.getBikeReservationStartingTime().get()) < parseTime(selectedEnd))
                    .filter(x -> parseTime(x.getBikeReservationEndingTime().get()) > parseTime(selectedStart))
                    .collect(Collectors.toList());

            //Go through the list one by one and subtract from total
            for (BikeReservation br : filteredReservations) {
                remainder = remainder - br.getBikeReservationQuantity().get();
            }
            //Accidentally made bike number negative in the developping process
            if (remainder < 0) {
                remainder = 0;
            }
            return remainder;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return 0;
    }

    /**
     * Adjusts the intervals of the slider depending on the opening hours.
     *
     * @param selectedBuilding Building passed
     */
    private void setTimeSlotSlider(Building selectedBuilding) {
        try {
            StringConverter<Number> converter = getRangeSliderConverter();

            double opening;
            double closing;

            if (selectedBuilding != null) {
                opening = (double) converter.fromString(selectedBuilding.getOpeningTime().get());
                closing = (double) converter.fromString(selectedBuilding.getClosingTime().get());
                if (closing == 1439) {
                    timeSlotSlider.setMax(1440);
                    timeSlotSlider.setMajorTickUnit((1440 - opening) / 3);
                } else {
                    timeSlotSlider.setMax(closing);
                    timeSlotSlider.setMajorTickUnit((closing - opening) / 3);
                }
                timeSlotSlider.setMin(opening);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
}