package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.dto.ParticipantDTO;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddParticipantCtrl implements Initializable {
    private final ServerUtils serverUtils;

    private final MainCtrl mainCtrl;
    @FXML
    private Text name;
    @FXML
    private Text accountHolder;
    @FXML
    private Label title;
    @FXML
    public Label invalidEmailLabel;

    @FXML
    public Label invalidIbanLabel;
    @FXML
    public Label invalidBicLabel;
    @FXML
    public Label unknownError;
    @FXML
    public Button cancelButton;
    @FXML
    public Button applyChangesButton;


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
        ImageView save = new ImageView(new Image("save_icon-removebg-preview.png"));
        save.setFitWidth(15);
        save.setFitHeight(15);
        applyChangesButton.setGraphic(save);
        mainCtrl.setButtonRedProperty(cancelButton);
        mainCtrl.setButtonGreenProperty(applyChangesButton);
    }
    public void resetFields() {
        emailField.setText("");
        ibanField.setText("");
        bicField.setText("");
        accountHolderField.setText("");
        nameField.setText("");
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
        boolean error = false;
        if(name == null || name.isEmpty()){
            error = !setConfirmationNoName();
            name = "Unknown";
        }
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();
        String accountHolder = accountHolderField.getText();

        // email validation
        if(email != null && !email.isEmpty() && (!email.contains("@") || !email.contains("."))){
            showErrorBriefly(invalidEmailLabel);
            error = true;
        }
        // iban validation
        if (iban != null && !iban.isEmpty() && (iban.length() < 15 || iban.length() > 34)){
            showErrorBriefly(invalidIbanLabel);
            error = true;
        }
        // bic validation
        if(bic != null && !bic.isEmpty() && (bic.length() < 8 || bic.length() > 11)){
            showErrorBriefly(invalidBicLabel);
            error = true;
        }
        if (error) {
            return;
        }
        Event event = serverUtils.getEventById(eventId);
        // generate a random uuid
        String uuid = java.util.UUID.randomUUID().toString();

        ParticipantDTO participantDTO = new ParticipantDTO(name, 0, iban, bic, email,
                accountHolder, eventId, uuid, event.getInviteCode());
        participantDTO.setGhostStatus(true);
        try {
            serverUtils.createParticipant(participantDTO);
            mainCtrl.setConfirmationAddParticipant();
            mainCtrl.showParticipantManager(eventId);
            resetFields();
        } catch (RuntimeException e) {
            showErrorBriefly(unknownError);
        }
    }
    public boolean setConfirmationNoName() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Adding Participant");
        alert.setContentText("You did not enter a name, if you wish to continue the name " +
                "of the participant you are adding will be set to 'Unknown'");
        AtomicBoolean ok = new AtomicBoolean(true);
        alert.showAndWait().ifPresent(action -> {
            if (action != ButtonType.OK) {
                ok.set(false);
            }
        });
        return ok.get();
    }
    public void cancel() {
        mainCtrl.showParticipantManager(eventId);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            cancel();
        }
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (k.match(press)) {
            addParticipant();
        }
    }

    public void setTitle(String txt){
        Platform.runLater(() -> {
            this.title.setText(txt);
        });
    }

    public void setCancelButton(String txt) {
        Platform.runLater(() -> {
            this.cancelButton.setText(txt);
        });
    }

    public void setApplyChangesButton(String txt){
        Platform.runLater(() -> {
            this.applyChangesButton.setText(txt);
        });
    }

    public void setName(String txt){
        Platform.runLater(() -> {
            this.name.setText(txt);
        });
    }

    public void setInvalidEmailLabel(String txt){
        Platform.runLater(() -> {
            this.invalidEmailLabel.setText(txt);
        });
    }

    public void setInvalidIbanLabel(String txt){
        Platform.runLater(() -> {
            this.invalidIbanLabel.setText(txt);
        });
    }

    public void setInvalidBicLabel(String txt){
        Platform.runLater(() -> {
            this.invalidBicLabel.setText(txt);
        });
    }

    public void setUnknownError(String txt){
        Platform.runLater(() -> {
            this.unknownError.setText(txt);
        });
    }

    public void setAccountHolder(String txt){
        Platform.runLater(() -> {
            this.accountHolder.setText(txt);
        });
    }

}
