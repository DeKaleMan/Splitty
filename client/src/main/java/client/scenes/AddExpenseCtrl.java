package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class AddExpenseCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final int eventCode = 1;
    private final MainCtrl mainCtrl;
    private final SplittyOverviewCtrl splittyCtrl;


    @FXML
    public Label titleLabel;



    @FXML
    private ComboBox<Participant> personComboBox;
    @FXML
    public Label payerError;

    //all the things needed for the addExpense
    @FXML
    private TextArea whatFor;
    @FXML
    private Label amountInvalidError;
    @FXML
    public Label amountNegativeError;
    @FXML
    public Label noAmountError;

    @FXML
    private DatePicker dateSelect;
    @FXML
    private Label dateInvalidError;

    @FXML
    private ListView splitList;

    @FXML
    private TextField amount;

    @FXML
    private ComboBox category;
    private List<Participant> participant;

    @FXML
    private Label addExpenseText;
    @FXML
    private Label whoPaid;
    @FXML
    private Label howMuch;
    @FXML
    private Label when;
    @FXML
    private Label howToSplit;
    @FXML
    private Label description;
    @FXML
    private Label expenseTypetext;

    @FXML
    private Button back;
    @FXML
    private Button add;
    @FXML
    public Label addExpenseError;
    @FXML
    private Button cancel;
    @FXML
    private Button selectAll;


    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, SplittyOverviewCtrl splittyCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.splittyCtrl = splittyCtrl;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Participant person1 = new Participant("name", 1000.00, "iBAN", "bIC", "holder", "", "uuid1", new Event());
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        serverUtils.registerForExpenseWS("/topic/addExpense", Expense.class ,exp -> {
            System.out.println("expense added " + exp);
        });
        try {
            allparticipants = serverUtils.getParticipants(eventCode);
        } catch (RuntimeException e) {
            allparticipants = new ArrayList<>();
        }
        list.addAll(allparticipants);
        list.add(person1);
        personComboBox.setItems(list);
        personComboBox.setCellFactory(param -> new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        personComboBox.setButtonCell(new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        createSplitList(list);
        category.setCellFactory(param -> new ListCell<Type>() {
            @Override
            protected void updateItem(Type item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });this.category.setItems(FXCollections.observableArrayList(Type.Food, Type.Drinks, Type.Travel, Type.Other));
    }

    @FXML
    public void back() {
        mainCtrl.showSplittyOverview(eventCode);
    }

    /**
     * Sets the title of the event
     *
     * @param title event's title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     * this collects all data and asks the splittycontroller to add the
     * expense to the list but that might not be necessary when the database works
     */
    @FXML
    public void addExpense() {
        List<String> participants = getSelected();//get all the names of the participants
        boolean error = false;
        Date date = null;
        try {
            LocalDate localDate = dateSelect.getValue();
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            error = true;
            dateInvalidError.setVisible(true);
        }
        Type type = (Type) category.getValue();
        if (type == null) type= Type.Other;
        Participant payer = personComboBox.getValue();
        if (payer == null) {
            error = true;
            payerError.setVisible(true);
        }
        double amountDouble = 0.0;
        try {
            if (amount.getText() == null || amount.getText().isEmpty()) {
                noAmountError.setVisible(true);
                return;
            }
            amountDouble = Double.parseDouble(amount.getText());
            if (amountDouble <= 0.0) {
                amountNegativeError.setVisible(true);
                return;
            }
        } catch (NumberFormatException e) {
            amountInvalidError.setVisible(true);
            error = true;
        }
        String description = whatFor.getText();
        if (error) return;
        try {
            splittyCtrl.addExpense(description, type, date, amountDouble, payer.getEmail());
        } catch (Exception e) {
            addExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(event1 -> addExpenseError.setVisible(false));
            visiblePause.play();
            return;
        }
        back();
        splittyCtrl.fetchExpenses();
    }


    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    // This part is never used because expenses doesn't save who should pay for it only the payer
    // this should be changed eventually but that is not part of the ExpenseController

    public void createSplitList(ObservableList<Participant> people) {
        for (Participant p : people) {
            RadioButton button = new RadioButton();
            button.setText(p.getName());
            splitList.getItems().add(button);
        }
    }


    /**
     * @return a list with all the selected participants who should contribute to this expense
     */
    public List<String> getSelected() {
        List<String> res = new ArrayList<>();
        for (Object b : splitList.getItems()) {
            if (((RadioButton) b).isSelected()) {
                res.add(((RadioButton) b).getText());
            }
        }
        return res;
    }


    /**
     * select all buttons to split between
     */
    @FXML
    public void selectAll() {
        for (Object button : splitList.getItems()) {
            ((RadioButton) button).setSelected(true);
        }
    }



    //Setters for all the text attributes


//    public void setError(String text) {
//        this.error = error;
//    }

    public void setAddExpenseText(String text) {
        this.addExpenseText.setText(text);
    }

    public void setWhoPaid(String text) {
        this.whoPaid.setText(text);
    }

    public void setHowMuch(String text) {
        this.howMuch.setText(text);
    }

    public void setWhen(String text) {
        this.when.setText(text);
    }

    public void setHowToSplit(String text) {
        this.howToSplit.setText(text);
    }

    public void setDescription(String text) {
        this.description.setText(text);
    }

    public void setExpenseTypetext(String text) {
        this.expenseTypetext.setText(text);
    }

    public void setBack(String text) {
        this.back.setText(text);
    }

    public void setAdd(String text) {
        this.add.setText(text);
    }

    public void setAbort(String text) {
        this.cancel.setText(text);
    }

    public void setSelectAll(String text) {
        this.selectAll.setText(text);
    }

    public void setSelectWhoPaid(String text) {
        this.personComboBox.setPromptText(text);
    }

    public void setExpenseTypeBox(String text) {
        this.category.setPromptText(text);
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }

    public void resetPayerErrors() {
        payerError.setVisible(false);
    }

    public void resetAmountErrors(KeyEvent keyEvent) {
        amountNegativeError.setVisible(false);
        noAmountError.setVisible(false);
        amountInvalidError.setVisible(false);
    }

    public void resetDateErrors() {
        dateInvalidError.setVisible(false);
    }
}
