package client.scenes;

import client.utils.EventDump;
import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

public class AdminOverviewCtrl {


    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;

    ObservableList<String> sortList = FXCollections.observableArrayList("Title", "Creation date", "Last activity");

    @FXML
    private Button importEventButton;

    @FXML
    private Button viewEventButton;
    @FXML
    public Button refreshButton;

    @FXML
    private Button exportEventButton;
    @FXML
    public Label noEventSelectedError;
    @FXML
    private Button deleteEventButton;

    @FXML
    private Text sortByText;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private ListView<Event> eventList;
    @FXML
    public AnchorPane jsonImportPane;
    @FXML
    private Text adminManagementOverviewText;
    @FXML
    private Text serverTag;
    @FXML
    private Button logOutButton;

    @Inject
    public AdminOverviewCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    @FXML
    public void initialize() {
        setImage();
        // set buttons
        mainCtrl.setButtonRedProperty(deleteEventButton);
        mainCtrl.setButtonRedProperty(logOutButton);
        sortComboBox.setItems(sortList);

        // This code (It took me some googling to find this out) makes sure that the items
        // that are shown in the listview only show the event name, the default is the toString method
        eventList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Event> call(ListView<Event> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Event event, boolean empty) {
                        super.updateItem(event, empty);
                        if (empty || event == null) {
                            setText(null);
                        } else {
                            setText(event.getName());
                        }
                    }
                };
            }
        });
        refreshEvents();
    }

    private void setImage() {
        ImageView export = new ImageView(new Image("exportIcon.png"));
        export.setFitWidth(15);
        export.setFitHeight(15);
        exportEventButton.setGraphic(export);
        ImageView importIcon = new ImageView(new Image("importIcon.png"));
        importIcon.setFitWidth(15);
        importIcon.setFitHeight(15);
        importEventButton.setGraphic(importIcon);
        ImageView refresh = new ImageView(new Image("refreshIcon.jpg"));
        refresh.setFitWidth(15);
        refresh.setFitHeight(15);
        refreshButton.setGraphic(refresh);
        ImageView trash = new ImageView(new Image("trashIcon.png"));
        trash.setFitWidth(14);
        trash.setFitHeight(14);
        deleteEventButton.setGraphic(trash);
    }

    @FXML
    public void importEvent() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EventDump eventDump = objectMapper.readValue(file, EventDump.class);
            eventDump.setServerUtils(serverUtils);
            eventDump.importEvent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        refreshEvents();
        jsonImportPane.setVisible(false);
        sortByText.setVisible(true);
        sortComboBox.setVisible(true);
    }

    @FXML
    public void exportEvent() {
        Event toExportEvent = eventList.getSelectionModel().getSelectedItem();
        if (toExportEvent == null) {
            noEventSelectedError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                    event -> noEventSelectedError.setVisible(false)
            );
            visiblePause.play();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                EventDump eventDump = new EventDump(toExportEvent.getId(), serverUtils);
                String jsonContent = eventDump.exportEvent();

                // Write the JSON content to the selected file
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonContent);
                fileWriter.close();

                System.out.println("Event exported to JSON file successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error occurred while exporting event to JSON file.");
            }
        }
    }

    @FXML
    public void deleteEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        if (event == null) {
            noEventSelectedError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                    event1 -> noEventSelectedError.setVisible(false)
            );
            visiblePause.play();
            return;
        }
        serverUtils.deleteEventById(event.getId());
        ObservableList<Event> events = FXCollections.observableArrayList(serverUtils.getAllEvents());
        eventList.setItems(events);
        updateEventSorting();
    }

    @FXML
    public void viewEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        if (event == null) {
            noEventSelectedError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                    event1 -> noEventSelectedError.setVisible(false)
            );
            visiblePause.play();
            return;
        }
        mainCtrl.showSplittyOverview(event.getId());
    }

    @FXML
    public void updateEventSorting() {
        String selectedOption = sortComboBox.getValue();
        ObservableList<Event> newEventList = FXCollections.observableArrayList();
        // Update the event sorting based on the new selected option
        if (selectedOption == null) {
            selectedOption = "Title";
        }
        switch(selectedOption) {
            case "Title":
                ObservableList<Event> currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getName))
                        .forEach(newEventList::add);
                break;
            case "Creation date":
                currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getDate))
                        .forEach(newEventList::add);
                newEventList = FXCollections.observableArrayList(newEventList.reversed());
                break;
            case "Last activity":
                currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getLastActivity))
                        .forEach(newEventList::add);
                newEventList = FXCollections.observableArrayList(newEventList.reversed());
                break;
        }
        eventList.setItems(newEventList);
    }

    public void logOut() {
        mainCtrl.showStartScreen();
        mainCtrl.setAdmin(false);
    }
    public void setAdminManagementOverviewText(String txt) {
        Platform.runLater(() -> {
            adminManagementOverviewText.setText(txt);
        });
    }

    public void setImportEventButtonText(String txt) {
        Platform.runLater(() -> {
            importEventButton.setText(txt);
        });
    }

    public void setExportEventButtonText(String txt) {
        Platform.runLater(() -> {
            exportEventButton.setText(txt);
        });
    }

    @FXML
    public void setDeleteEventButtonText(String txt) {
        Platform.runLater(() -> {
            deleteEventButton.setText(txt);
        });
    }

    @FXML
    public void setServerTagText(String txt) {
        Platform.runLater(() -> {
            serverTag.setText(txt);
        });
    }

    @FXML
    public void setViewEventButtonText(String txt) {
        Platform.runLater(() -> {
            viewEventButton.setText(txt);
        });
    }

    @FXML
    public void setSortByText(String txt) {
        Platform.runLater(() -> {
            sortByText.setText(txt);
        });
    }
    public void setLogOutButtonText(String txt) {
        logOutButton.setText(txt);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            logOut();
        }
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN);
        if (k.match(press)) {
            if (eventList.getSelectionModel().getSelectedItem() != null) {
                deleteEvent();
            }
        }
    }
    @FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Event event = eventList.getSelectionModel().getSelectedItem();
            if (event != null) {
                mainCtrl.showSplittyOverview(event.getId());
            }
        }
    }

    @FXML
    public void abortImportMouse(MouseEvent press) {
        Platform.runLater(()-> jsonImportPane.setVisible(false));
    }

    @FXML
    public void refreshEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList(serverUtils.getAllEvents());
        eventList.setItems(events);
        updateEventSorting();
    }
}
