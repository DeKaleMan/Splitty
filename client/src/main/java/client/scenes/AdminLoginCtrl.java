package client.scenes;

import client.utils.AdminServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
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
    private Text signIn;
    @FXML
    private Text instruction;
    @FXML
    private Text passwordInstructionLink;

    @FXML
    private Button signInButton;


    @FXML
    private TextField urlField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordInstructionsText;

    @FXML
    public void adminSignIn(ActionEvent actionEvent) {
        String serverUrl = urlField.getText();
        String password = passwordField.getText();

        Response response = adminServerUtils.validatePassword(password, serverUrl);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            mainCtrl.showAdminOverview();
        } else {
            System.out.println("incorrect password: " + password);
        }


    }

    @FXML
    public void showPasswordInstructions(MouseEvent mouseEvent) {
        passwordInstructionsText.setVisible(true);
    }

    public void setSignIn(String txt) {
        this.signIn.setText(txt);
    }

    public void setInstruction(String txt) {
        this.instruction.setText(txt);
    }

    public void setPasswordInstructionLink(String txt) {
        this.passwordInstructionLink.setText(txt);
    }

    public void setSignInButton(String txt) {
        this.signInButton.setText(txt);
    }

    public void setUrlField(String txt) {
        this.urlField.setPromptText(txt);
    }

    public void setPasswordField(String txt) {
        this.passwordField.setPromptText(txt);
    }

    public void setPasswordInstructionsText(String txt) {
        this.passwordInstructionsText.setText(txt);
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
}
