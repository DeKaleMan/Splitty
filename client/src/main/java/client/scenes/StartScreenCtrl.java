package client.scenes;

import client.utils.Config;
import client.utils.Language;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

public class StartScreenCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    @FXML
    private Label createEventText;
    @FXML
    private TextField createEventTextField;

    @FXML
    private Label joinEventText;
    @FXML
    private TextField joinEventTextField;

    @FXML
    private Button adminLogin;
    @FXML
    private Button showAllEventsButton;
    @FXML
    private Button join;
    @FXML
    private Button create;

    @FXML
    private ComboBox languageSelect;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<Event> eventListView;

    private int eventCode;

    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    public void initialize() {
        eventListView.setCellFactory(new Callback<>() {
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
        // sorts the events by last activity
        List<Event> events = mainCtrl.getMyEvents();
        if(events!=null){
            ObservableList<Event> newEventList = FXCollections.observableArrayList();
            ObservableList<Event> currentEventList = FXCollections.observableArrayList(events);
            currentEventList.stream().sorted(Comparator.comparing(Event::getLastActivity))
                    .forEach(newEventList::add);
            eventListView.setItems(newEventList);
        }

        // Load the image
        Image image = new Image("Logo_.png"); // Path relative to your resources folder
        // Set the image to the ImageView
        imageView.setImage(image);
    }

    /**
     * Creates an event with the title specified in the createEventTextField
     * TO DO - actually create an event
     */
    public void createEvent(){
        String name = createEventTextField.getText();
        if (name == null || name.isEmpty()) {
            name = "New event";
        }
        mainCtrl.showCreateEvent(name);
        //TO DO: add event to database, fill in more information about the event.
        //This will happen in the CreateEventCtrl class!
    }

    /**
     * Join an event with the title specified in the joinEventTextField
     * TO DO - join an event by the event id/URL
     */
    public void joinEvent(){
        eventCode = Integer.parseInt(joinEventTextField.getText());
        Participant p = mainCtrl.joinEvent(eventCode);
        if (p == null) {
            // show error message
            return;
        }
        mainCtrl.showSplittyOverview(eventCode);
        System.out.println("Joined event: " + joinEventTextField.getText());
    }

    @FXML
    public void showAllEvents() {
        mainCtrl.showUserEventList();
    }

    public void setTitle(String eventTitle) {
        createEventTextField.setText(eventTitle);
    }


    public void showAdminLogin(ActionEvent actionEvent) {
        mainCtrl.showAdminLogin();
    }
    public void setCreateEventText(String text) {
        createEventText.setText(text);
    }

    public void setJoinEventText(String text) {
        joinEventText.setText(text);
    }

    public void setAdminLogin(String text) {
        adminLogin.setText(text);
    }
    public void setShowAllEvents(String text) {
        showAllEventsButton.setText(text);
    }
    public void setJoinButtonText(String text) {
        join.setText(text);
    }
    public void setCreateButtonText(String text) {
        create.setText(text);
    }
    public void setNoEventLabel(String text){
//        noEventLabel.setText(text);
    }


    public void setLanguageSelect(String language){

        ObservableList<String> languages = FXCollections.observableArrayList();

        for(Language l: Language.values()){
            languages.add(l.toString());
        }
        languageSelect.setItems(languages);
        languageSelect.setValue(language);
    }

    @FXML
    public void changeLanguage() {
        try {
            if (languageSelect.getSelectionModel().getSelectedItem() != null) {
                String selected = (String) languageSelect.getSelectionModel().getSelectedItem();
                Language toLang = Language.valueOf(selected);
                mainCtrl.changeLanguage(toLang);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void showSettings(){
        mainCtrl.showSettings();
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Event event = (Event) eventListView.getSelectionModel().getSelectedItem();
            if (event != null) {
                mainCtrl.showSplittyOverview(event.getId());
            }
        }
    }
}
