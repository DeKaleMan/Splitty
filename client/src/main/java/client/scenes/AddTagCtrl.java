package client.scenes;

import client.utils.ServerUtils;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class AddTagCtrl {
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    private int eventId;

    @Inject
    public AddTagCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }


    public void cancel() {
        mainCtrl.showManageTags(eventId);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            cancel();
        }
    }

    public void setFields(int eventId) {
    }
    public void setFields(Tag tag, int eventId) {
    }

    public void addTag(ActionEvent actionEvent) {
    }
}
