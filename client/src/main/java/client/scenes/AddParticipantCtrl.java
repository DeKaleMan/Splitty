package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.dto.ParticipantDTO;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AddParticipantCtrl implements Initializable {
    private final ServerUtils serverUtils;

    private final MainCtrl mainCtrl;

    @FXML
    public Label invalidEmailLabel;
    @FXML
    public Label invalidIbanLabel;
    @FXML
    public Label invalidBicLabel;


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

    public void showErrorBriefly(Label errorLabel) {
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }


    public void addParticipant(){
        String name = nameField.getText();
        if(name==null || name.isEmpty()){
            // maybe make this more explicit like alert the user using an
            // alertbox that his name will be "unknown name"
            name = "Unknown name";
        }
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();
        String accountHolder = accountHolderField.getText();

        // email validation
        if(!email.contains("@") || !email.contains(".")){
            showErrorBriefly(invalidEmailLabel);
            return;
        }
        // iban validation
        if(!iban.isEmpty() && iban.length() < 15){
            showErrorBriefly(invalidIbanLabel);
            return;
        }
        // bic validation
        if(!bic.isEmpty() && bic.length() < 8){
            showErrorBriefly(invalidBicLabel);
            return;
        }

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


    public void cancel(ActionEvent actionEvent) {
        mainCtrl.showSplittyOverview(eventId);
    }
}
