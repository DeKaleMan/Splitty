package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Participant;
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

public class EditParticipantCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final Config config;
    private final MainCtrl mainCtrl;

    @FXML
    public Label invalidEmailLabel;
    @FXML
    public Label invalidIbanLabel;
    @FXML
    public Label invalidBicLabel;

    @FXML
    private Label title;

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
    public EditParticipantCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public void showErrorBriefly(Label errorLabel) {
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }

    public void autoFillWithMyData(){ // autofill the fields with the current participant's data

        // make sure to get my information from the server since i might have modified my profile before
        Participant me = serverUtils.getParticipant(config.getId(), eventId);
        nameField.setText(me.getName());
        emailField.setText(me.getEmail());
        ibanField.setText(me.getIBan());
        bicField.setText(me.getBIC());
        accountHolderField.setText(me.getAccountHolder());
    }
    public void editParticipant(ActionEvent actionEvent) {
        Participant me = serverUtils.getParticipant(config.getId(), eventId);

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

        ParticipantDTO participant = new ParticipantDTO(
                name,
                me.getBalance(), // make sure to keep the balance the same
                iban,
                bic,
                email,
                accountHolder,
                eventId,
                config.getId()
        );
        participant.setGhostStatus(false); // make sure to set the ghost property ghost to true in case you want to edit a ghost participant

        serverUtils.updateParticipant(config.getId(), participant);
        mainCtrl.showSplittyOverview(eventId);
    }

    public void cancel(ActionEvent actionEvent) {
        mainCtrl.showSplittyOverview(eventId);
    }
}
