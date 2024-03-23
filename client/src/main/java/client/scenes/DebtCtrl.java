package client.scenes;

import client.utils.ServerUtils;

import commons.Payment;
import commons.dto.PaymentDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class DebtCtrl implements Initializable {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    @FXML
    private ListView paymentInstructionListView;
    @FXML
    private Label titlelabel;

    @FXML
    private Button undo;

    private Payment undone;

    private List<Payment> changed;

    private ObservableList<Payment> payments;

    @Inject
    public DebtCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        changed = new ArrayList<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO make eventCode not hardcoded
        payments = FXCollections.observableArrayList(serverUtils.getPaymentsOfEvent(1).stream().filter(x -> !x.isPaid()).toList());
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
                                HBox hBox = gethBox(payment);
                                VBox info = generateInfo(payment);
                                TitledPane pane = new TitledPane("", info);
                                pane.setGraphic(hBox);
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
        HBox emailInfo = new HBox();
        if(payment.getPayee().getEmail() == null) emailInfo.getChildren().add(new Text("No email specified for this participant.\n"));
        else{
            Button sendMessage = new Button("Send reminder");
            sendMessage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sendMessage(payment);
                }
            });
            emailInfo.getChildren().addAll(new Text("Email available: "), sendMessage);
        }
        info.getChildren().add(emailInfo);
        if(payment.getPayee().getIBan() != null && payment.getPayee().getBIC() != null){
            info.getChildren().add(new Text("Banking info available:\n" +
                "IBAN: " + payment.getPayee().getIBan() + "\nBIC: " + payment.getPayee().getBIC()));
        }else{
            info.getChildren().add(new Text("Incomplete or no banking info available"));
        }
        return info;
    }

    private HBox gethBox(Payment payment) {
        String title = payment.getPayer().getName() + " pays " +
            payment.getPayee().getName() + " " +
            payment.getAmount();
        Button received = new Button("Mark received");
        received.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                markReceived(received);
            }
        });
        HBox hBox = new HBox(new Text(title), received);
        return hBox;
    }

    public void setDebtList() {
        //search for a query

    }

    @FXML
    public void back() {
        persistPayments(changed);
        mainCtrl.showSplittyOverview(titlelabel.getText());
    }

    public void markReceived(Button button) throws NoSuchElementException, IndexOutOfBoundsException {
            Payment p = ((ListCell<Payment>) button.getParent().getParent()
                .getParent().getParent()).getItem();
            System.out.println("removed " + p.getId());
            markAsPaid(p);
            undone = p;
            undo.setVisible(true);
    }

    private void persistPayments(List<Payment> payments){
        for(Payment p : payments){
            //TODO make eventCode not hardcoded
            serverUtils.updatePayment(new PaymentDTO(p.getPayer().getUuid(), p.getPayee().getUuid(), 1, p.getAmount(), p.isPaid()), p.getId());
        }
    }


    public void setTitlelabel(String title) {
        titlelabel.setText((title));
    }


    /**
     * removes a debt from the list and the database
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

    }

    @FXML
    public void undo() {
        changed.remove(undone);
        undone.setPaid(false);
        this.paymentInstructionListView.getItems().add(undone);
        undo.setVisible(false);
    }
}
