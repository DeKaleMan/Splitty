package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.inject.Inject;
import java.util.Scanner;

public class InvitationCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private String inviteCode;
    private String eventTitle;

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
        mainCtrl.showSplittyOverview(eventTitle);
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

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
