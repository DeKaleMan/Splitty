package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.*;
import commons.Currency;
import javafx.animation.PauseTransition;

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
    protected ComboBox<Tag> category;
    @FXML
    public Button addTagButton;

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

    public Expense getExpense() {
        Date date = getDate();
        boolean error;
        error = date == null;
        Tag tag = getTag();
        Participant payer = personComboBox.getValue();
        if (payer == null) {
            payerError.setVisible(true);
            error = true;
        }
        Double amountDouble = getAmountDouble(date);
        if (amountDouble == null) {
            expenseLoading.setVisible(false);
            error = true;
        }
        Participant receiver = receiverListView.getSelectionModel().getSelectedItem();
        if(!isSharedExpense && receiver == null) {
            //TODO handle invalid receiver
            expenseLoading.setVisible(false);
            error = true;
        }
        String description = whatFor.getText();
        if (error) {
            expenseLoading.setVisible(false);
            return null;
        }
        List<Participant> participants = new ArrayList<>();
        if(isSharedExpense) participants.addAll(owing);
        else participants.add(receiver);
        return mainCtrl.addExpense(description, tag, date, amountDouble, payer,
                eventId, isSharedExpense, participants);
    }

    @FXML
    public void addExpense() {
        expenseLoading.setVisible(true);
        new Thread(() -> {
            try {
                //add to database
                Expense e = getExpense();
                serverUtils.generatePaymentsForEvent(eventId);
                mainCtrl.updateOverviewUndoStacks(e, new ArrayList<>(), "add");
                mainCtrl.showUndoInOverview();
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
        this.eventId = eventCode;
        splitList.setVisible(false);
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
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
        tagRefresh();
        currencyComboBox.setValue(Currency.EUR);
        personComboBox.setValue(null);
        amount.setText("");
    }

    private void tagRefresh() {
        setTagsUp();
        category.setValue(null);
    }

    public void showManageTags() {
        mainCtrl.showManageTags(eventId, true, null);
    }

}
