package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Expense;
import commons.Participant;
import commons.Type;
import javafx.animation.PauseTransition;


import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.*;


public class AddExpenseCtrl extends ExpenseCtrl implements Initializable {
    @FXML
    protected ComboBox<Participant> personComboBox;
    @FXML
    protected Label payerError;

    //all the things needed for the addExpense
    @FXML
    protected TextArea whatFor;
    @FXML
    protected Label amountError;

    @FXML
    protected DatePicker dateSelect;
    @FXML
    protected Label dateInvalidError;

    @FXML
    protected ListView<Participant> splitList;

    @FXML
    protected TextField amount;

    @FXML
    protected ComboBox<Type> category;

    @FXML
    protected Label sceneTypeText;
    @FXML
    protected Label whoPaid;
    @FXML
    protected Label howMuch;
    @FXML
    protected Label when;
    @FXML
    protected Label howToSplit;
    @FXML
    protected Label description;
    @FXML
    protected Label expenseTypetext;

    @FXML
    protected Button commit;
    @FXML
    protected Label commitExpenseError;
    @FXML
    protected Button cancel;
    @FXML
    protected RadioButton selectAll;
    @FXML
    protected RadioButton selectSome;

    @FXML
    protected RadioButton sharedExpense;

    @FXML
    protected RadioButton givingMoneyToSomeone;

    @FXML
    protected ToggleGroup selectionToggles;

    @FXML
    protected HBox receiverHBox;

    @FXML
    protected ListView<Participant> receiverListView;

    @FXML
    protected ComboBox<Currency> currencyComboBox;

    @FXML
    protected ProgressIndicator expenseLoading;

    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        super(serverUtils, mainCtrl, config);
    }


    @FXML
    public void addExpense() {
        expenseLoading.setVisible(true);
        new Thread(() -> {
            Date date = getDate();
            if(date == null) {
                expenseLoading.setVisible(false);
                return;
            }

            Type type = getType();
            Participant payer = personComboBox.getValue();
            if (payer == null) {
                payerError.setVisible(true);
                expenseLoading.setVisible(false);
                return;
            }

            Double amountDouble = getAmountDouble(date);
            if (amountDouble == null) {
                expenseLoading.setVisible(false);
                return;
            }

            Participant receiver = receiverListView.getSelectionModel().getSelectedItem();
            if(!isSharedExpense && receiver == null) {
                //TODO handle invalid receiver
                expenseLoading.setVisible(false);
                return;
            }
            String description = whatFor.getText();
            try {
                //add to database
                ExpenseDTO exp =
                        new ExpenseDTO(eventCode,description,type, date, amountDouble, payer.getUuid(),isSharedExpense);
                Expense expense = serverUtils.addExpense(exp);
                serverUtils.send("/app/addExpense", exp);
                if(isSharedExpense) addSharedExpense(amountDouble, expense, payer);
                else addGivingMoneyToSomeone(amountDouble, expense, payer, receiver);
                serverUtils.generatePaymentsForEvent(eventCode);
                expenseLoading.setVisible(false);
                back();
            } catch (Exception e) {
                commitExpenseError.setVisible(true);
                expenseLoading.setVisible(false);
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
                visiblePause.setOnFinished(event1 -> commitExpenseError.setVisible(false));
                visiblePause.play();
            }
        }).start();
    }


    private void addGivingMoneyToSomeone(double amountDouble, Expense expense, Participant payer,
                                         Participant receiver) {
        serverUtils.saveDebt(
            new DebtDTO(-amountDouble, eventCode, expense.getExpenseId(), receiver.getUuid()));
        serverUtils.updateParticipant(receiver.getUuid(),
            new ParticipantDTO(receiver.getName(), receiver.getBalance() - amountDouble, receiver.getIBan(),
                receiver.getBIC(), receiver.getEmail(), receiver.getAccountHolder(), receiver.getEvent().getId(),
                receiver.getUuid()));
        serverUtils.saveDebt(
            new DebtDTO(amountDouble, eventCode, expense.getExpenseId(), payer.getUuid()));
        serverUtils.updateParticipant(payer.getUuid(),
            new ParticipantDTO(payer.getName(), payer.getBalance() + amountDouble, payer.getIBan(),
                payer.getBIC(), payer.getEmail(), payer.getAccountHolder(), payer.getEvent().getId(),
                payer.getUuid()));
    }

    private void addSharedExpense(double amountDouble, Expense expense, Participant payer) {
        double amountPerPerson = amountDouble / (owing.size()+1);
        for (Participant p : owing) {
            serverUtils.saveDebt(
                new DebtDTO(-amountPerPerson, eventCode, expense.getExpenseId(), p.getUuid()));
            serverUtils.updateParticipant(p.getUuid(),
                new ParticipantDTO(p.getName(), p.getBalance() - amountPerPerson, p.getIBan(),
                    p.getBIC(), p.getEmail(), p.getAccountHolder(), p.getEvent().getId(),
                    p.getUuid()));
        }
        serverUtils.saveDebt(
            new DebtDTO(amountDouble - amountPerPerson, eventCode, expense.getExpenseId(), payer.getUuid()));
        serverUtils.updateParticipant(payer.getUuid(),
            new ParticipantDTO(payer.getName(), payer.getBalance() + amountDouble - amountPerPerson, payer.getIBan(),
                payer.getBIC(), payer.getEmail(), payer.getAccountHolder(), payer.getEvent().getId(),
                payer.getUuid()));
    }


    void setSplitListUp() {
        splitList.setItems(rest);
        splitList.setCellFactory(new Callback<ListView<Participant>, ListCell<Participant>>() {
            @Override
            public ListCell<Participant> call(ListView listView) {
                return new ListCell<Participant>() {
                    @Override
                    protected void updateItem(Participant participant, boolean b) {
                        super.updateItem(participant, b);
                        if (participant == null || b) {
                            setGraphic(null);
                        } else {
                            RadioButton button = new RadioButton();
                            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(
                                    ObservableValue<? extends Boolean> observableValue,
                                    Boolean wasPreviouslySelected, Boolean isNowSelected) {
                                    if (isNowSelected) {
                                        owing.add(participant);
                                    } else {
                                        owing.remove(participant);
                                    }
                                }
                            });
                            button.setText(participant.getName());
                            setGraphic(button);
                        }
                    }
                };
            }
        });
    }


    public void refresh(int eventCode){
        this.eventCode = eventCode;
        splitList.setVisible(false);
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        serverUtils.registerForExpenseWS("/topic/addExpense", Expense.class, exp -> {
            System.out.println("expense added " + exp);
        });
        try {
            allparticipants = serverUtils.getParticipants(eventCode);
        } catch (Exception e) {
            allparticipants = new ArrayList<>();
        }
        list.addAll(allparticipants);
        setComboboxUp(list);
        payer = null;
        sharedExpense.setSelected(true);
        rest.clear();
        owing.clear();
        selectAll.setSelected(false);
        selectSome.setSelected(false);
        dateSelect.setValue(null);
        whatFor.setText("");
        category.setValue(null);
        currencyComboBox.setValue(Currency.EUR);
        personComboBox.setValue(null);
        amount.setText("");
    }

}
