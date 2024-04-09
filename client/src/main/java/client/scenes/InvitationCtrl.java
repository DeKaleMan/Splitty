package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.EmailType;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import javax.inject.Inject;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Label noEmail;

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

    @FXML
    private Label errorNoValidEmail;

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
//        mainCtrl.showSplittyOverview(eventCode);
    }

    public void setServer(String serverURL){
        this.serverURL = serverURL;
    }

    /**
     * Reads emails from the associated TextArea
     */
    private void readAndSendEmails() {
        Scanner scanner = new Scanner(emailArea.getText());
        EmailType type = typeEmail.getValue();
        noEmail.setVisible(true);
        if (!(scanner.hasNext()) && !(type.equals(EmailType.Default))){
            noEmail.setVisible(true);
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));
            pauseTransition.setOnFinished(event -> noEmail.setVisible(false));
            pauseTransition.play();
        }
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
        Email email = getherDataForEmail(emailadress);
        Mailer mailInfo = getSenderInfo();
        try{
            Mail.mailSending(email, mailInfo);
            System.out.println("Invitation succesfully sent to: " + emailadress);
        } catch (Exception e){

        }


    }

    @FXML
    private void back() {
        System.out.println("going back to event overview");
        errorNoValidEmail.setVisible(false);
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
        String emailTo = "";
        if (emailType == EmailType.Default){
            emailTo = config.getEmail();
        }
        String emailSubject = emailSubject(emailType);
        String emailBody = emailBody(emailType);
        String fromEmail = config.getEmail();
        boolean isValid = isValidEmail(fromEmail);
        if (isValid == false){
            errorNoValidEmail.setVisible(true);
            throw new RuntimeException("email is not valid");
        }
        emailTo = emailT;

        Email email = Mail.makeEmail(fromEmail, emailTo, emailSubject, emailBody);

        System.out.println("gethering information for making an email.");

        return email;
//        String serverConnection = ServerUtils.server;

    }

    public Mailer getSenderInfo(){
        String host = "smtp.gmail.com";
        int port = 587;
        String usernameEmail = config.getEmail();
        String passwordToken = "txrobxvossaibwat";
        Mailer mailer = Mail.getSenderInfo(host, port, usernameEmail, passwordToken);
        return mailer;
    }



    public String emailBody(EmailType emailType){
        String defaultBody = "This is a default email to yourself " +
                "to check if it the credetials are right.";
        String invitationBody = "Hi, you are invited to an event in spliity." +
                " You can join with the invitecode: "
                + inviteCode + ". The server we run it on is: " + serverURL;
        String reminder = "You have received an email from me already. Please look at it.";
        String reminderToPay = "You have participated in an event. Please pay me back.";
        switch (emailType) {
            case EmailType.Default:
                return defaultBody;
            case EmailType.Invitation:
                return invitationBody;
            case EmailType.Reminder:
                return reminder;
            case EmailType.ReminderToPay:
                return reminderToPay;
        }
        return "Something went wrong please disregard this email.";
    }

    public String emailSubject(EmailType emailType){
        String defaultEmail = "";
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

    public boolean isValidEmail(String email){
        String emailformat = "^[a-zA-Z0-9_+&*-" +
                "]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:" +
                "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailformat);
        Matcher matcher = pattern.matcher(email);
        boolean isValid = matcher.matches();
        return isValid;
    }



}
