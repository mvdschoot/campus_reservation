package nl.tudelft.oopp.demo.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Food;
import nl.tudelft.oopp.demo.entities.Reservation;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.user.calendar.controller.CalendarPaneController;
import nl.tudelft.oopp.demo.user.logic.SearchViewLogic;
import nl.tudelft.oopp.demo.views.CalendarPaneView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.RentABikeView;
import nl.tudelft.oopp.demo.views.RoomView;

/**
 * Controller class for SearchView (JavaFX).
 */
public class SearchViewController implements Initializable, Runnable {

    public static Stage thisStage;
    public static ObservableList<Building> buildingList;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    /**
     * These are the FXML elements that inject some functionality into the application.
     */
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox cardHolder;
    @FXML
    private ComboBox<Building> buildingComboBox;
    @FXML
    private RadioButton yesCheckBoxTeacherOnly;
    @FXML
    private RadioButton noCheckBoxTeacherOnly;
    @FXML
    private RadioButton yesCheckBoxFood;
    @FXML
    private RadioButton noCheckBoxFood;
    @FXML
    private ComboBox<String> capacityComboBox;
    @FXML
    private ToggleGroup teacherOnly;
    @FXML
    private TextField searchBar;
    @FXML
    private ComboBox<String> bikesAvailable;
    @FXML
    private Button signOutButton;
    @FXML
    private Button rentABikeButton;

    private List<Building> buildings;
    private List<Room> roomList;
    private List<Room> rooms;

    // This is necessary due to the format of inserting items into a comboBox.
    private ObservableList<String> capacityList;
    private ObservableList<String> bikeList;
    private List<Integer> buildingsWithFood;

    private Map<Integer, HBox> roomCards;

    private boolean loaded;


    SimpleDateFormat sdf = new SimpleDateFormat("ss.SSS");


    /**
     * Default construct of searchView class.
     */
    public SearchViewController() {
    }

    /**
     * Method that gets called when loading the view.
     * Loads the buildings and rooms from the database
     * sets actions for when a filter is selected.
     * JavaFX standard.
     *
     * @param location  is passed
     * @param resources is passed
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loaded = false;
            Thread t1 = new Thread(this);
            t1.start();
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            rentABikeButton.getStyleClass().clear();
            rentABikeButton.getStyleClass().add("bike-button");

            // assign lists to the initialized ObservableLists
            // This is necessary due to the format of inserting items into a comboBox.
            final ObservableList<String> capacityList = FXCollections.observableArrayList();
            final ObservableList<String> bikeList = FXCollections.observableArrayList();

            // the comboBox only shows 6 rows (more => scroll)
            buildingComboBox.setVisibleRowCount(6);

            // configure the date picker
            datePicker.setConverter(getDatePickerStringConverter());
            datePicker.setDayCellFactory(getDayCellFactory());

            // assign values to the observable lists
            capacityList.addAll("1-5", "5-10", "10-20", "20+");
            bikeList.addAll("1+", "5+", "10+", "20+");

            // populating the choicebox
            capacityComboBox.setItems(capacityList);
            bikesAvailable.setItems(bikeList);

            // get all rooms from database
            roomList = Room.getRoomData();
            if (roomList != null) {
                rooms = new ArrayList<Room>(roomList);
            }

            roomCards = new HashMap<Integer, HBox>();

            // create a 'card' showing some information of the room, for every room
            for (Room r : roomList) {
                HBox roomCard = createRoomCard(r);
                roomCards.put(r.getRoomId().get(), roomCard);
                cardHolder.getChildren().add(roomCard);
            }

            // create a 'card' showing some information of the room, for every room
            while (!loaded) {
                synchronized (this) {
                    this.wait();
                }
            }
            getCardsShown(roomList);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }

        // if a new filter is applied or an filter is removed filter again and load the cards again
        buildingComboBox.setOnAction(event -> {
            try {
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        capacityComboBox.setOnAction(event -> {
            try {
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        bikesAvailable.setOnAction(event -> {
            try {
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        yesCheckBoxTeacherOnly.setOnAction(event -> {
            try {
                yesCheckBoxTeacherOnly.setSelected(true);
                noCheckBoxTeacherOnly.setSelected(false);
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        noCheckBoxTeacherOnly.setOnAction(event -> {
            try {
                yesCheckBoxTeacherOnly.setSelected(false);
                noCheckBoxTeacherOnly.setSelected(true);
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        yesCheckBoxFood.setOnAction(event -> {
            try {
                yesCheckBoxFood.setSelected(true);
                noCheckBoxFood.setSelected(false);
                loadCards();

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        noCheckBoxFood.setOnAction(event -> {
            try {
                yesCheckBoxFood.setSelected(false);
                noCheckBoxFood.setSelected(true);
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a key is released only the searchbar gets filtered again.
        // the rest stays the same and the list of the rooms of the other filters is used again.
        searchBar.setOnKeyReleased(event -> {
            try {
                searchbarChanges();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });

        // if a new filter is applied or an filter is removed filter again and load the cards again
        datePicker.setOnAction(event -> {
            try {
                loadCards();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
            }
        });
    }

    /**
     * Filters the rooms according to the filters selected.
     * Makes a call to getCardsShown() to show the cards on the view.
     *
     * @throws UnsupportedEncodingException when encoding fails.
     */
    public void loadCards() throws UnsupportedEncodingException {
        //load all rooms back in the roomlist to filter again
        roomList.clear();
        roomList.addAll(rooms);

        //Check if there are any filters selected and if so filter the roomlist
        if (buildingComboBox.getValue() != null) {
            int building = buildingComboBox.getValue().getBuildingId().getValue();
            roomList = SearchViewLogic.filterRoomByBuilding(roomList, building);
        }

        // if the checkbox is selected it filters according to the checkbox.
        if (yesCheckBoxTeacherOnly.isSelected()) {
            roomList = SearchViewLogic.filterRoomByTeacherOnly(roomList, true);
        }

        // if the checkbox is selected it filters according to the checkbox.
        if (noCheckBoxTeacherOnly.isSelected()) {
            roomList = SearchViewLogic.filterRoomByTeacherOnly(roomList, false);
        }
        if (yesCheckBoxFood.isSelected()) {
            roomList = SearchViewLogic.filterByFood(roomList, buildingsWithFood);
        }

        // if the combobox is selected on a value it filters for that value.
        if (capacityComboBox.getValue() != null) {
            String capacity = capacityComboBox.getValue();

            roomList = SearchViewLogic.filterRoomByCapacity(roomList, capacity);
        }

        // if the combobox is selected on a value it filters for that value.
        if (bikesAvailable.getValue() != null) {
            String bikes = bikesAvailable.getValue();

            roomList = SearchViewLogic.filterByBike(roomList, buildings, bikes);
        }

        // if a date is selected it filters out the rooms that are fully booked for that day.
        if (datePicker.getValue() != null) {
            String date = datePicker.getValue().toString();
            ObservableList<Reservation> reservations = Reservation.getAllReservations();
            SearchViewLogic.filterRoomsByDate(roomList, date, reservations, buildings);
        }

        // value of the searchbar is put in searchBarInput
        // and is filtered on building name and room name.
        // the list is put in a new List
        // so if a other key is pressed the other filters don't have to be applied again.
        String searchBarInput = searchBar.getText();
        List<Room> roomsToShow = roomList;
        if (!searchBarInput.equals("")) {
            roomsToShow = SearchViewLogic.filterBySearch(roomList, searchBarInput, buildings);
        }

        //Load the cards that need to be shown
        getCardsShown(roomsToShow);

    }

