package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditExpenseCtrl extends ExpenseCtrl {
    public Button addTagButton;
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
    protected ComboBox<Tag> category;

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

    private Expense expense;


    @Inject
    public EditExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        super(serverUtils, mainCtrl, config);
    }

    @FXML
    @Transactional
    public void editExpense() {
        Date date = getDate();
        if(date == null) return;

        Tag tag = getTag();
        Participant oldPayer = personComboBox.getValue();
        if (oldPayer == null) {
            payerError.setVisible(true);
            return;
        }

        Double amountDouble = getAmountDouble(date);
        if (amountDouble == null) return;

        Participant receiver = receiverListView.getSelectionModel().getSelectedItem();
        if(!isSharedExpense && receiver == null) {
            return;
        }


        String description = whatFor.getText();

        try {
            //add to database
            ExpenseDTO
                exp =
                new ExpenseDTO(eventId, description, tag, date, amountDouble,
                    payer.getUuid(),isSharedExpense);
            Expense editedExpense = serverUtils.updateExpense(expense.getExpenseId(), exp);
            if(isSharedExpense) editSharedExpense(editedExpense, oldPayer, amountDouble);
            else editGivingMoneyToSomeone(editedExpense, oldPayer, amountDouble, receiver);
            serverUtils.generatePaymentsForEvent(eventId);
            back();
        } catch (Exception e) {
            commitExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(event1 -> commitExpenseError.setVisible(false));
            visiblePause.play();
        }
    }

    private void editGivingMoneyToSomeone(Expense editedExpense, Participant oldPayer,
                                          double amountDouble, Participant receiver) {
        Participant newReceiver = serverUtils.getParticipant(
            receiver.getUuid(), receiver.getEvent().getId());
        serverUtils.saveDebt(
            new DebtDTO(-amountDouble, eventId, editedExpense.getExpenseId(),
                newReceiver.getUuid()));
        serverUtils.updateParticipant(newReceiver.getUuid(),
            new ParticipantDTO(newReceiver.getName(),
                newReceiver.getBalance() - amountDouble, newReceiver.getIBan(),
                newReceiver.getBIC(), newReceiver.getEmail(), newReceiver.getAccountHolder(),
                newReceiver.getEvent().getId(),
                newReceiver.getUuid()));
        Participant newPayer = serverUtils.getParticipant(
            oldPayer.getUuid(), oldPayer.getEvent().getId());
        serverUtils.saveDebt(
            new DebtDTO(amountDouble, eventId, editedExpense.getExpenseId(),
                newPayer.getUuid()));
        serverUtils.updateParticipant(newPayer.getUuid(),
            new ParticipantDTO(newPayer.getName(),
                newPayer.getBalance() + amountDouble, newPayer.getIBan(),
                newPayer.getBIC(), newPayer.getEmail(), newPayer.getAccountHolder(),
                newPayer.getEvent().getId(),
                newPayer.getUuid()));
    }

    private void editSharedExpense(Expense editedExpense,
                                   Participant oldPayer, double amountDouble) {
        double amountPerPerson = editedExpense.getTotalExpense() / (owing.size()+1);
        for (Participant oldP : owing) {
            Participant p = serverUtils.getParticipant(
                oldP.getUuid(), oldP.getEvent().getId());
            serverUtils.saveDebt(
                new DebtDTO(-amountPerPerson, eventId, editedExpense.getExpenseId(),
                    p.getUuid()));
            serverUtils.updateParticipant(p.getUuid(),
                new ParticipantDTO(p.getName(), p.getBalance() - amountPerPerson
                    , p.getIBan(),
                    p.getBIC(), p.getEmail(), p.getAccountHolder(), p.getEvent().getId(),
                    p.getUuid()));
        }
        Participant newPayer = serverUtils.getParticipant(
            oldPayer.getUuid(), oldPayer.getEvent().getId());
        serverUtils.saveDebt(
            new DebtDTO(amountDouble - amountPerPerson,
                eventId, editedExpense.getExpenseId(), newPayer.getUuid()));
        serverUtils.updateParticipant(newPayer.getUuid(),
            new ParticipantDTO(newPayer.getName(),
                newPayer.getBalance() + amountDouble - amountPerPerson, newPayer.getIBan(),
                newPayer.getBIC(), newPayer.getEmail(), newPayer.getAccountHolder(),
                newPayer.getEvent().getId(),
                newPayer.getUuid()));
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
                            if(owing.contains(participant)) button.setSelected(true);
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

    public void refresh(Expense expense){
        this.eventId = expense.getEvent().getId();
        isSharedExpense = expense.isSharedExpense();
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        try {
            allparticipants = serverUtils.getParticipants(eventId);
        } catch (Exception e) {
            allparticipants = new ArrayList<>();
        }
        list.addAll(allparticipants);
        setup(list);
        this.expense = expense;
        owing.clear();
        List<Participant> owingFromDb = serverUtils.getDebtByExpense(expense.getEvent().getId(),
            expense.getExpenseId()).stream().filter(x -> x.getBalance() < 0)
            .map(x -> x.getParticipant()).toList();
        personComboBox.setValue(expense.getPayer());
        dateSelect.setValue(expense.getDate().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate());
        currencyComboBox.setValue(config.getCurrency());
        whatFor.setText(expense.getDescription());
        category.setValue(expense.getTag());
        double totalExpense = expense.getTotalExpense();
        if(config.getCurrency() != commons.Currency.EUR) totalExpense = mainCtrl.getAmountInDifferentCurrency(
            Currency.EUR,
            config.getCurrency(), expense.getDate(), totalExpense);
        amount.setText(mainCtrl.getFormattedDoubleString(totalExpense));
        payer = expense.getPayer();
        rest.clear();
        rest.addAll(allparticipants);
        rest.remove(payer);
        if(isSharedExpense) {
            sharedExpense.setSelected(true);
            if (owingFromDb.size() == rest.size()) {
                selectAll.setSelected(true);
            } else {
                selectSome.setSelected(true);
                owing.addAll(owingFromDb);
            }
        }else{
            givingMoneyToSomeone.setSelected(true);
            Participant receiver = owingFromDb.getFirst();
            receiverListView.getSelectionModel().select(receiver);
        }
    }

    private void setup(ObservableList<Participant> list) {
        setCategoriesUp();
        setCurrencyUp();
        setComboboxUp(list);
        setListViewsUp();
        setTogglesUp();
    }

    public void showManageTags() {
        mainCtrl.showManageTags(eventId);
    }
}
