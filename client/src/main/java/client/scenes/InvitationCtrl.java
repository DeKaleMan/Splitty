package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import javax.inject.Inject;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    private final SettingsCtrl settingsCtrl;

    private final Config config;
    private String inviteCode;
    private int eventCode;

    private String titleEvent;

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
    private Label errorNoValidEmail;
    @FXML
    private Label defaultLabel;
    @FXML
    private Button defaultButton;

    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, SettingsCtrl settingsCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
        this.settingsCtrl = settingsCtrl;
        inviteCode = "testInviteCode";
    }

    public void setDefaultButton(String txt){
        defaultButton.setText(txt);
    }

    public void setDefaultLabel(String txt){
        defaultLabel.setText(txt);
    }

    public void setNoEmail(String txt){
        noEmail.setText(txt);
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
        if (!(scanner.hasNext())){
            emailArea.setText("Please fill in the emails of all your \n" +
                    "friends you want to \ninvite to event: " + titleEvent);
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
            System.out.println("did not work");
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
        this.titleEvent = title;
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
        String emailTo = "";
        String emailSubject = "Good new, you are invited!";
        String emailBody = emailBody();
        String fromEmail = config.getEmail();
        boolean isValid = isValidEmail(emailT);
        if (isValid == false){
            errorNoValidEmail.setVisible(true);
            new Thread(() -> {
                try {
                    Thread.sleep(6000); // Sleep for 6 seconds
                    errorNoValidEmail.setVisible(false); // Hide the error message after 6 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("email is not valid");
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
        String passwordToken = config.getEmailToken();
        Mailer mailer = Mail.getSenderInfo(host, port, usernameEmail, passwordToken);
        return mailer;
    }


    public String emailBody(){
        String name = config.getName();
        String eventName = titleEvent;
        String server = serverURL;
        String res = "Dear reader,\n \n" + "Your friend "
                + name + " would like to invite you to the "
                + eventName + " event. " + "He is thrilled to welcome " +
                "you to the event. Here are the details:\n \n"
                + "Event: " + eventName + "\n" +
                "Server: " + server + "\n" +
                "InviteCode: " + inviteCode + "\n\n" +
                "You can join the event easily with the inviteCode. \n \n" +
                "By joining the event you never have to worry that you pay to much for " +
                "any occasion, and make sure that you can fully enjoy the moment with you friends \n \n" +
                "sincerly, \n" +
                "Team Splitty";
        return res;
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

    public void checkDefault(){
        settingsCtrl.sendDefaultEmail();
        emailArea.setText("configure email has been send to \n" +
                "yourself");
    }



}
