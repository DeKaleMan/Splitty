package client.scenes;

import client.utils.Language;
import client.utils.ServerUtils;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartScreenCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
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

    // the events
    @FXML
    private Button eventButton1;
    @FXML
    private Label eventLabel1;

    @FXML
    private Button eventButton2;
    @FXML
    private Label eventLabel2;

    @FXML
    private Button eventButton3;
    @FXML
    private Label eventLabel3;

    @FXML
    private Label noEventLabel;

    @FXML
    private ImageView imageView;

    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    // list the 3 most recent events on the start page
    public void initialize() {
        // retrieve from database based on recency (now null to have something)
        // the commented below is for testing
        noEventLabel.setVisible(false);
        Event event1 = new Event("test event", new Date(10, 10, 2005), "Admin", "This is just for testing");
        Event event2 = null;
        Event event3 = null;
        setup(event1, eventButton1, eventLabel1);
        setup(event2, eventButton2, eventLabel2);
        setup(event3, eventButton3, eventLabel3);
        // this will be querified
        if (event1 == null && event2 == null && event3 == null) {
            noEventLabel.setVisible(true);
        }

        // Load the image
        Image image = new Image("Logo_.png"); // Path relative to your resources folder

        // Set the image to the ImageView
        imageView.setImage(image);

    }

    private void setup(Event event, Button button, Label label) {
        if (event == null) {
            button.setVisible(false);
            label.setVisible(false);
            return;
        }
        button.setVisible(true);
        label.setVisible(true);

        button.setOnAction(something -> {
            mainCtrl.showSplittyOverview(event.getName());
        });

        button.setText(event.getName());
        label.setText(event.getDate() + ": " + event.getDescription());
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
        System.out.println("Joined event: " + joinEventTextField.getText());
        mainCtrl.showSplittyOverview(joinEventTextField.getText());
        //TO DO, this will happen here in this method
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
        noEventLabel.setText(text);
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
    public void changeLanguage(){
        try{
            if(languageSelect.getSelectionModel().getSelectedItem() != null){
                String selected = (String) languageSelect.getSelectionModel().getSelectedItem();
                Language toLang =  Language.valueOf(selected);
                mainCtrl.changeLanguage(toLang);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
