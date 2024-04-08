package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.EventDTO;
import commons.Participant;
import commons.dto.ParticipantDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    @FXML
    public Button cancelButton;
    @FXML
    public Button createButton;


    // event text fields
    @FXML
    private TextField titleField;
    @FXML
    public Label titleError;
    @FXML
    private DatePicker datePicker;
    @FXML
    public Label dateEmptyError;
    @FXML
    public Label dateIncorrectError;

    @FXML
    private TextArea eventDescriptionArea;
    @FXML
    private Label eventNameText;
    @FXML
    private Label dateText;
    @FXML
    private Label eventDescriptionText;
    @FXML
    private Label nameText;
    @FXML
    private Label accountHolderText;





    @FXML
    private Button goToSettings;
    // participant text fields

    @FXML
    public Label hostNameError;


    // this list will store all added participants until
    // the create event button is clicked, then it will be added to the database
    // via foreign keys
    private List<Participant> participants;

    @Inject
    public CreateEventCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        participants = new ArrayList<>();
        this.config = config;

    }

    @FXML
    public void initialize() {
        mainCtrl.setButtonGreenProperty(createButton);
        mainCtrl.setButtonRedProperty(cancelButton);
    }

    public void setTitle(String title) {
        titleField.setText(title);
    }

    @FXML
    public void cancel() {
        mainCtrl.showStartScreen();
    }
    @FXML
    public void showSettings(){
        mainCtrl.showSettings(false);
    }

    @FXML
    public ParticipantDTO addHost(int id, String inviteID) {
        return new ParticipantDTO(config.getName(), 0.0, config.getIban(), config.getBic(),
                config.getEmail(), config.getName(), id, config.getId(), inviteID);
    }

    @FXML
    public void createEvent() {
        String name = titleField.getText();
        String dateString = datePicker.getEditor().getText();
        String description = eventDescriptionArea.getText();
        Date date = null;
        boolean error = false;
        if(config.getName() == null){
            noNameError();
            error = true;
        }
        try {
            LocalDate localDate = datePicker.getValue();
            if (dateString == null || dateString.isEmpty()) {
                throw new IllegalArgumentException();
            }
            date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (IllegalArgumentException e) {
            dateIncorrectError.setVisible(false);
            dateEmptyError.setVisible(true);
            error = true;
            return;
        } catch (Exception e) {
            dateEmptyError.setVisible(false);
            dateIncorrectError.setVisible(true);
            error = true;
            return;
        }
        //fetch owner
        String owner = config.getId();
        // create new event and add to database, go to that event overview and add participants via database.
        EventDTO event = new EventDTO(name, date, owner, description);
        Event eventCreated = serverUtils.addEvent(event);
        ParticipantDTO participantDTO = null;
        participantDTO = addHost(eventCreated.getId(), eventCreated.getInviteCode());
        serverUtils.createParticipant(participantDTO);
        mainCtrl.showSplittyOverview(eventCreated.getId());
        mainCtrl.addEvent(eventCreated);
        mainCtrl.setConfirmationEventCreated();
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            cancel();
        }
    }

    public void resetTitleFieldError() {
        titleError.setVisible(false);
    }

    public void resetDateFieldError() {
        dateIncorrectError.setVisible(false);
        dateEmptyError.setVisible(false);
    }
    public void noNameError(){
        hostNameError.setVisible(true);
        goToSettings.setVisible(true);
    }
    public void resetValues(){
        titleField.setText(null);
        datePicker.setValue(null);
        eventDescriptionArea.setText(null);
        resetDateFieldError();
        resetTitleFieldError();
        hostNameError.setVisible(false);
        goToSettings.setVisible(false);
    }

    public void setEventNameText(String txt){
        Platform.runLater(() -> {
            this.eventNameText.setText(txt);
        });
    }

    public void setDateText(String txt){
        Platform.runLater(() -> {
            this.dateText.setText(txt);
        });
    }

    public void setEventDescriptionText(String txt){
        Platform.runLater(() -> {
            this.eventDescriptionText.setText(txt);
        });
    }

    public void setEventDescriptionArea(String txt){
        Platform.runLater(() -> {
            this.eventDescriptionArea.setPromptText(txt);
        });
    }

    public void setNameText(String txt){
        Platform.runLater(() -> {
            this.nameText.setText(txt);
        });
    }

    public void setCreateButton(String txt){
        Platform.runLater(() -> {
            this.createButton.setText(txt);
        });
    }

    public void setCancelButton(String txt){
        Platform.runLater(() -> {
            this.cancelButton.setText(txt);
        });
    }

    public void setRequired(String txt){
        Platform.runLater(() -> {
            //this.nameField.setPromptText(txt);
            this.titleField.setPromptText(txt);
        });
    }
}
