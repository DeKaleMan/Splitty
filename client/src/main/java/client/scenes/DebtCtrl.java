package client.scenes;

import client.utils.Config;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    private ListView<Payment> paymentInstructionListView;
    @FXML
    private Label titlelabel;

    @FXML
    private Button undo;

    private Stack<Payment> undone;

    private List<Payment> changed;

    private ObservableList<Payment> payments;

    //TODO make eventCode not hardcoded
    private int eventCode = 1;

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
            emailInfo.add(new Text("No email specified for this participant.\n"), 0, 0);
        else {
            Button sendMessage = new Button("Send reminder");
            sendMessage.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    sendMessage(payment);
                }
            });
            emailInfo.add(new Text("Email available: "), 0, 0);
            emailInfo.add(sendMessage, 1, 0);
            emailInfo.setHgap(10.0);
        }
        info.getChildren().add(emailInfo);
        if (payment.getPayee().getIBan() != null && payment.getPayee().getBIC() != null &&
            payment.getPayee().getAccountHolder() != null) {
            info.getChildren().add(new Text("Banking info available" +
                "\n\tAccount Holder: " + payment.getPayee().getAccountHolder() +
                "\n\tIBAN: " + payment.getPayee().getIBan() +
                "\n\tBIC: " + payment.getPayee().getBIC()));
        } else {
            info.getChildren().add(new Text("Incomplete or no banking info available"));
        }
        info.setSpacing(2);
        return info;
    }

    private GridPane getGrid(Payment payment) {
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
        received.getStyleClass().add("receivedButton");
        Text titleNode = new Text(title);
        GridPane grid = new GridPane();
        grid.add(titleNode, 0, 0);
        if (payment.getPayee().getUuid().equals(config.getId())) grid.add(received, 1, 0);
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
        }
    }

    public void refresh() {
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
}
