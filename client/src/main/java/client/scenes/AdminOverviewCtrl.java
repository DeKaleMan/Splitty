package client.scenes;

import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class AdminOverviewCtrl {

    private MainCtrl mainCtrl;

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
    }

    @FXML
    public void deleteEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void viewEvent() {
        Event event = eventList.getSelectionModel().getSelectedItem();
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
}
