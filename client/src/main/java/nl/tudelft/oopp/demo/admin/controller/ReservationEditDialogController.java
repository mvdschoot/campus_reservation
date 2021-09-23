package nl.tudelft.oopp.demo.admin.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.admin.logic.ReservationEditDialogLogic;

import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.general.GeneralMethods;

import org.controlsfx.control.RangeSlider;

/**
 * .
 * This controller class is invokes on the onclick of the newReservationButton/ editReservationButton
 * in the AdminManageReservationViewController class.
 * This controller class displays a dialog box for the admin to create/update reservations.
 */
public class ReservationEditDialogController {

    public static Reservation reservation;
    public static Stage dialogStage;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String pathSeparator = File.separator;
    @FXML
    private ComboBox<User> username;
    @FXML
    private ComboBox<Room> room;
    @FXML
    private DatePicker date;
    @FXML
    private Text startTime;
    @FXML
    private Text endTime;
    @FXML
    private GridPane grid;
    @FXML
    private Label timeslot;
    private RangeSlider timeslotSlider;

    /**
     * .
     * Default constructor of the ReservationEditDialogController class.
     */
    public ReservationEditDialogController() {
    }

    /**
     * Create a new empty reservation.
     */
    public static void emptyReservation() {
        reservation = new Reservation();
    }

