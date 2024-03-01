package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javax.inject.Inject;

public class ManageParticipantsCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private Label titleLabel;

    @FXML
    private Button sendInvites;

    @Inject
    public ManageParticipantsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }


    public void backEventOverview(){
        mainCtrl.showSplittyOverview(titleLabel.getText());
    }
    /**
     * removes the participant from the list
     */
    public void removeParticipantFromList(Participant p){
        String name = p.getName();
    }
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

}
