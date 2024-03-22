package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;

import commons.dto.ExpenseDTO;
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

    private ObservableList<Expense> data;

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

    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, SplittyOverviewCtrl splittyCtrl, ObservableList<Expense> data) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.splittyCtrl = splittyCtrl;
        this.data=data;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Participant person1 = new Participant("name", 1000.00, "iBAN", "bIC", "holder", "email", new Event());
        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
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
        });
        this.category.setItems(FXCollections.observableArrayList(Type.Food, Type.Drinks, Type.Travel, Type.Other));

        try {
            serverUtils.registerForExpenseWS("/topic/addExpense", Expense.class ,q -> {
                data.add(q);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
