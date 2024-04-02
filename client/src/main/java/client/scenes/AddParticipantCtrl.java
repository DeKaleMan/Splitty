package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.dto.ParticipantDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AddParticipantCtrl implements Initializable {
    private final ServerUtils serverUtils;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;
    @FXML
    private TextField accountHolderField;

    MainCtrl mainCtrl;
    private int eventId;

    @Inject
    public AddParticipantCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public void addParticipant(){
        String name = nameField.getText();
        if(name==null || name.isEmpty()){
            name = "Unknown name"; // something like this
        }
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();
        String accountHolder = accountHolderField.getText();

        Event event = serverUtils.getEventById(eventId);
        // generate a random uuid

        String uuid = java.util.UUID.randomUUID().toString();

        ParticipantDTO participantDTO = new ParticipantDTO(
                name,
                0,
                iban,
                bic,
                email,
                accountHolder,
                eventId,
                uuid,
                event.getInviteCode()
        );
        participantDTO.setGhostStatus(true);

        serverUtils.createParticipant(participantDTO); // Maybe some error handling?
        mainCtrl.showSplittyOverview(eventId);
    }


}
