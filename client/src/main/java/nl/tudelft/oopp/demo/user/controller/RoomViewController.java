package nl.tudelft.oopp.demo.user.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;

import nl.tudelft.oopp.demo.admin.controller.AdminManageReservationViewController;
import nl.tudelft.oopp.demo.communication.FoodServerCommunication;
import nl.tudelft.oopp.demo.communication.ReservationServerCommunication;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.user.CurrentUserManager;
import nl.tudelft.oopp.demo.user.logic.RoomViewLogic;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.ReservationConfirmationView;
import nl.tudelft.oopp.demo.views.SearchView;

import org.controlsfx.control.RangeSlider;


/**
 * Controller class for the Room view (JavaFX).
 */
public class RoomViewController implements Initializable {

    public static int currentRoomId;
    // current Stage
    public static Stage thisStage;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    // current room to show info about
    private static Room currentRoom;
    private final String pathSeparator = File.separator;
    /**
     * These are the FXML elements that inject some functionality into the application.
     */
    @FXML
    private Text name;
    @FXML
    private Text capacity;
    @FXML
    private Text building;
    @FXML
    private Text teacherOnly;
    @FXML
    private Text type;
    @FXML
    private ImageView image;
    @FXML
    private Text description;
    @FXML
    private ComboBox<Food> foodChoice;
    @FXML
    private Button bookButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane grid;
    @FXML
    private VBox reservationVbox;
    @FXML
    private Text startTime;
    @FXML
    private Text endTime;
    @FXML
    private Text dateError;
    @FXML
    private Text timeslotError;
    @FXML
    private Text teacherOnlyError;
    @FXML
    private Button signOutButton;
    // double thumb slider
    private RangeSlider timeSlotSlider;
    private List<Food> selectedFoodList;
    private Map<Food, Integer> foodMap;

