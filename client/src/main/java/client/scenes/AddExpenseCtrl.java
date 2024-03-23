package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final int eventCode = 1;
    private final MainCtrl mainCtrl;
    private final SplittyOverviewCtrl splittyCtrl;


    @FXML
    public Label titleLabel;

    @FXML
    private ComboBox<Participant> personComboBox;

    //all the things needed for the addExpense
    @FXML
    private TextArea whatFor;

    @FXML
    private DatePicker dateSelect;

    @FXML
    private ListView splitList;

    @FXML
    private TextField amount;

    @FXML
    private ComboBox category;
    @FXML
    private Label error;
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
    private Button abort;
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
        Participant person1 = new Participant("name", 1000.00, "iBAN", "bIC", "holder", "uuid1", new Event());
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        serverUtils.registerForExpenseWS("/topic/addExpense", Expense.class ,exp -> {
            System.out.println("expense added " + exp);
        });
        try {
            allparticipants = serverUtils.getParticipants(eventCode);
        } catch (Exception e) {
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
        mainCtrl.showSplittyOverview(titleLabel.getText());
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
        //collect information
        List<String> participants = getSelected();//get all the names of the participants

        //link these to participants and then add the expense
        if (dateSelect.getValue() == null) {
            dateSelect.setPromptText("invalid Date");
        }

        try {
            LocalDate localDate = dateSelect.getValue();
            Date date = java.sql.Date.valueOf(localDate);
            Type type = (Type) category.getValue();
            if (type == null) throw new NoSuchElementException();
            double amountDouble = Double.parseDouble(amount.getText());
            if (amountDouble <= 0) {
                amount.setText("NO VALID AMOUNT");
                throw new NoSuchElementException();
            }
            Participant payer = personComboBox.getValue();
            String description = whatFor.getText();
            //add to database
            splittyCtrl.addExpense(description, type, date, amountDouble, payer.getEmail());
            back();
        } catch (Exception e) {
            dateSelect.setPromptText("try again");
            error.setText("Something is incomplete");
        }
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
        this.abort.setText(text);
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
}
