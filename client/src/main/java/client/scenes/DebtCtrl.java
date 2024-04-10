package client.scenes;

import ch.qos.logback.core.encoder.EchoEncoder;
import client.utils.Config;
import client.utils.ServerUtils;

import commons.Currency;
import commons.Participant;
import commons.Payment;
import commons.dto.ParticipantDTO;
import commons.dto.PaymentDTO;
import jakarta.mail.AuthenticationFailedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import javax.inject.Inject;
import javax.naming.AuthenticationException;
import java.net.URL;
import java.util.*;

public class DebtCtrl implements Initializable {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    private Config config;

    private String eventName;

    private Mail mail;

    @FXML
    private ListView<Payment> paymentInstructionListView;
    @FXML
    private Label titlelabel;

    @FXML
    private Button undo;
    @FXML
    private Label labelWrong;

    private Stack<Payment> undone;

    private List<Payment> changed;

    private ObservableList<Payment> payments;

    private int eventCode;

    @Inject
    public DebtCtrl(ServerUtils server, MainCtrl mainCtrl, Config config) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
    }

    public void injectMail(Mail mail){
        this.mail = mail;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mainCtrl.setButtonRedProperty(undo);

        payments = FXCollections.observableArrayList();
        this.paymentInstructionListView.setItems(payments);
        paymentInstructionListView.setCellFactory(
            new Callback<ListView<Payment>, ListCell<Payment>>() {
                @Override
                public ListCell call(ListView listView) {
                    return new ListCell<Payment>() {
                        @Override
                        protected void updateItem(Payment payment, boolean b) {
                            super.updateItem(payment, b);

                            if (payment == null || b) {
                                setStyle("-fx-background-color: #f4f4f4; -fx-padding: 0");
                                setGraphic(null);
                            } else {
                                GridPane headerGrid = getGrid(payment);
                                VBox info = generateInfo(payment);
                                TitledPane pane = new TitledPane("", info);
                                headerGrid.minWidthProperty().bind(pane.widthProperty());
                                headerGrid.setPadding(new Insets(0,10,0,35));
                                pane.setGraphic(headerGrid);
                                pane.getStyleClass().add("paymentInstruction");
                                pane.setExpanded(false);
                                setStyle("-fx-background-color: #f4f4f4; -fx-padding: 0");
                                setGraphic(pane);
                            }
                        }
                    };
                }
            });
        undo.setVisible(false);
    }

    private VBox generateInfo(Payment payment) {
        VBox info = new VBox();
        GridPane emailInfo = new GridPane();
        if (payment.getPayee().getEmail() == null)
            emailInfo.add(new Text(mainCtrl.translate("No email specified for this participant.") + "\n"), 0, 0);
        else {
            Button sendMessage = new Button(mainCtrl.translate("Send reminder"));
            sendMessage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sendMessage(payment);
                }
            });
            emailInfo.add(new Text(mainCtrl.translate("Email available: ")), 0, 0);
            emailInfo.add(sendMessage, 1, 0);
            emailInfo.setHgap(10.0);
        }
        info.getChildren().add(emailInfo);
        if (payment.getPayee().getIBan() != null && payment.getPayee().getBIC() != null &&
            payment.getPayee().getAccountHolder() != null) {
            info.getChildren().add(new Text(mainCtrl.translate("Banking info available") +
                "\n\t" + mainCtrl.translate("Account Holder:") + " " + payment.getPayee().getAccountHolder() +
                "\n\t" + mainCtrl.translate("IBAN:") + " " + payment.getPayee().getIBan() +
                "\n\t" + mainCtrl.translate("BIC:") + " " + payment.getPayee().getBIC()));
        } else {
            info.getChildren().add(new Text(mainCtrl.translate("Incomplete or no banking info available")));
        }
        info.setSpacing(2);
        return info;
    }

    private GridPane getGrid(Payment payment) {
        double amount = payment.getAmount();
        try {
            amount = mainCtrl.getAmountInDifferentCurrency(Currency.EUR,
                config.getCurrency(), new Date(), amount);
        }catch (RuntimeException e){
            GridPane errorGrid = new GridPane();
            errorGrid.add(new Text(e.getMessage()),0,0);
            return errorGrid;
        }

        String pays = " " + mainCtrl.translate("pays") + " ";
        String markReceived = mainCtrl.translate("Mark received");
        String title = payment.getPayer().getName() + pays
                + payment.getPayee().getName()
                + " "
                + mainCtrl.getFormattedDoubleString(amount)
                + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol();
        Button received = new Button(markReceived);
        received.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                markReceived(received);
            }
        });
        received.getStyleClass().add("receivedButton");
        Label titleNode = new Label(title);
        titleNode.setStyle("-fx-text-fill: white; -fx-font-size: 12px");
        GridPane grid = new GridPane();
        grid.add(titleNode, 0, 0);
        Region fillRegion = new Region();
        fillRegion.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(fillRegion, Priority.ALWAYS);
        grid.add(fillRegion,1,0);
        if (payment.getPayee().getUuid().equals(config.getId())) grid.add(received, 2, 0);
        grid.getStyleClass().add("headerGrid");
        grid.setHgap(10.0);
        return grid;
    }

    @FXML
    public void back() {
        undo.setVisible(false);
        persistPayments(changed);
        mainCtrl.showSplittyOverview(eventCode);
    }

    public void markReceived(Button button)
        throws NoSuchElementException, IndexOutOfBoundsException {
        Payment p = ((ListCell<Payment>) button.getParent().getParent()
            .getParent().getParent()).getItem();
        System.out.println("removed " + p.getId());
        markAsPaid(p);
        undone.push(p);
        undo.setVisible(true);
    }

    private void persistPayments(List<Payment> payments) {
        for (Payment p : payments) {
            serverUtils.updatePayment(
                new PaymentDTO(p.getPayer().getUuid(), p.getPayee().getUuid(), eventCode,
                    p.getAmount(),
                    p.isPaid()), p.getId());
            Participant payer = p.getPayer();
            Participant payee = p.getPayee();
            if(p.isPaid()){
                serverUtils.updateParticipant(payer.getUuid(),
                    new ParticipantDTO(payer.getName(),payer.getBalance() + p.getAmount(),
                        payer.getIBan(),payer.getBIC(),payer.getEmail(),payer.getAccountHolder(),
                        payer.getEvent().getId(),payer.getUuid()));
                serverUtils.updateParticipant(payee.getUuid(),
                    new ParticipantDTO(payee.getName(),payee.getBalance() - p.getAmount(),
                        payee.getIBan(),payee.getBIC(),payee.getEmail(),payee.getAccountHolder(),
                        payee.getEvent().getId(),payee.getUuid()));
            }
        }
    }

    public void refresh(int eventCode) {
        this.eventCode = eventCode;
        payments.clear();
        payments.addAll(
            serverUtils.getPaymentsOfEvent(eventCode).stream().filter(x -> !x.isPaid()).toList());
        undone = new Stack<>();
        changed = new ArrayList<>();
    }

    public void setTitlelabel(String title) {
        titlelabel.setText((title));
    }


    /**
     * removes a Payment from the list and adds it to changed
     *
     * @param p
     */
    public void markAsPaid(Payment p) {
        p.setPaid(true);
        changed.add(p);
        this.paymentInstructionListView.getItems().remove(p);
    }
    @FXML
    public void sendMessage(Payment payment) {
        try {
            System.out.println("Test if this works !!!!");
            String host = "smtp.gmail.com";
            int port = 587;
            String fromEmail = config.getEmail();
            String passwordToken = config.getEmailToken();

            if (fromEmail == null || fromEmail.isEmpty() || passwordToken == null || passwordToken.isEmpty()){
                labelWrong.setVisible(true);
                return;
            }
            Mailer mailer = mail.getSenderInfo(host, port, fromEmail, passwordToken);

            if (!performAuthenticationCheck(fromEmail, passwordToken)) {
                System.out.println("Error: Authentication failed. Please check your email and password token.");
                labelWrong.setText("Authentication failed. Please check your email and password token.");
                labelWrong.setVisible(true);
                return;
            }

            String toEmail = payment.getPayee().getEmail();
            String subject = "reminder to pay";
            String body = makeBody(payment);
            Email email = mail.makeEmail(fromEmail, toEmail, subject, body);

            mail.mailSending(email, mailer);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean performAuthenticationCheck(String fromEmail, String passwordToken) {
        String format = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!fromEmail.matches(format)) {
            System.out.println("Error: Invalid email format.");
            return false;
        }

        // Validate password length and format
        if (!passwordToken.matches("[a-z]+") || passwordToken.length() < 16) {
            System.out.println("Error: Password should be a 12-letter lowercase password.");
            return false;
        }

        try {
            String host = "smtp.gmail.com";
            int port = 587;
            Mailer mailer = mail.getSenderInfo(host, port, fromEmail, passwordToken);

            // Attempt to authenticate by sending a test email
            Email testEmail = mail.makeEmail(fromEmail, fromEmail, "Test Subject", "Test Body");
            mailer.sendMail(testEmail);

            // If no exception is thrown during the sendMail operation, authentication is successful
            return true;
        } catch (Exception e) {
            // Authentication failed
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void undo() {
        Payment p = undone.pop();
        changed.remove(p);
        p.setPaid(false);
        this.paymentInstructionListView.getItems().add(p);
        if (undone.isEmpty()) undo.setVisible(false);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }

    public String makeBody(Payment payment){
        String nameEmailReveiver = payment.getPayee().getName();
        String nameEvent =payment.getPayer().getEvent().getName();
        String nameEmailSender = payment.getPayer().getName();
        double balance = payment.getPayee().getBalance();
        String s = "Dear " + nameEmailReveiver + "\n\n" +
                "We would like to remind you that you still have an open debt in splitty event " + nameEvent +
                ". You owe " + nameEmailSender + " " + balance + ". \n \n" +
                "If you would like to leave the event, you first have to pay back all your debts. \n\n" +
                "sincerly, Team splitty";


        return s;
    }
}
