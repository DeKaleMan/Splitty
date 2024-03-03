package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

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
    }

    @FXML
    public void showPasswordInstructions(MouseEvent mouseEvent) {
        passwordInstructionsText.setVisible(true);
    }
}
