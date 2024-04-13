package client.scenes;

import client.utils.AdminServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;
    private AdminServerUtils adminServerUtils;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl, AdminServerUtils adminServerUtils) {
        this.mainCtrl = mainCtrl;
        this.adminServerUtils = adminServerUtils;
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
    private TextField urlField;
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
        String serverUrl = urlField.getText();
        String password = passwordField.getText();
        if (serverUrl == null || serverUrl.isEmpty()) {
            serverNotFoundError.setVisible(true);
            return;
        }
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
        this.urlField.setText(null);
        this.passwordField.setText(null);
        this.incorrectPasswordError.setVisible(false);
        this.serverNotFoundError.setVisible(false);
    }
    @FXML
    public void showPasswordInstructions(MouseEvent mouseEvent) {
        passwordInstructionsText.setVisible(true);
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

    public void setPasswordInstructionLink(String txt) {
        Platform.runLater(() -> {
            this.passwordInstructionLink.setText(txt);
        });
    }

    public void setSignInButton(String txt) {
        Platform.runLater(() -> {
            this.signInButton.setText(txt);
        });
    }

    public void setUrlField(String txt) {
        Platform.runLater(() -> {
            this.urlField.setPromptText(txt);
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
