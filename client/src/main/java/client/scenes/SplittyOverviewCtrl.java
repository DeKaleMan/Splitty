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

    /**
     * Shows the invitation scene (sends it the title to retain it)
     */
    public void sendInvitesOnClick(){
        mainCtrl.showInvitation(titleLabel.getText());
    }

    /**
     * Sets the title of the event
     * @param title event's title
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }


    @FXML
    public void showAddExpense() {
        mainCtrl.showAddExpense(titleLabel.getText());
    }

    /**
     * go back to Start screen
     */
    @FXML
    private void back() {
        mainCtrl.showStartScreen();
    }

}

