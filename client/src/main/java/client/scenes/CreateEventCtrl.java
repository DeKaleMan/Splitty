package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.EventDTO;
import commons.Participant;
import commons.dto.ParticipantDTO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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


    // participant text fields

    @FXML
    private TextField nameField;
    @FXML
    public Label hostNameError;
    @FXML
    private TextField emailField;
    @FXML
    private TextField iBanField;
    @FXML
    private TextField bICField;
    @FXML
    private TextField accountHolderField;

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


    public void setTitle(String title) {
        titleField.setText(title);
    }

    @FXML
    public void cancel() {
        mainCtrl.showStartScreen();
    }

    @FXML
    public ParticipantDTO addHost(int id) {
        return new ParticipantDTO(nameField.getText(), 0.0, config.getIban(), config.getBic(),
                config.getEmail(), config.getName(), id, config.getId());
    }

    @FXML
    public void createEvent() {
        String name = titleField.getText();
        String dateString = datePicker.getEditor().getText();
        LocalDate localDate = datePicker.getValue();
        String description = eventDescriptionArea.getText();
        Date date = null;
        boolean error = false;
        try {
            if (dateString == null || dateString.isEmpty()) {
                throw new IllegalArgumentException();
            }
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (IllegalArgumentException e) {
            dateIncorrectError.setVisible(false);
            dateEmptyError.setVisible(true);
            error = true;
        } catch (Exception e) {
            dateEmptyError.setVisible(false);
            dateIncorrectError.setVisible(true);
            error = true;
        }
        if (name == null || name.isEmpty() || error){
            if (name == null || name.isEmpty()) {
                titleError.setVisible(true);
            }
            if (nameField.getText() == null || nameField.getText().isEmpty()) {
                hostNameError.setVisible(true);
            }
            return;
        }

        //fetch owner
        String owner = config.getId();
        // create new event and add to database, go to that event overview and add participants via database.
        EventDTO event = new EventDTO(name, date, owner, description);
        Event eventCreated = serverUtils.addEvent(event);
        ParticipantDTO participantDTO = addHost(eventCreated.getId());
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
}