    /**
     * .
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        try {
            final Reservation reservation = AdminManageReservationViewController.currentSelectedReservation;
            this.reservation = null;

            date.setConverter(getDateConverter());
            final ObservableList<User> userObservableList = User.getUserData();
            final ObservableList<Room> roomObservableList = Room.getRoomData();

            //Initializing the observable list for the users available
            username.setItems(userObservableList);
            this.setUserComboBoxConverter(userObservableList);
            username.valueProperty().addListener(((observable, oldValue, newValue) -> {
                usernameSelected(newValue);
            }));

            this.setRoomComboBoxConverter(roomObservableList);

            //This method sets up the slider which determines the time of reservation in the dialog view.
            configureRangeSlider();

            date.setDayCellFactory(getDayCellFactory());

            // change CSS when date changes or room changes
            date.valueProperty().addListener(((observable, oldValue, newValue) -> {
                if (room.getSelectionModel().getSelectedIndex() >= 0) {
                    configureCss();
                }
            }));
            room.valueProperty().addListener(((observable, oldValue, newValue) -> {
                setTimeSlotSlider();
                configureCss();
            }));

            if (reservation != null) {
                if (userObservableList == null) {
                    return;
                }
                username.getSelectionModel().select(userObservableList.stream()
                        .filter(x -> x.getUsername().get().equals(reservation.getUsername().get().toLowerCase()))
                        .collect(Collectors.toList()).get(0));
                username.setDisable(true);

                if (roomObservableList == null) {
                    return;
                }
                room.getSelectionModel().select(roomObservableList.stream()
                        .filter(x -> x.getRoomId().get() == reservation.getRoom().get())
                        .collect(Collectors.toList()).get(0));

                StringConverter<Number> sliderConverter = getRangeSliderConverter();
                StringConverter<LocalDate> dateConverter = getDateConverter();

                date.setValue(dateConverter.fromString(reservation.getDate().get()));
                double startTimeValue = (double) sliderConverter
                        .fromString(reservation.getReservationStartingTime().get());
                double endTimeValue = (double) sliderConverter
                        .fromString(reservation.getReservationEndingTime().get());

                timeslotSlider.setLowValue(startTimeValue);
                timeslotSlider.setHighValue(endTimeValue);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private void setTimeSlotSlider() {
        try {
            Building selectedBuilding = Building.getBuildingById(
                    room.getSelectionModel().getSelectedItem().getRoomBuilding().get());
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
            timeslotSlider.setMaxWidth(240);
            timeslotSlider.setShowTickLabels(true);
            timeslotSlider.setShowTickMarks(true);
            timeslotSlider.setMajorTickUnit(120);

            // get and set the StringConverter to show hh:mm format
            StringConverter<Number> converter = getRangeSliderConverter();
            timeslotSlider.setLabelFormatter(converter);

            // add listeners to show the current thumb values in separate Text objects
            configureRangeSliderListeners(converter);

            // initialize the Text objects with the current values of the thumbs
            startTime.setText("Start: " + converter.toString(timeslotSlider.getLowValue()));
            endTime.setText("End: " + converter.toString(timeslotSlider.getHighValue()));

            // inject the RangeSlider in the JavaFX layout
            grid.add(timeslotSlider, 1, 3);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Configures the CSS of the RangeSlider.
     * Configure (in CSS) the colors of the track of the range slider to show in green the available timeslots
     * and in red the rest
     */
    private void configureCss() {
        try {
            // get currently selected room
            Room selectedRoom = room.getSelectionModel().getSelectedItem();
            // get css file and delete its content to fill it again
            File css = new File(getClass().getResource("/RangeSlider.css")
                    .getPath().replace("/", pathSeparator));
            css.delete();
            css.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(css));

            if (selectedRoom == null) {
                // make track completely white
                GeneralMethods.setSliderDefaultCss(timeslotSlider, bw,
                        getClass().getResource("/RangeSlider.css").toExternalForm());
                return;
            }
            // get reservations for this room on the selected date
            List<Reservation> reservations = Reservation.getRoomReservationsOnDate(selectedRoom.getRoomId().get(),
                    date.getValue(), getDateConverter());

            // sort them in ascending order
            reservations.sort(new Comparator<Reservation>() {
                @Override
                public int compare(Reservation o1, Reservation o2) {
                    // split time in hh:mm
                    String[] o1StartSplit = o1.getReservationStartingTime().get().split(":");
                    int o1StartHour = Integer.parseInt(o1StartSplit[0]);
                    int o1StartMinute = Integer.parseInt(o1StartSplit[1]);

                    String[] o2StartSplit = o2.getReservationStartingTime().get().split(":");
                    int o2StartHour = Integer.parseInt(o2StartSplit[0]);
                    int o2StartMinute = Integer.parseInt(o2StartSplit[1]);

                    // compare hours and minutes
                    if (o1StartHour < o2StartHour) {
                        return -1;
                    } else if (o1StartHour > o2StartHour) {
                        return 1;
                    }
                    if (o1StartMinute < o2StartMinute) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            // first part of css
            bw.write(".track {\n" + "\t-fx-background-color: linear-gradient(to right, ");

            // iterator to loop over all the reservations
            Iterator<Reservation> it = reservations.iterator();

            // if there are no reservations make the track completely green
            if (!it.hasNext()) {
                bw.write("#91ef99 0%, #91ef99 100%);\n");
            }

            Building selectedBuilding = Building.getBuildingById(selectedRoom.getRoomBuilding().getValue());
            StringConverter<Number> converter = getRangeSliderConverter();
            double opening = (double) converter.fromString(selectedBuilding.getOpeningTime().get());
            double closing = (double) converter.fromString(selectedBuilding.getClosingTime().get());

            Reservation res = AdminManageReservationViewController.currentSelectedReservation;

            // calculate and add green and red parts
            while (it.hasNext()) {
                Reservation r = it.next();
                double startTime = (double) converter.fromString(r.getReservationStartingTime().get());
                double endTime = (double) converter.fromString(r.getReservationEndingTime().get());
                double startPercentage = ((startTime - opening) / (closing - opening)) * 100.0;
                double endPercentage = ((endTime - opening) / (closing - opening)) * 100.0;
                // if reservation is the one that is being edited, give it a light blue color
                if (res != null && res.getReservationId().get() == r.getReservationId().get()) {
                    bw.write("#91ef99 " + startPercentage + "%, ");
                    bw.write("#70e5fa " + startPercentage + "%, ");
                    bw.write("#70e5fa " + endPercentage + "%, ");
                    bw.write("#91ef99 " + endPercentage + "%");
                    if (!it.hasNext()) {
                        bw.write(");\n");
                    } else {
                        bw.write(", ");
                    }
                    continue;
                }
                bw.write("#91ef99 " + startPercentage + "%, ");
                bw.write("#ffc0cb " + startPercentage + "%, ");
                bw.write("#ffc0cb " + endPercentage + "%, ");
                bw.write("#91ef99 " + endPercentage + "%");
                if (!it.hasNext()) {
                    bw.write(");\n");
                } else {
                    bw.write(", ");
                }
            }

            // last part of css (more configuration)
            bw.write("\t-fx-background-insets: 0 0 -1 0, 0, 1;\n"
                    + "\t-fx-background-radius: 0.25em, 0.25em, 0.166667em; /* 3 3 2 */\n"
                    + "\t-fx-padding: 0.25em; /* 3 */\n"
                    + "}\n\n" + ".range-bar {\n"
                    + "\t-fx-background-color: rgba(0,0,0,0.3);\n"
                    + "}");
            // flush and close writer
            bw.flush();
            bw.close();
            // remove current stylesheet
            timeslotSlider.getStylesheets().remove(getClass().getResource("/RangeSlider.css").toExternalForm());
            // add new stylesheet
            timeslotSlider.getStylesheets().add(getClass().getResource("/RangeSlider.css").toExternalForm());
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
            timeslotSlider.highValueProperty().addListener((observable, oldValue, newValue) ->
                    endTime.setText("End: " + converter.toString(newValue)));
            timeslotSlider.lowValueProperty().addListener((observable, oldValue, newValue) ->
                    startTime.setText("Start: " + converter.toString(newValue)));

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
     * .
     * This methods converts the date from the datepicker into database readable format
     *
     * @param formatter - DateTimeFormatter object which is used for formatting
     * @return - A string which contains the date in a proper format
     */
    public static StringConverter<LocalDate> getDateConverter(DateTimeFormatter formatter) {
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the converter of the room combobox to only show the names of the rooms.
     *
     * @param ol ObservableList of rooms
     */
    public void setRoomComboBoxConverter(ObservableList<Room> ol) {
        StringConverter<Room> converter = new StringConverter<Room>() {
            @Override
            public String toString(Room object) {
                if (object == null) {
                    return "";
                } else {
                    return object.getRoomName().get();
                }
            }

            @Override
            public Room fromString(String id) {
                return ol.stream()
                        .filter(x -> String.valueOf(x.getRoomId()).equals(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        room.setConverter(converter);
    }

    /**
     * Sets the converter of the user combobox to only show the names of the users.
     *
     * @param ol ObservableList of users
     */
    public void setUserComboBoxConverter(ObservableList<User> ol) {
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
            public User fromString(String id) {
                return ol.stream()
                        .filter(x -> String.valueOf(x.getUsername()).equals(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        username.setConverter(converter);
    }

    /**
     * .
     * Called when a  user is selected
     * The room combobox only shows the available rooms according to the user type
     * @param newUser is selected
     */
    public void usernameSelected(User newUser) {
        try {
            if (username.getValue() != null) {
                final ObservableList<Room> roomObservableList = Room.getRoomData();
                boolean isStudent = (this.username.getSelectionModel().getSelectedItem().getUserType().get() == 2);
                if (isStudent) {
                    List<Room> filteredRooms = roomObservableList.stream().filter(x -> !x.getTeacherOnly().get())
                            .collect(Collectors.toList());
                    roomObservableList.clear();
                    for (Room r : filteredRooms) {
                        roomObservableList.add(r);
                    }
                }
                room.setItems(roomObservableList);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Called when the OK button is clicked on the dialog box.
     * This causes the information input by the user to be stored in an object.
     *
     * @param event event that triggered this method
     */
    @FXML
    public void okClicked(ActionEvent event) {
        LocalDate dateSelected = date.getValue();
        if (isInputValid()) {
            emptyReservation();
            reservation.setUsername(username.getSelectionModel().getSelectedItem().getUsername().get());
            reservation.setRoom(room.getSelectionModel().getSelectedItem().getRoomId().get());
            reservation.setDate(dateSelected.toString());
            reservation.setStartingTime(startTime.getText().replace("Start: ", ""));
            reservation.setEndingTime(endTime.getText().replace("End: ", "").equals("24:00")
                    ? "23:59" : endTime.getText().replace("End: ", ""));

            this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.close();
        }
    }

    /**
     * Method that cancels the current edit/creation of a reservation.
     *
     * @param event that triggered this method
     */
    @FXML
    private void cancelClicked(ActionEvent event) {
        try {
            reservation = null;
            this.dialogStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            dialogStage.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    public boolean isInputValid() {

        User user = username.getValue();
        Room resRoom = room.getValue();
        LocalDate resDate = date.getValue();
        double currentStartValue = timeslotSlider.getLowValue();
        double currentEndValue = timeslotSlider.getHighValue();
        StringConverter<LocalDate> temp = getDateConverter();

        String errorMessage =  ReservationEditDialogLogic.isInputValid(
                user, resRoom, resDate, currentStartValue, currentEndValue, temp);

        // If all the fields are valid, then true is returned.
        if (errorMessage.equals("")) {
            return true;
        } else {
            // Show the error message.
            GeneralMethods.alertBox("Invalid Fields", "Please correct the invalid fields",
                    errorMessage, Alert.AlertType.ERROR);
            return false;
        }
    }


    /**
     * Method that checks if the chosen timeslot is free.
     *
     * @return true if the timeslot is free, false otherwise
     */
    private boolean checkTimeSlotValidity() {
        try {
            // get currently selected room
            Room selectedRoom = room.getSelectionModel().getSelectedItem();
            if (selectedRoom == null) {
                return false;
            }
            // get all reservations for the current room on the chosen date
            List<Reservation> roomReservations = Reservation.getRoomReservationsOnDate(
                    selectedRoom.getRoomId().get(),
                    date.getValue(), getDateConverter());

            // if something went wrong with the server communication return false
            if (roomReservations == null) {
                return false;
            }

            // get converter to convert date value to String format hh:mm
            StringConverter<Number> timeConverter = getRangeSliderConverter();

            // if there are no reservations the timeslot is valid
            if (roomReservations.size() == 0) {
                return true;
            }

            Reservation res = AdminManageReservationViewController.currentSelectedReservation;

            for (Reservation r : roomReservations) {
                // if reservation equals the one we are editing, don't consider it
                if (res != null) {
                    if (r.getReservationId().get() == res.getReservationId().get()) {
                        continue;
                    }
                }

                // get rangeslider values + reservation values
                double currentStartValue = timeslotSlider.getLowValue();
                double currentEndValue = timeslotSlider.getHighValue();
                double startValue = (double) timeConverter.fromString(r.getReservationStartingTime().get());
                double endValue = (double) timeConverter.fromString(r.getReservationEndingTime().get());

                // check if the values overlap
                if (!((currentStartValue <= startValue && currentEndValue <= startValue)
                        || (currentStartValue >= endValue && currentEndValue >= endValue))) {
                    return false;
                }

            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return false;
    }
}