    /**
     * Clears all the cards currently shown in the view and shows the cards that are filtered.
     *
     * @param roomList list of rooms that are going to be shown.
     */
    public void getCardsShown(List<Room> roomList) {

        //Removes cards that are now in the view
        cardHolder.getChildren().clear();

        for (Room r : roomList) {
            HBox roomCard = roomCards.get(r.getRoomId().get());
            cardHolder.getChildren().add(roomCard);
        }


    }

    /**
     * filters the rooms on the searchbar input. It searches for matches in the building name and room name.
     * Makes a call to SearchViewLogic.FilterBySearch.
     *
     * @throws UnsupportedEncodingException when encoding fails.
     */
    public void searchbarChanges() throws UnsupportedEncodingException {
        // filters the rooms on the searchbar input. It searches for matches in the building name and room name.
        String searchBarInput = searchBar.getText();
        if (searchBarInput == "") {
            loadCards();
        } else {
            List<Room> roomsToShow = SearchViewLogic.filterBySearch(roomList, searchBarInput, buildings);
            //Load the cards that need to be shown
            getCardsShown(roomsToShow);
        }
    }


    /**
     * Create cellFactory for the datePicker that disables all days before today and weekend days.
     * It also marks them red to make sure the user understands why they are disabled.
     *
     * @return a CallBack object used to set the dayCellFactory for the datePicker
     */
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        try {
            final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {

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
            return dayCellFactory;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Creates a StringConverter that converts the selected value to a usable Date (in String format).
     *
     * @return a StringConverter object
     */
    private StringConverter<LocalDate> getDatePickerStringConverter() {
        try {
            return new StringConverter<LocalDate>() {
                // set the wanted pattern (format)
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

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
     * Create a StringConverter that shows the name of the building for each building in the comboBox.
     *
     * @return StringConverter
     */
    private StringConverter<Building> getBuildingComboBoxConverter() {
        try {
            StringConverter<Building> converter = new StringConverter<Building>() {
                @Override
                public String toString(Building object) {
                    if (object == null) {
                        return "";
                    }
                    return object.getBuildingName().get();
                }

                @Override
                public Building fromString(String id) {
                    return buildingList.stream()
                            .filter(x -> String.valueOf(x.getBuildingId()).equals(id))
                            .collect(Collectors.toList()).get(0);
                }
            };
            return converter;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }


    /**
     * When a card gets clicked, the RoomView gets loaded with all the corresponding room information.
     *
     * @param event MouseEvent
     */
    public void cardClicked(MouseEvent event) {
        try {
            // get current Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // get the card that was clicked
            HBox selectedCard = (HBox) event.getSource();

            // get the VBox that contains the 'invisible' room id
            VBox cardInfo = (VBox) selectedCard.getChildren().get(1);

            // get room id from that VBox and parse to int

            // set the currentRoomID such that the RoomView controller knows which room to show information from
            RoomViewController.currentRoomId = Integer.parseInt(((Text) cardInfo.getChildren().get(0)).getText());

            // load RoomView
            RoomView rv = new RoomView();
            rv.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Clears all the filters and sets them back to 'empty'.
     */
    @FXML
    private void clearFiltersClicked() {
        try {
            // clear every filter and reload the cards
            datePicker.setValue(null);
            buildingComboBox.setValue(null);
            yesCheckBoxFood.setSelected(false);
            noCheckBoxFood.setSelected(false);
            yesCheckBoxTeacherOnly.setSelected(false);
            noCheckBoxTeacherOnly.setSelected(false);
            searchBar.setText("");
            capacityComboBox.setValue(null);
            bikesAvailable.setValue(null);
            loadCards();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Loads the calendar view with all the booking history of the current user.
     *
     * @param event event that triggered this method
     */
    @FXML
    public void bookingHistoryClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            CalendarPaneController.thisStage = stage;
            CalendarPaneView cpv = new CalendarPaneView();
            cpv.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles the onclick of signOut Button.
     * Redirects the user back to the login page.
     *
     * @param event event that triggered this method
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) {
        try {
            // get current stage and load log in view
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            LoginView loginView = new LoginView();
            loginView.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Handles the onclick of rentABike Button.
     * Redirects the user to the page to rent a bike.
     *
     * @param event event that triggered this method
     */
    @FXML
    public void rentABikeClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            RentABikeView rabv = new RentABikeView();
            rabv.start(stage);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Creates a new 'card' (HBox) which contains some information about the room.
     *
     * @param r The Room that we have to show information from
     * @return HBox which is the final 'card'
     */
    public HBox createRoomCard(Room r) {
        try {
            // load the 'skeleton of a new card
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("/RoomCard.fxml");
            loader.setLocation(xmlUrl);

            HBox newCard = loader.load();

            Image roomImage = null;
            try {
                // get the room image
                roomImage = new Image("images/" + r.getRoomPhoto().get());
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString());
                // load placeholder instead
                roomImage = new Image("images/placeholder.png");
            }


            // use lookup to retrieve Nodes by their id and set their content
            ImageView image = ((ImageView) newCard.lookup("#image"));
            image.setImage(roomImage);
            // set the correct width
            image.setFitWidth(300);

            Building b = buildingList.stream()
                    .filter(x -> x.getBuildingId().get() == r.getRoomBuilding().get())
                    .collect(Collectors.toList()).get(0);

            ((Text) newCard.lookup("#idText")).setText(String.valueOf(r.getRoomId().get()));
            ((Text) newCard.lookup("#titleText")).setText(r.getRoomName().get());
            ((Text) newCard.lookup("#buildingText")).setText("Building: " + b.getBuildingName().get());
            ((Text) newCard.lookup("#capacityText")).setText("Capacity: " + r.getRoomCapacity().get());
            ((Text) newCard.lookup("#descriptionText")).setText("Description: " + r.getRoomDescription().get());

            // set mouse clicked event on card (to redirect to room view)
            newCard.setOnMouseClicked(event -> {
                try {
                    cardClicked(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // set space between the cards
            VBox.setMargin(newCard, new Insets(0, 0, 70, 0));

            return newCard;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    @Override
    public void run() {
        //loads all the building from the database.
        buildings = Building.getBuildingData();
        buildingList = (ObservableList<Building>) buildings;

        //gets all the buildings that have food that you can order.
        buildingsWithFood = new ArrayList<Integer>();
        for (int i = 0; i != buildings.size(); i++) {
            int buildingId = buildings.get(i).getBuildingId().getValue();
            if (!Food.getFoodByBuildingId(buildingId).isEmpty()) {
                buildingsWithFood.add(buildings.get(i).getBuildingId().getValue());
            }
        }
        //sets the values for the combobox.
        buildingComboBox.setItems(buildingList);
        buildingComboBox.setConverter(getBuildingComboBoxConverter());

        loaded = true;
        synchronized (this) {
            this.notifyAll();
        }
    }
}
