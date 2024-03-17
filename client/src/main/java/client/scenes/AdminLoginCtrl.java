package client.scenes;

import client.utils.AdminServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AdminLoginCtrl {

    private MainCtrl mainCtrl;
    private AdminServerUtils adminServerUtils;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl, AdminServerUtils adminServerUtils) {
        this.mainCtrl = mainCtrl;
        this.adminServerUtils = adminServerUtils;
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
}
