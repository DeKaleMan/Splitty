package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class ContactDetailsCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    public Button abortButton;

    @Inject
    public ContactDetailsCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        mainCtrl.setButtonRedProperty(abortButton);
    }
}