    /**
     * Method that gets called before everything (mostly to initialize nodes etc.).
     * JavaFX standard.
     *
     * @param location  is passed
     * @param resources is passed
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            // initialize the list with all the selected foods
            selectedFoodList = new ArrayList<>();

            grid.setMinWidth(reservationVbox.getWidth());

            // initialize the Room object that contains the info about this room
            currentRoom = Room.getRoomById(currentRoomId);

            // configure the string converters and custom properties (like disabling some dates in the datePicker)
            configureDatePicker();
            configureRangeSlider();

            setTimeSlotSlider();

            // make sure errors are not visible
            hideErrors();


            // if user is a student and the room is teacher only => disable all components
            if (CurrentUserManager.getType() == 2 && currentRoom.getTeacherOnly().get()) {
                teacherOnlyError.setVisible(true);
                disableReservationComponents();
            } else {
                teacherOnlyError.setVisible(false);
            }

            // adjust layout
            changeWidthConstraints(thisStage.getWidth());
            image.setFitHeight(100000);

            // listener that adjusts layout when width of stage changes
            thisStage.widthProperty().addListener((obs, oldVal, newVal) -> changeWidthConstraints(newVal));


            ObservableList<Food> foodList = Food.getFoodByBuildingId(
                    Building.getBuildingById(currentRoom.getRoomBuilding().get())
                            .getBuildingId().get());
            foodChoice.setConverter(getFoodChoiceConverter(foodList));
            foodChoice.setItems(foodList);
            foodChoice.setButtonCell(getButtonCell());
            setFoodChoiceListeners();

            // set text and image info about the room
            configureRoomInfoTexts();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Method that sets all the room info in the text and image fields.
     */
    private void configureRoomInfoTexts() {
        try {
            // sets all the room info text fields (+ image)
            name.setText("Name: " + currentRoom.getRoomName().get());
            capacity.setText("Capacity: " + currentRoom.getRoomCapacity().get());
            building.setText("Building: " + Building.getBuildingById(currentRoom.getRoomBuilding().get())
                    .getBuildingName().get());
            teacherOnly.setText("Teachers only: " + (currentRoom.getTeacherOnly().get() ? "yes" : "no"));
            type.setText("Type: " + currentRoom.getRoomType().get());
            description.setText("Description:\n" + currentRoom.getRoomDescription().get());
            configureRoomImage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Makes sure that when the user has selected an item in the food combobox, the prompt text resets.
     *
     * @return ListCell that correctly updates the value.
     */
    private ListCell<Food> getButtonCell() {
        try {
            return new ListCell<Food>() {
                @Override
                protected void updateItem(Food item, boolean btl) {
                    super.updateItem(item, btl);
                    if (item != null) {
                        setText(item.getFoodName().get());
                    } else {
                        // if item is null, show prompt text
                        setText("Food choice:");
                    }
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Method that sets the listeners for the food combobox.
     */
    private void setFoodChoiceListeners() {
        // when new food gets chosen, the selection gets cleared
        foodChoice.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                createNewFoodText(newValue);
                // has to be done in separate runnable because otherwise
                // it would interfere with the listener.
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        foodChoice.getSelectionModel().clearSelection();
                    }
                });
            }
        }));
    }

    /**
     * Method that creates a new food section when a new food is selected.
     *
     * @param food food that got selected
     */
    private void createNewFoodText(Food food) {
        // get the amount of rows int he grid
        int rowCount = grid.getRowCount();

        // get name of the selected food
        String foodName = food.getFoodName().get();

        for (int i = 0; i < grid.getChildren().size(); i++) {
            // get child
            GridPane currentGrid = (GridPane) grid.getChildren().get(i);
            // if foods are the same, increment the food quantity
            if (getFoodNameByColumn(currentGrid).getText().equals(foodName)) {
                Text quantityText = getFoodQuantityByColumn(currentGrid);
                int intQuantity = Character.getNumericValue(quantityText.getText().charAt(0));
                quantityText.setText(intQuantity + 1 + "x");
                return;
            }
        }

        // if 4 foods have been chosen you cannot select more
        if (rowCount >= 4) {
            Alert alert = GeneralMethods.createAlert("Food restrictions",
                    "You are not allowed to order more than 4 dishes per reservation",
                    thisStage,
                    Alert.AlertType.WARNING);
            alert.showAndWait();
            return;
        }

        // add the food to the selected food list
        selectedFoodList.add(food);

        // make the new food section
        GridPane miniGrid = new GridPane();
        final Text foodText = new Text(food.getFoodName().get());
        final Text quantity = new Text("1x");
        final Text foodPrice = new Text(GeneralMethods.formatPriceString(food.getFoodPrice().get()));
        Button remove = new Button("X");
        // when button clicked, remove the food section
        remove.setOnAction(e -> {
            selectedFoodList.remove(food);
            removeRowFromGrid(miniGrid);
        });

        miniGrid.setAlignment(Pos.CENTER);
        // correctly align the values in each column
        ColumnConstraints constraints1 = new ColumnConstraints(10, 100, 200,
                Priority.SOMETIMES, HPos.LEFT, true);
        ColumnConstraints constraints2 = new ColumnConstraints(10, 100, 200,
                Priority.SOMETIMES, HPos.CENTER, true);
        ColumnConstraints constraints3 = new ColumnConstraints(10, 100, 200,
                Priority.SOMETIMES, HPos.RIGHT, true);

        // set the column constraints for the 4 columns
        miniGrid.getColumnConstraints().addAll(constraints1, constraints2, constraints3, constraints3);

        // add all the components to the food section
        miniGrid.add(quantity, 0, 0);
        miniGrid.add(foodText, 1, 0);
        miniGrid.add(foodPrice, 2, 0);
        miniGrid.add(remove, 3, 0);

        // add the food section to the big grid
        grid.addRow(rowCount, miniGrid);
    }

    /**
     * Method that removes a food section from the big grid.
     *
     * @param miniGrid the food section to remove
     */
    private void removeRowFromGrid(GridPane miniGrid) {
        // get all children
        List<Node> children = new ArrayList<>(grid.getChildren());
        // remove all children
        grid.getChildren().clear();

        int currentRow = 0;
        for (Node child : children) {
            // get food section
            GridPane childGrid = (GridPane) child;
            // if food section is same as the one that should get deleted, don't add it back
            if (childGrid != miniGrid) {
                // add back the food section
                grid.add(childGrid, 0, currentRow++);
            }
        }
    }

    /**
     * Method that gets the name of the food from a food section.
     *
     * @param gridPane the food section
     * @return Text the food name
     */
    private Text getFoodNameByColumn(GridPane gridPane) {
        Node name = null;
        // get the children
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            // get component at column index 1
            if (GridPane.getColumnIndex(node) == 1) {
                name = node;
                break;
            }
        }
        return (Text) name;
    }

    /**
     * Method that gets the food quantity text from a food section.
     *
     * @param gridPane food section
     * @return Text the food quantity
     */
    private Text getFoodQuantityByColumn(GridPane gridPane) {
        Node quantity = null;
        // get children
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            // get component at column index 0
            if (GridPane.getColumnIndex(node) == 0) {
                quantity = node;
                break;
            }
        }
        return (Text) quantity;
    }

    /**
     * Constructs a converter for the food ComboBox to only show the name of the foods.
     *
     * @param foodList list of all foods
     * @return a StringConverter which converts food object to the food name
     */
    private StringConverter<Food> getFoodChoiceConverter(ObservableList<Food> foodList) {
        try {
            return new StringConverter<Food>() {
                @Override
                public String toString(Food object) {
                    return RoomViewLogic.toStringFood(object);
                }

                @Override
                public Food fromString(String string) {
                    return RoomViewLogic.fromStringFood(string, foodList);
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Method that configures the ImageView which contains the image of the room.
     */
    private void configureRoomImage() {
        try {
            File resourceImage = new File("client/src/main/resources/images/"
                    + currentRoom.getRoomPhoto().get());
            // get path to the room image
            String path = resourceImage.getAbsolutePath();

            // get the room image
            BufferedImage roomPhoto = ImageIO.read(resourceImage);

            // set the image in the ImageView
            image.setImage(new Image("file:" + path));

            // crop the image to show in proportion with the standard room picture size
            Rectangle2D viewPort = new Rectangle2D(0, 0, roomPhoto.getWidth(),
                    roomPhoto.getWidth() * (336.9 / 503.0));
            image.setViewport(viewPort);

        } catch (Exception e) {
            try {
                // set the default image in the ImageView
                image.setImage(new Image(getClass().getResource("/images/placeholder.png")
                        .toExternalForm()));
                // make sure the image is correctly resized in proportion to the current stage width
                changeWidthConstraints(thisStage.getWidth());
                logger.log(Level.SEVERE, e.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Method that disables all the components needed to book a reservation.
     */
    private void disableReservationComponents() {
        // disbale all the components that are used to book a reservation
        try {
            bookButton.setDisable(true);
            foodChoice.setDisable(true);
            timeSlotSlider.setDisable(true);
            datePicker.setDisable(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Methods that hides all the error messages.
     */
    private void hideErrors() {
        try {
            // hide each error message
            dateError.setVisible(false);
            timeslotError.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * This method changes the width of some JavaFX nodes based on the width of the stage.
     * It's the callback method for the stage width listener.
     *
     * @param newWidth The new width of the current stage
     */
    private void changeWidthConstraints(Number newWidth) {
        try {
            // set the width of some nodes based on the calculated ratio between their width and the stages width
            image.setFitWidth((newWidth.doubleValue() - 188) / 1.41550696);
            reservationVbox.setPrefWidth((newWidth.doubleValue() - 188) / 3.3969);
            timeSlotSlider.setMaxWidth((newWidth.doubleValue() - 188) / 5);
            description.setWrappingWidth((newWidth.doubleValue() - 188) / 1.564835);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
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
            // set value change listener to adjust css for available timeslots
            datePicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
                try {
                    configureCss();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.toString());
                }
            }));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Create cellFactory for the datePicker that disables all days before today and weekend days.
     * It also marks them red to make sure the user understands why they are disabled.
     *
     * @return a CallBack object used to set the dayCellFactory for the datePicker in
     * {@link #configureDatePicker()}
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

            // configure css of rangeslider to show user what timeslots are free
            configureCss();

            // initialize the Text objects with the current values of the thumbs
            assert converter != null;
            startTime.setText(converter.toString(timeSlotSlider.getLowValue()));
            endTime.setText(converter.toString(timeSlotSlider.getHighValue()));

            // inject the RangeSlider in the JavaFX layout
            reservationVbox.getChildren().add(2, timeSlotSlider);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * .
     * Configure (in CSS) the colors of the track of the range slider to show in green the available timeslots
     * and in red the rest
     */
    private void configureCss() {
        try {
            // get css file and delete its content to fill it again
            File css = new File(getClass().getResource("/RangeSlider.css")
                    .getPath().replace("/", pathSeparator));
            css.delete();
            css.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(css));
            // if no date picked, make slider track white
            if (datePicker.getValue() == null) {
                GeneralMethods.setSliderDefaultCss(timeSlotSlider, bw,
                        getClass().getResource("/RangeSlider.css").toExternalForm());
                return;
            }
            // get reservations for this room on the selected date
            List<Reservation> reservations = Reservation.getRoomReservationsOnDate(currentRoomId,
                    datePicker.getValue(), getDatePickerConverter());

            if (reservations == null) {
                return;
            }

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

            Building selectedBuilding = Building.getBuildingById(currentRoom.getRoomBuilding().get());
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
            timeSlotSlider.getStylesheets().remove(getClass().getResource("/RangeSlider.css")
                    .toExternalForm());
            // add new stylesheet
            timeSlotSlider.getStylesheets().add(getClass().getResource("/RangeSlider.css")
                    .toExternalForm());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private void setTimeSlotSlider() {
        try {
            Building selectedBuilding = Building.getBuildingById(currentRoom.getRoomBuilding().get());
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


    /**
     * Configure the rangeSlider listeners.
     * The listeners make sure that the user jumps intervals of
     * 30 minutes and sets the texts with the correct value.
     *
     * @param converter String converter that is created in {@link #getRangeSliderConverter()}
     */
    private void configureRangeSliderListeners(StringConverter<Number> converter) {
        try {
            // listeners to adjust start and end Text objects when thumbs get moved
            timeSlotSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
                endTime.setText(converter.toString(newValue));
            });
            timeSlotSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
                startTime.setText(converter.toString(newValue));
            });

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
     * Method that checks if the chosen timeslot is free.
     *
     * @return true if the timeslot is free, false otherwise
     */
    private boolean checkTimeSlotValidity() {
        // get all reservations for the current room on the chosen date
        List<Reservation> roomReservations = Reservation.getRoomReservationsOnDate(currentRoomId,
                datePicker.getValue(), getDatePickerConverter());

        // get converter to convert date value to String format hh:mm
        StringConverter<Number> timeConverter = getRangeSliderConverter();

        // if there are no reservations the timeslot is valid
        if (roomReservations.size() == 0) {
            return true;
        }

        for (Reservation r : roomReservations) {
            // get rangeslider values + reservation values
            double currentStartValue = timeSlotSlider.getLowValue();
            double currentEndValue = timeSlotSlider.getHighValue();
            double startValue = (double) timeConverter.fromString(r.getReservationStartingTime().get());
            double endValue = (double) timeConverter.fromString(r.getReservationEndingTime().get());

            // check if the values overlap
            if (!RoomViewLogic.checkTimeSlotValidity(currentStartValue, currentEndValue, startValue, endValue)) {
                return false;
            }

        }
        return true;
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
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                {
                    // set placeholder text for the datePicker
                    datePicker.setPromptText(pattern.toLowerCase());
                }

                @Override
                public String toString(LocalDate date) {
                    return RoomViewLogic.toStringDate(date, dateFormatter);
                }

                @Override
                public LocalDate fromString(String string) {
                    return RoomViewLogic.fromStringDate(string, dateFormatter);
                }
            };
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
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
     * Method that executes when the backButton is clicked. It returns to the searchview.
     */
    @FXML
    private void backButtonClicked() {
        try {
            // load the searchview
            SearchView sv = new SearchView();
            sv.start(thisStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    // TODO: add try catch everywhere

    /**
     * Method that executes when book button is clicked. It checks if fields are correctly filled.
     *
     * @param event ActionEvent
     */
    @FXML
    private void bookClicked(ActionEvent event) {
        try {
            String selectedDate;
            String selectedStartTime;
            String selectedEndTime;
            String selectedFood;

            // input is valid, assign corresponding values
            if (isInputValid()) {
                selectedDate = Objects.requireNonNull(getDatePickerConverter()).toString(datePicker.getValue());
                selectedStartTime = Objects.requireNonNull(
                        getRangeSliderConverter()).toString(timeSlotSlider.getLowValue());
                selectedEndTime = getRangeSliderConverter().toString(timeSlotSlider.getHighValue());

                // if user confirms booking, reservations is sent to server
                if (confirmBooking(selectedDate, selectedStartTime, selectedEndTime)) {
                    if (ReservationServerCommunication.createReservation(CurrentUserManager.getUsername(),
                            currentRoomId, selectedDate, selectedStartTime, selectedEndTime.contains("24")
                                    ? "23:59" : selectedEndTime)) {
                        // get id of the new reservation (last inserted reservation)
                        int currentId = Integer.parseInt(ReservationServerCommunication.getCurrentId()) - 1;

                        // link food to reservation
                        for (Food f : selectedFoodList) {
                            FoodServerCommunication.addFoodToReservation(f.getFoodId().get(),
                                    currentId, foodMap.get(f));
                        }

                        // create confirmation Alert
                        Alert alert = GeneralMethods.createAlert("Room booked",
                                "You successfully booked this room!",
                                ((Node) event.getSource()).getScene().getWindow(), Alert.AlertType.CONFIRMATION);
                        alert.showAndWait();
                        SearchView sv = new SearchView();
                        sv.start(thisStage);
                    } else {
                        // Throw exception with message that something went wrong
                        throw new Exception("reservation booking went wrong");
                    }
                }
            } else {
                // create error Alert
                Alert alert = GeneralMethods.createAlert("fields incomplete",
                        "Please fill in all the fields",
                        ((Node) event.getSource()).getScene().getWindow(), Alert.AlertType.ERROR);
                assert alert != null;
                alert.showAndWait();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            // create error Alert
            Alert alert = GeneralMethods.createAlert("Something went wrong",
                    "Sorry, something went wrong on our end. We're fixing it now!",
                    ((Node) event.getSource()).getScene().getWindow(), Alert.AlertType.ERROR);
            assert alert != null;
            alert.showAndWait();
        }
    }

    /**
     * Loads a little pop up stage that shows all information about the booking and asks for confirmation.
     *
     * @param date      day of the booking
     * @param startTime starting time of the booking
     * @param endTime   ending time of the booking
     * @return true if the user confirms, false otherwise
     */
    private boolean confirmBooking(String date, String startTime, String endTime) {
        try {
            if (selectedFoodList.size() <= 0) {
                ReservationConfirmationViewController.foodChosen = false;
            } else {
                ReservationConfirmationViewController.foodChosen = true;
            }
            // set all fields to the current reservation details
            ReservationConfirmationViewController.room = currentRoom;
            ReservationConfirmationViewController.date = date;
            ReservationConfirmationViewController.startTime = startTime;
            ReservationConfirmationViewController.endTime = endTime;
            ReservationConfirmationViewController.foodList = selectedFoodList;
            foodMap = new HashMap<>();
            for (int i = 0; i < selectedFoodList.size(); i++) {
                GridPane miniGrid = (GridPane) grid.getChildren().get(i);
                int quantity = Integer.parseInt(((Text) miniGrid.getChildren().get(0)).getText().replace("x", ""));
                foodMap.put(selectedFoodList.get(i), quantity);
            }
            ReservationConfirmationViewController.foodMap = foodMap;
            // load confirmation pop up stage
            ReservationConfirmationView rcv = new ReservationConfirmationView();
            rcv.start(thisStage);
            // return true if confirmed, false otherwise
            return ReservationConfirmationViewController.confirmed;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return false;
    }

    /**
     * Validator.
     * Checks if fields are correctly filled and shows errors and warnings if
     * the user forgot some fields.
     *
     * @return true if everything is filled in correctly, false otherwise
     */
    public boolean isInputValid() {
        try {
            // true if there are errors, false otherwise
            boolean errors = false;

            // clear error messages
            dateError.setVisible(false);
            timeslotError.setVisible(false);

            // set error messages if necessary
            if (datePicker.getValue() == null) {
                dateError.setVisible(true);
                errors = true;
            }
            if (!checkTimeSlotValidity() || timeSlotSlider.getLowValue() == timeSlotSlider.getHighValue()) {
                timeslotError.setVisible(true);
                errors = true;
            }
            // return true if no errors where triggered
            return !errors;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return false;
    }

    /**
     * Redirects the user back to the login page.
     *
     * @param event ActionEvent
     */
    @FXML
    private void signOutButtonClicked(ActionEvent event) {
        try {
            //loads a new login page
            LoginView loginView = new LoginView();
            loginView.start(thisStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }
}