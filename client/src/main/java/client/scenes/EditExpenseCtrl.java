package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Expense;
import commons.Participant;
<<<<<<< HEAD
import commons.Type;
=======
import commons.Tag;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
>>>>>>> cf8a494da76c0b46f34a7d66a5119294485b1b9d
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
            List<Participant> participants = new ArrayList<>();
            if(isSharedExpense) participants.addAll(owing);
            else participants.add(receiver);
            mainCtrl.updateOverviewUndoStacks(expense,
                serverUtils.getDebtByExpense(eventId,expense.getExpenseId()), "edit");
            mainCtrl.editExpense(expense.getExpenseId(), description, tag, date, amountDouble,
                oldPayer, eventId, isSharedExpense, participants);
            mainCtrl.showUndoInOverview();
            back();
        } catch (Exception e) {
            commitExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(event1 -> commitExpenseError.setVisible(false));
            visiblePause.play();
        }
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
            }
            owing.addAll(owingFromDb);
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
