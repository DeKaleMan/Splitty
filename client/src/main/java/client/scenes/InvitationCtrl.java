package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.EmailType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.simplejavamail.api.email.Email;

import javax.inject.Inject;
import java.util.Scanner;

public class InvitationCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    private final Config config;
    private String inviteCode;
    private int eventCode;

    private String serverURL;

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

    @FXML
    private ComboBox<EmailType> typeEmail;

    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, Config config) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
        inviteCode = "testInviteCode";
    }

    public void initialize() {
        typeEmail.getItems().addAll(EmailType.values());
        typeEmail.setValue(EmailType.Default);
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

    public void setServer(String serverURL){
        this.serverURL = serverURL;
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
     * @param emailadress String email to send the invitation to
     */
    private void sendEmailInvitation(String emailadress){
        System.out.println("Invitation sent to: " + emailadress);
        Email email = getherDataForEmail(emailadress);
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

    public Email getherDataForEmail(String emailT){
        EmailType emailType = typeEmail.getValue();
        String emailSubject = emailSubject(emailType);
        String emailBody = emailBody(emailType);
        String fromEmail = config.getEmail();
        String emailTo = emailT;

        Email email = Mail.makeEmail(fromEmail, emailTo, emailSubject, emailBody);

        System.out.println("gethering information for making an email.");

        return email;
//        String serverConnection = ServerUtils.server;

    }

    public String emailBody(EmailType emailType){
        String emailBody = "";
        switch (emailType) {
            case EmailType.Default:
                return emailBody + "default email";
            case EmailType.Invitation:
                return emailBody + "invitation email";
            case EmailType.Reminder:
                return emailBody + "Reminder email";
            case EmailType.ReminderToPay:
                return emailBody + "reminder to pay";
        }
        return emailBody;
    }

    public String emailSubject(EmailType emailType){
        String emailSubject = "";
        switch (emailType) {
            case EmailType.Default:
                return emailSubject + "default email";
            case EmailType.Invitation:
                return emailSubject + "invitation email";
            case EmailType.Reminder:
                return emailSubject + "Reminder email";
            case EmailType.ReminderToPay:
                return emailSubject + "reminder to pay";
        }
        return emailSubject;
    }



}
