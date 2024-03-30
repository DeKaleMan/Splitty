package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class StartScreenCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    private String currentLang;

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
    private ListView eventListView;

    @FXML
    private ProgressIndicator progress;
    private int eventCode;
    @FXML
    private ImageView flag;


    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.config = config;

    }
    // list the 3 most recent events on the start page
    @FXML
    public void initialize() {
        eventListView.getItems().clear();
        eventListView.setCellFactory(eventListView -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                } else {
                    setText(event.getName());
                }
            }
        });

        List<Event> events = mainCtrl.getMyEvents();
        if (events != null) {
            eventListView.setItems(FXCollections.observableArrayList(events));
        }
                // Load the image
        Image image = new Image("Logo_.png"); // Path relative to your resources folder
        // Set the image to the ImageView
        imageView.setImage(image);
        //Image flag = new Image("enFlag.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //this is just because we implement it, I don't think we need it but I wanted to test is like this
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
            mainCtrl.showSplittyOverview(event.getId());
        });

        button.setText(event.getName());
        label.setText(event.getDate() + ": " + event.getDescription());
    }

    /**
     * Creates an event with the title specified in the createEventTextField
     * TO DO - actually create an event
     */
    public void createEvent() {
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
    public void joinEvent() {
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

    public void setNoEventLabel(String text) {
//        noEventLabel.setText(text);
    }


    public void setLanguageSelect() {
        //TODO add a check if this list is the same as the actual list otherwise
        // set it or find a way to initialize this once without the actual values because those are null before you init
        ObservableList<String> languages = FXCollections.observableArrayList();
        mainCtrl.language = config.getLanguage();
        languages.addAll(mainCtrl.languages);
        languageSelect.setItems(languages);
        languageSelect.setValue(mainCtrl.language);
        //languageSelect.setValue(flag);
        Image flag = mainCtrl.getFlag();
        setFlag(flag);
        if (!mainCtrl.language.equals(currentLang)) {
            changeLanguage();
        }
//        languageSelect.setItems(FXCollections.observableList(mainCtrl.languages));
    }

    @FXML
    public void changeLanguage() {
        setProgress();
        try {
            if (languageSelect.getSelectionModel().getSelectedItem() != null) {
                String selected = (String) languageSelect.getSelectionModel().getSelectedItem();

                //Language toLang = Language.valueOf(selected);
                if (mainCtrl.languages.contains(selected)) {
                    config.setLanguage(selected);
                    config.write();
                    String toLang = selected;
                    mainCtrl.changeLanguage(toLang);
                }

                currentLang = selected;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        setLanguageSelect();
        setProgress();
        languageSelect.setVisible(false);

    }

    public void setProgress() {
        if (this.progress.isVisible()) {
            this.progress.setVisible(false);
        } else {
            this.progress.setVisible(true);
        }
    }

    public void showSettings() {
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

    public void setFlag(Image image) {
        flag.setImage(image);
    }

    @FXML
    public void showLangOptions() {
//        this.languageSelect.setVisible(true);
        //this.languageSelect.setValue(flag);
        languageSelect.show();

        imageView.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!languageSelect.getBoundsInParent().contains(event.getX(), event.getY())) {
                // Clicked outside of the choice box, hide it
                languageSelect.setVisible(false);
            }
        });
        imageView.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // Hide the choice box when any key is pressed
            languageSelect.setVisible(false);
        });
    }

}
