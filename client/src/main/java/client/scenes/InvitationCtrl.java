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
import java.util.Scanner;

public class InvitationCtrl {

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

    public void showInviteCode() {
        inviteCodeLabel.setText(inviteCode);
    }

    public void sendInvitesOnClick() {
        System.out.println("Button clicked!!");
        readAndSendEmails();
        mainCtrl.showSplittyOverview();
    }

    private void readAndSendEmails() {
        Scanner scanner = new Scanner(emailArea.getText());
        while(scanner.hasNextLine()){
            sendEmailInvitation(scanner.nextLine());
        }
    }

    private void sendEmailInvitation(String email){
        System.out.println("Invitation sent to: " + email);
        /* TO DO */
    }
}
