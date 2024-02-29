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

    @FXML
    private Label titleLabel;

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

    /**
     * Displays the invite code in the associated label
     */
    public void showInviteCode() {
        inviteCodeLabel.setText(inviteCode);
    }

    /**
     * Sends invitations to specified emails and goes back to event's overview
     */
    public void sendInvitesOnClick() {
        System.out.println("Button clicked!!");
        readAndSendEmails();
        mainCtrl.showSplittyOverview(titleLabel.getText());
    }

    /**
     * Reads emails from the associated TextArea
     */
    private void readAndSendEmails() {
        Scanner scanner = new Scanner(emailArea.getText());
        while(scanner.hasNextLine()){
            sendEmailInvitation(scanner.nextLine());
        }
    }

    /**
     * Sends an invitation to the specified email
     * TO DO - the actual sending functionality
     * @param email String email to send the invitation to
     */
    private void sendEmailInvitation(String email){
        System.out.println("Invitation sent to: " + email);
        /* TO DO */
    }

    @FXML
    private void back() {
        System.out.println("going back to event overview");
        mainCtrl.showSplittyOverview(titleLabel.getText());
    }


    /**
     * Sets the title of the event
     * @param title event's title
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }

//    /**
//     * Just retains the event's title
//     * @param eventTitle title of the event
//     */
//    public void setEventTitle(String eventTitle) {
//        this.eventTitle = eventTitle;
//    }

}
