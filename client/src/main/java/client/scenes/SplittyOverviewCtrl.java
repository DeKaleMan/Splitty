package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class SplittyOverviewCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @FXML
    private Button sendInvites;

    @FXML
    private Label titleLabel;
    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    public void sendInvitesOnClick(){
        mainCtrl.showInvitation(titleLabel.getText());
    }

    public void setTitle(String title){
        titleLabel.setText(title);
    }

}

