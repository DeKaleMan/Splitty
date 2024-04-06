package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class StartScreenCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;



    private String currentLang = "en";
    @FXML
    private Label myEventsText;
    @FXML
    private Label createEventText;
    @FXML
    private TextField createEventTextField;

    @FXML
    private Label joinEventText;
    @FXML
    private TextField joinEventTextField;
    @FXML
    public Label invalidCodeError;
    @FXML
    public Label codeNotFoundError;
    @FXML
    public Label alreadyParticipantError;

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
    @FXML
    public Label myEventsNotFoundError;
    @FXML
    public Label noConnectionError;
    private List<Event> events;
    @FXML
    public Label settingsSavedLabel;
    @FXML
    public Button settingsButton;
    @FXML
    private ProgressIndicator progress;

    @FXML
    private ImageView flag;



    @Inject
    public StartScreenCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.config = config;
        events = new ArrayList<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView settings = new ImageView(new Image("Settings-icon.png"));
        settings.setFitWidth(15);
        settings.setFitHeight(15);
        settingsButton.setGraphic(settings);
        // Load the image
        Image image = new Image("Logo_.png"); // Path relative to your resources folder
        // Set the image to the ImageView
        imageView.setImage(image);
        //Image flag = new Image("enFlag.png");
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
    }

    public void fetchList() {
        eventListView.getItems().clear();

        events = mainCtrl.getMyEvents();
        if (events != null) {
            ObservableList<Event> newEventList = FXCollections.observableArrayList();
            ObservableList<Event> currentEventList = FXCollections.observableArrayList(events);
            currentEventList.stream().sorted(Comparator.comparing(Event::getLastActivity))
                    .forEach(newEventList::add);
            eventListView.setItems(newEventList);
        }
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
        String eventInviteCode = joinEventTextField.getText();
        if (eventInviteCode == null || eventInviteCode.isEmpty()) {
            invalidCodeError.setVisible(true);
            alreadyParticipantError.setVisible(false);
            codeNotFoundError.setVisible(false);
            return;
        }
        // already a participant of this event?
        if (mainCtrl.getMyEvents().stream().anyMatch(e ->
                e.getInviteCode().equals(eventInviteCode))) {
            invalidCodeError.setVisible(false);
            codeNotFoundError.setVisible(false);
            alreadyParticipantError.setVisible(true);
            return;
        }
        if (config.getName() == null || config.getName().isEmpty()) {
            if (!setConfirmationJoin()) {
                return;
            }
        }
        try {
            Participant p = mainCtrl.joinEvent(eventInviteCode);
            if (p == null) {
                invalidCodeError.setVisible(false);
                codeNotFoundError.setVisible(true);
                alreadyParticipantError.setVisible(false);
                return;
            }
            mainCtrl.showSplittyOverview(p.getEvent().getId());
            System.out.println("Joined event: " + joinEventTextField.getText());
        } catch (NumberFormatException e) {
            codeNotFoundError.setVisible(false);
            invalidCodeError.setVisible(true);
        } catch (RuntimeException e) {
            invalidCodeError.setVisible(false);
            codeNotFoundError.setVisible(true);
        }
        mainCtrl.setConfirmationJoinedEvent();
    }

    public boolean setConfirmationJoin() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Joining Event");
        alert.setContentText("You are about to join an event without having a name, " +
                "you can set your name in the settings. If you wish to continue," +
                " your name will be set to 'Unknown'");
        AtomicBoolean ok = new AtomicBoolean(true);
        alert.showAndWait().ifPresent(action -> {
            if (action != ButtonType.OK) {
                ok.set(false);
            }
        });
        return ok.get();
    }




    @FXML
    public void showAllEvents() {
        mainCtrl.showUserEventList();
    }

    public void setTitle(String eventTitle) {
        createEventTextField.setText(eventTitle);
    }

    public void setmyEventsText(String txt){
        this.myEventsText.setText(txt);
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
    public void setSettings(String text){
        this.settingsButton.setText(text);
    }


    public void setLanguageSelect() {
        //TODO add a check if this list is the same as the actual list otherwise
        // set it or find a way to initialize this once without the actual values because those are null before you init
        ObservableList<String> languages = FXCollections.observableArrayList();
        mainCtrl.language = config.getLanguage();
        if (mainCtrl.language == null) {
            mainCtrl.language = "en";
        }
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
        mainCtrl.showSettings(noConnectionError.visibleProperty().get());
    }


    public void handleMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Event event = (Event) eventListView.getSelectionModel().getSelectedItem();
            if (event != null) {
                mainCtrl.showSplittyOverview(event.getId());
            }
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.N,
                KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN);
        if (k.match(press)) {
            createEvent();
        }
    }

    public void resetErrors() {
        codeNotFoundError.setVisible(false);
        invalidCodeError.setVisible(false);
        alreadyParticipantError.setVisible(false);
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
                // Clicked outside the choice box, hide it
                languageSelect.setVisible(false);
            }
        });
        imageView.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // Hide the choice box when any key is pressed
            languageSelect.setVisible(false);
        });
    }

    public void addEvent(Event event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
        ObservableList<Event> currentEventList = FXCollections.observableArrayList(events);
        eventListView.setItems(currentEventList);
    }

    public void setSettingsSavedLabel() {
        settingsSavedLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(5));
        visiblePause.setOnFinished(
                event1 -> settingsSavedLabel.setVisible(false)
        );
        visiblePause.play();
    }
    public void setNoEventsError(boolean b) {
        myEventsNotFoundError.setVisible(b);
        noConnectionError.setVisible(b);
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Event event = eventListView.getSelectionModel().getSelectedItem();
            if (event != null) {
                mainCtrl.showSplittyOverview(event.getId());
            }
        }
    }
}
