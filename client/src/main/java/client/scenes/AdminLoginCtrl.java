package client.scenes;

import client.utils.AdminServerUtils;
import client.utils.Config;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;
    private AdminServerUtils adminServerUtils;
    private Config config;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl, AdminServerUtils adminServerUtils, Config config) {
        this.mainCtrl = mainCtrl;
        this.adminServerUtils = adminServerUtils;
        this.config = config;
    }

    @FXML
    private Label signIn;
    @FXML
    private Button back;
    @FXML
    private Label instruction;
    @FXML
    private Text passwordInstructionLink;

    @FXML
    private Button signInButton;

    @FXML
    public Label serverNotFoundError;
    @FXML
    private PasswordField passwordField;
    @FXML
    public Label incorrectPasswordError;
    @FXML
    private Label passwordInstructionsText;

    @FXML
    public void adminSignIn(ActionEvent actionEvent) {
        String serverUrl = config.getConnection();
        String password = passwordField.getText();
        Response response;
        try {
            response = adminServerUtils.validatePassword(password, serverUrl);
        } catch (RuntimeException e) {
            serverNotFoundError.setVisible(true);
            return;
        }

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            mainCtrl.showAdminOverview();
            mainCtrl.setAdmin(true);
        } else {
            incorrectPasswordError.setVisible(true);
        }

    }
    public void reset(){
        this.passwordField.setText(null);
        this.incorrectPasswordError.setVisible(false);
        this.serverNotFoundError.setVisible(false);
    }

    public void setSignIn(String txt) {
        Platform.runLater(() -> {
            this.signIn.setText(txt);
        });
    }

    public void setInstruction(String txt) {
        Platform.runLater(() -> {
            this.instruction.setText(txt);
        });
    }

    public void setSignInButton(String txt) {
        Platform.runLater(() -> {
            this.signInButton.setText(txt);
        });
    }

    public void setPasswordField(String txt) {
        Platform.runLater(() -> {
            this.passwordField.setPromptText(txt);
        });
    }

    public void setPasswordInstructionsText(String txt) {
        Platform.runLater(() -> {
            this.passwordInstructionsText.setText(txt);
        });
    }

    public void setBack(String txt){
        Platform.runLater(() -> {
            this.back.setText(txt);
        });
    }


    public void back() {
        mainCtrl.showStartScreen();
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }

    public void resetPasswordError(KeyEvent keyEvent) {
        incorrectPasswordError.setVisible(false);
    }

    public void resetServerErrors(KeyEvent keyEvent) {
        serverNotFoundError.setVisible(false);
    }
}
