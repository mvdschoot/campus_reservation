package nl.tudelft.oopp.demo.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import nl.tudelft.oopp.demo.admin.logic.AdminLogic;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.general.GeneralMethods;
import nl.tudelft.oopp.demo.views.AdminHomePageView;
import nl.tudelft.oopp.demo.views.LoginView;
import nl.tudelft.oopp.demo.views.RoomEditDialogView;
import nl.tudelft.oopp.demo.views.RoomNewDialogView;

/**
 * Class that controls the admin view showing all the rooms.
 * The admin can create, edit and delete rooms
 */
public class AdminManageRoomViewController {

    public static Room currentSelectedRoom;
    private static Logger logger = Logger.getLogger("GlobalLogger");
    @FXML
    private TableView<Room> roomTable;
    @FXML
    private TableColumn<Room, Number> roomIdColumn;
    @FXML
    private TableColumn<Room, String> roomNameColumn;
    @FXML
    private TableColumn<Room, Number> roomBuildingColumn;
    @FXML
    private TableColumn<Room, String> roomOnlyTeachersColumn;
    @FXML
    private TableColumn<Room, Number> roomCapacityBuilding;
    @FXML
    private TableColumn<Room, String> roomPhotoColumn;
    @FXML
    private TableColumn<Room, String> roomDescriptionColumn;
    @FXML
    private TableColumn<Room, String> roomTypeColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button signOutButton;

    /**
     * Default constructor for JavaFX.
     */
    public AdminManageRoomViewController() {
    }

