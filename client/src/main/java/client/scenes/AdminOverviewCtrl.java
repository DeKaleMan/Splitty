package client.scenes;

import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.inject.Inject;

public class AdminOverviewCtrl {

    private MainCtrl mainCtrl;

    ObservableList<String> sortList = FXCollections.observableArrayList("Title", "Creation date", "Last activity");

    // This is test code and should be replaced using api requests
    ObservableList<Event> testEventList = FXCollections.observableArrayList(
            new Event("Test", "10-11-2005", "Jesse", "testdesc"),
            new Event("Test1", "10-11-2005", "Jesse", "testdesc"),
            new Event("Test3", "10-11-2005", "Jesse", "testdesc"),
            new Event("Test4", "10-11-2005", "Jesse", "testdesc"),
            new Event("Test5", "10-11-2005", "Jesse", "testdesc"));

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
    private TextArea jsonImportTextArea;

    @FXML
    private Button jsonImportButton;

    @Inject
    public AdminOverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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

        eventList.setItems(testEventList);
    }

    @FXML
    public void showImportFields() {
        jsonImportTextArea.setVisible(true);
        jsonImportButton.setVisible(true);
        sortByText.setVisible(false);
        sortComboBox.setVisible(false);
    }

    @FXML
    public void importEvent() {
        String jsonDump = jsonImportTextArea.getText();
        jsonImportTextArea.clear();

        jsonImportTextArea.setVisible(false);
        jsonImportButton.setVisible(false);
        sortByText.setVisible(true);
        sortComboBox.setVisible(true);
    }

    @FXML
    public void exportEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        System.out.println("Export: " + event);
    }

    @FXML
    public void deleteEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        System.out.println("Delete: " + event);
    }

    @FXML
    public void viewEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
        System.out.println("View: " + event);
    }

    @FXML
    public void updateEventSorting() {
        String selectedOption = sortComboBox.getValue();
        // Update the event sorting based on the new selected option
        switch(selectedOption) {
            case "Title":
                System.out.println("Title sorting selected");
                break;
            case "Creation date":
                System.out.println("Creation date sorting selected");
                break;
            case "Last activity":
                System.out.println("Last activity sorting selected");
                break;
        }
    }

    @FXML
    public void logOut() {
        mainCtrl.showStartScreen();
    }
}
