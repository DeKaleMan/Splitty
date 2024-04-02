package client.scenes;

import client.Main;
import client.utils.Config;
import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;

public class ServerCtrl {
    public Button connectButton;
    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    private Config config;
    @FXML
    public Label startupNotification;
    @FXML
    public Label notConnectedError;
    @FXML
    public ImageView imageView;
    @FXML
    public TextField serverField;

    @Inject
    public ServerCtrl(MainCtrl mainCtrl, Config config) {
        this.mainCtrl = mainCtrl;
        this.config = config;
    }


    public void setField(boolean startup) {

        if (config.getConnection() != null) {
            serverField.setText(config.getConnection());
        }
        if (startup) {
            startupNotification.setVisible(true);
        } else {
            startupNotification.setVisible(false);
        }
    }

    public void connect() {
        notConnectedError.setVisible(false);
        try {
            ServerUtils.serverDomain = serverField.getText();
            ServerUtils.resetServer();
            serverUtils = new ServerUtils();

        } catch (RuntimeException e) {
            e.printStackTrace();
            notConnectedError.setVisible(true);
        }
    }

    public void resetError(KeyEvent keyEvent) {
        notConnectedError.setVisible(false);
    }
}