    /**
     * Fills the TableView with the correct values for every room.
     * It also initializes a Tooltip on the image name cells. When the admin hovers on the name, he gets
     * a preview of the image.
     */
    @FXML
    private void initialize() {
        try {
            backButton.getStyleClass().clear();
            backButton.getStyleClass().add("back-button");
            signOutButton.getStyleClass().clear();
            signOutButton.getStyleClass().add("signout-button");
            // Initialize the room table with the eight columns.
            roomIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getRoomId().get()));
            roomNameColumn.setCellValueFactory(cellData -> cellData.getValue().getRoomName());
            roomBuildingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getRoomBuilding().get()));
            // To align the text in this column in a centralized manner; looks better
            roomBuildingColumn.setStyle("-fx-alignment: CENTER");

            roomOnlyTeachersColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                    cell.getValue().getTeacherOnly().get() ? "yes" : "no"));
            // To align the text in this column in a centralized manner; looks better
            roomOnlyTeachersColumn.setStyle("-fx-alignment: CENTER");

            roomCapacityBuilding.setCellValueFactory(cell -> new SimpleIntegerProperty(
                    cell.getValue().getRoomCapacity().get()));
            // To align the text in this column in a centralized manner; looks better
            roomCapacityBuilding.setStyle("-fx-alignment: CENTER");

            roomPhotoColumn.setCellValueFactory(cell -> cell.getValue().getRoomPhoto());
            roomDescriptionColumn.setCellValueFactory(cell -> cell.getValue().getRoomDescription());
            roomTypeColumn.setCellValueFactory(cell -> cell.getValue().getRoomType());

            // sets a Tooltip such that when the admin hovers over the image name,
            // he can get a preview of the image
            roomPhotoColumn.setCellFactory(tc -> {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                        if (!empty) {
                            Tooltip tooltip = getPhotoToolTip(this.getItem());
                            this.setTooltip(tooltip);
                        }
                    }
                };
            });

            roomTable.setItems(Room.getRoomData());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Constructs a Tooltip with the image of the cell's room as its graphic.
     *
     * @param fileName name of image to find in resources
     * @return a Tooltip with the image set
     */
    private Tooltip getPhotoToolTip(String fileName) {
        ImageView image = new ImageView();
        final Tooltip tooltip = new Tooltip();

        // get the image file from resources
        File resourceImage = new File("client/src/main/resources/images/" + fileName);
        String path = resourceImage.getAbsolutePath();
        Image roomPhoto = new Image("file:" + path);
        image.setImage(roomPhoto);
        image.setPreserveRatio(true);
        // set the width to be the same as the current width of the column
        image.setFitWidth(roomPhotoColumn.getWidth());

        // set the image
        tooltip.setGraphic(image);
        return tooltip;
    }

    /**
     * Re-initializes the complete view (to immediately show the admin when a room is created or edited).
     */
    public void refresh() {
        initialize();
    }

    /**
     * Gets the currently selected room in the TableView.
     *
     * @return Room object of the selected room
     */
    public Room getSelectedRoom() {
        return AdminLogic.getSelectedRoom(roomTable);
    }

    /**
     * Gets the index of the selected room in the TableView.
     *
     * @return int index
     */
    public int getSelectedIndex() {
        return roomTable.getSelectionModel().getSelectedIndex();
    }


    /**
     * Deletes a room.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void deleteRoomClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Room selectedRoom = getSelectedRoom();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                if (AdminLogic.deleteRoomLogic(selectedRoom)) {
                    refresh();
                    // Creates an alert box to display the message.
                    GeneralMethods.alertBox("Delete room", "", "Room deleted!", AlertType.INFORMATION);
                } else {
                    // Create an alert box.
                    GeneralMethods.alertBox("Deletion failed", "",
                            "Room deletion failed", AlertType.WARNING);
                }
            } else {
                // Creates an alert box.
                GeneralMethods.alertBox("No Selection", "No Room Selected",
                        "Please select a Room in the table.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Creates a new room.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void createNewRoomClicked(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentSelectedRoom = null;
            RoomNewDialogView view = new RoomNewDialogView();
            view.start(stage);
            Room tempRoom = RoomEditDialogController.room;
            if (tempRoom == null) {
                return;
            }
            if (AdminLogic.createRoomLogic(tempRoom)) {
                refresh();
                // Creates an alert box to display the message.
                GeneralMethods.alertBox("New room", "", "New Room added!", AlertType.INFORMATION);
            } else {
                Alert alert = GeneralMethods.createAlert("New room",
                        "An error occurred, please try again!", stage, AlertType.ERROR);
                alert.showAndWait();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Opens dialog box for admin to edit a room.
     *
     * @param event event that triggered this method
     */
    @FXML
    private void editRoomClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Room selectedRoom = getSelectedRoom();
        int selectedIndex = getSelectedIndex();
        try {
            if (selectedIndex >= 0) {
                currentSelectedRoom = selectedRoom;

                RoomEditDialogView view = new RoomEditDialogView();
                view.start(stage);
                Room tempRoom = RoomEditDialogController.room;

                if (tempRoom == null) {
                    return;
                }
                if (AdminLogic.editRoomLogic(selectedRoom, tempRoom)) {
                    refresh();
                    // Creates an alert box to display the message.
                    GeneralMethods.alertBox("Edit room", "", "Room edited!", AlertType.INFORMATION);
                } else {
                    Alert alert = GeneralMethods.createAlert("Edit room",
                            "An error occurred, please try again!", stage, AlertType.INFORMATION);
                    alert.showAndWait();
                }
            } else {
                Alert alert = GeneralMethods.createAlert("No Selection",
                        "Please select a room in the table.", stage, AlertType.WARNING);
                alert.setHeaderText("No Room Selected");
                alert.showAndWait();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * This button redirects the user back to the login page.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    @FXML
    public void backClicked(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            AdminHomePageView adminHomePageView = new AdminHomePageView();
            adminHomePageView.start(stage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * This redirects the admin back to the login page.
     *
     * @param event is passed.
     * @throws IOException is thrown.
     */
    @FXML
    public void signOutButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // This open up a new login page.
        LoginView loginView = new LoginView();
        loginView.start(stage);
    }

}

