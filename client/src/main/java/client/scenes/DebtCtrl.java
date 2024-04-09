package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;

import commons.*;
import commons.Currency;
import commons.dto.PaymentDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class DebtCtrl implements Initializable {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    private Config config;
    @FXML
    AnchorPane background;

    @FXML
    private ListView<Payment> paymentInstructionListView;
    @FXML
    private Label titlelabel;

    @FXML
    private Button undo;

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
                                headerGrid.maxWidthProperty().bind(pane.widthProperty());
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
        titleNode.setMaxWidth(110);
        titleNode.setWrapText(true);
        GridPane grid = new GridPane();
        grid.add(titleNode, 0, 0);
        Region fillRegion = new Region();
        fillRegion.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(fillRegion, Priority.ALWAYS);
        grid.add(fillRegion,1,0);
        grid.add(received, 2, 0);
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
            Payment fromDB;
            try{
                fromDB = serverUtils.getPayment(p.getId());
            } catch (RuntimeException e){
                continue;
            }
            if(fromDB.isPaid() == p.isPaid()) continue;
            serverUtils.updatePayment(
                new PaymentDTO(p.getPayer().getUuid(), p.getPayee().getUuid(), eventCode,
                    p.getAmount(),
                    p.isPaid()), p.getId());
            Participant payer = serverUtils.getParticipant(p.getPayer().getUuid(), eventCode);
            Participant payee = serverUtils.getParticipant(p.getPayee().getUuid(), eventCode);
            if(p.isPaid()){
                mainCtrl.addExpense("Partial debt settling", Type.Other, new Date(),
                    p.getAmount(), payer, eventCode,false, List.of(payee));
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
        //TODO actually send a message
    }
    @FXML
    public void undo() {
        if(undone.isEmpty()) return;
        Payment p = undone.pop();
        changed.remove(p);
        p.setPaid(false);
        this.paymentInstructionListView.getItems().add(p);
        if (undone.isEmpty()) undo.setVisible(false);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }

        if(ctrlZ.match(press)){
            undo();
        }
    }
}
