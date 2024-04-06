package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.util.Scanner;

public class InvitationCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private String inviteCode;
    private int eventCode;

    @FXML
    private Label titleLabel;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private Label inviteCodeText;
    @FXML
    private Label inviteCodeInstructions;
    @FXML
    private Label sendEmailInvitesText;
    @FXML
    private Button back;

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

    public void initialize() {
        mainCtrl.setButtonGreenProperty(sendInvites);
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
        mainCtrl.showSplittyOverview(eventCode);
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
        mainCtrl.showSplittyOverview(mainCtrl.getCurrentEventCode());
    }


    /**
     * Sets the title of the event
     * @param title event's title
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }
    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }
    public void setInviteCode(String inviteCode){
        this.inviteCode = inviteCode;
    }

//    /**
//     * Just retains the event's title
//     * @param eventTitle title of the event
//     */
//    public void setEventTitle(String eventTitle) {
//        this.eventTitle = eventTitle;
//    }



    public void setInviteCodeText(String txt) {
        this.inviteCodeText.setText(txt);
    }

    public void setInviteCodeInstructions(String txt) {
        this.inviteCodeInstructions.setText(txt);
    }

    public void setSendEmailInvitesText(String txt) {
        this.sendEmailInvitesText.setText(txt);
    }

    public void setBack(String txt) {
        this.back.setText(txt);
    }

    public void setSendInvites(String txt) {
        this.sendInvites.setText(txt);
    }

    public void setEmailArea(String txt) {
        this.emailArea.setPromptText(txt);
    }
}
