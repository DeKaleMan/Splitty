package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Participant;
import commons.dto.ParticipantDTO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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


    private boolean host = false;
    private Participant editedParticipant;
    @FXML
    public Button cancelButton;
    @FXML
    public Button applyChangesButton;
    @FXML
    public Label invalidEmailLabel;
    @FXML
    public Label invalidIbanLabel;
    @FXML
    public Label invalidBicLabel;
    @FXML
    public Label unknownError;

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
        mainCtrl.setButtonRedProperty(cancelButton);
        mainCtrl.setButtonGreenProperty(applyChangesButton);
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public void showErrorBriefly(Label errorLabel) {
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> errorLabel.setVisible(false));
        pause.play();
    }

    public void autoFillWithMyData() {
        autoFillWithMyData(config.getId());
    }

    public void autoFillWithMyData(String participantId){ // autofill the fields with the current participant's data
        editedParticipant = serverUtils.getParticipant(participantId, eventId);
        // make sure to get my information from the server since I might have modified my profile before
        nameField.setText(editedParticipant.getName());
        emailField.setText(editedParticipant.getEmail());
        ibanField.setText(editedParticipant.getIBan());
        bicField.setText(editedParticipant.getBIC());
        accountHolderField.setText(editedParticipant.getAccountHolder());
    }
    public void editParticipant() {
        boolean error = false;
        String name = nameField.getText();
        if (name == null || name.isEmpty()){
            // maybe make this more explicit like alert the user using an
            // alertbox that his name will be "unknown name"
            name = "Unknown";
        }
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();
        String accountHolder = accountHolderField.getText();
        // email validation
        if(!email.contains("@") || !email.contains(".")){
            showErrorBriefly(invalidEmailLabel);
            error = true;
        }
        // iban validation
        if((!iban.isEmpty() && iban.length() < 15 ) || iban.length() > 34){
            showErrorBriefly(invalidIbanLabel);
            error = true;
        }
        // bic validation
        if((!bic.isEmpty() && bic.length() < 8) || bic.length() > 11){
            showErrorBriefly(invalidBicLabel);
            error = true;
        }
        if (error) {
            return;
        }
        ParticipantDTO participant = new ParticipantDTO(
                name,
                editedParticipant.getBalance(), // make sure to keep the balance the same
                iban,
                bic,
                email,
                accountHolder,
                eventId,
                editedParticipant.getUuid()
        );
        try {
            serverUtils.updateParticipant(editedParticipant.getUuid(), participant);
            mainCtrl.setConfirmationEditParticipant();
            cancel();
        } catch (RuntimeException e) {
            showErrorBriefly(unknownError);
        }
    }

    public void cancel() {
        if (host) {
            mainCtrl.showParticipantManager(eventId);
        } else {
            mainCtrl.showSplittyOverview(eventId);
        }
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}
