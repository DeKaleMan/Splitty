package client.scenes;

import client.utils.ServerUtils;
import commons.Person;
import commons.Quote;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InvitationCtrl{

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private String inviteCode;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private Button sendInvites;

    @FXML
    private TextArea emailArea;

    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        inviteCode = "testInviteCode";
    }

    public void backToOverview(){
        inviteCodeLabel.setText(inviteCode);
        System.out.println("Button clicked!!");
    }
}
