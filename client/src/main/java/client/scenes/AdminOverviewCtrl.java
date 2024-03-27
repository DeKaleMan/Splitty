package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

public class AdminOverviewCtrl {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;

    ObservableList<String> sortList = FXCollections.observableArrayList("Title", "Creation date", "Last activity");

    @FXML
    private Button importEventButton;

    @FXML
    private Button viewEventButton;

    @FXML
    private Button exportEventButton;

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
    private TextArea jsonImportTextArea;

    @FXML
    private Button jsonImportButton;
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

        ObservableList<Event> events = FXCollections.observableArrayList(serverUtils.getAllEvents());
        eventList.setItems(events);
    }

    @FXML
    public void showImportFields() {
        jsonImportPane.setVisible(true);
        jsonImportTextArea.setVisible(true);
        jsonImportButton.setVisible(true);
        sortByText.setVisible(false);
        sortComboBox.setVisible(false);
    }

    @FXML
    public void importEvent() {
        String jsonDump = jsonImportTextArea.getText();
        jsonImportTextArea.clear();

        // TODO: Add event, participants etc. to the database based on jsonDump (DB not fully done so can't do it yet)
        jsonImportPane.setVisible(false);
        jsonImportTextArea.setVisible(false);
        jsonImportButton.setVisible(false);
        sortByText.setVisible(true);
        sortComboBox.setVisible(true);
    }

    @FXML
    public void exportEvent() {
        Event toExportEvent = eventList.getSelectionModel().getSelectedItem();
        List<Participant> toExportParticipants = serverUtils.getParticipants(toExportEvent.getId());
        // TODO: Convert event to json and display somehow (DB not fully done so can't do it yet)
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Export: " + toExportEvent);
    }

    @FXML
    public void deleteEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        serverUtils.deleteEventById(event.getId());
        ObservableList<Event> events = FXCollections.observableArrayList(serverUtils.getAllEvents());
        eventList.setItems(events);
        updateEventSorting();
    }

    @FXML
    public void viewEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        mainCtrl.showSplittyOverview(event.getId());
    }

    @FXML
    public void updateEventSorting() {
        String selectedOption = sortComboBox.getValue();
        ObservableList<Event> newEventList = FXCollections.observableArrayList();
        // Update the event sorting based on the new selected option
        switch(selectedOption) {
            case "Title":
                System.out.println("Title sorting selected");
                ObservableList<Event> currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getName))
                        .forEach(newEventList::add);
                break;
            case "Creation date":
                System.out.println("Creation date sorting selected");
                currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getDate))
                        .forEach(newEventList::add);
                break;
            case "Last activity":
                System.out.println("Last activity sorting selected");
                currentEventList = eventList.getItems();
                currentEventList.stream().sorted(Comparator.comparing(Event::getLastActivity))
                        .forEach(newEventList::add);
                break;
        }
        eventList.setItems(newEventList);
    }

    public void logOut() {
        mainCtrl.showStartScreen();
        mainCtrl.setAdmin(false);
    }

    public void setAdminManagementOverviewText(String txt) {
        adminManagementOverviewText.setText(txt);
    }


    public void setImportEventButtonText(String txt) {
        importEventButton.setText(txt);
    }


    public void setExportEventButtonText(String txt) {
        exportEventButton.setText(txt);
    }

    @FXML
    public void setDeleteEventButtonText(String txt) {
        deleteEventButton.setText(txt);
    }

    @FXML
    public void setServerTagText(String txt) {
        serverTag.setText(txt);
    }

    @FXML
    public void setViewEventButtonText(String txt) {
        viewEventButton.setText(txt);
    }

    @FXML
    public void setJsonImportTextAreaPromptText(String txt) {
        jsonImportTextArea.setPromptText(txt);
    }

    @FXML
    public void setSortByText(String txt) {
        sortByText.setText(txt);
    }

    @FXML
    public void setLogOutButtonText(String txt) {
        logOutButton.setText(txt);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            logOut();
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
        jsonImportTextArea.setVisible(false);
        jsonImportButton.setVisible(false);
        jsonImportPane.setVisible(false);
    }
}
