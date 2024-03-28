package client.scenes;

import client.utils.ServerUtils;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private Button add;
    @FXML
    public Label addExpenseError;
    @FXML
    private Button cancel;
    @FXML
    private RadioButton selectAll;
    @FXML
    private RadioButton selectSome;

    @FXML
    private ToggleGroup selectionToggles;

    private Participant payer;
    
    private ObservableList<Participant> rest;

    private Set<Participant> owing;

    @Inject
    public AddExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl,
                          SplittyOverviewCtrl splittyCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.splittyCtrl = splittyCtrl;
        rest = FXCollections.observableArrayList();
        owing = new HashSet<>();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //the buttons
        mainCtrl.setButtonRedProperty(cancel);
        mainCtrl.setButtonGreenProperty(add);

        splitList.setVisible(false);

        ObservableList<Participant> list = FXCollections.observableArrayList();
        List<Participant> allparticipants;
        serverUtils.registerForExpenseWS("/topic/addExpense", Expense.class, exp -> {
            System.out.println("expense added " + exp);
        });
        try {
            allparticipants = serverUtils.getParticipants(eventCode);
        } catch (RuntimeException e) {
            allparticipants = new ArrayList<>();
        }
        list.addAll(allparticipants);
        personComboBox.setItems(list);
        personComboBox.setCellFactory(param -> new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                    setEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                            payer = item;
                            rest.clear();
                            rest.addAll(list);
                            rest.remove(item);
                            if (selectAll.isSelected()) {
                                owing.addAll(rest);
                                owing.remove(payer);
                                splitList.setVisible(false);
                            }
                            if (selectSome.isSelected()) {
                                owing.clear();
                                splitList.setVisible(true);
                            }
                        }
                    });
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
        setSplitListUp();
        setTogglesUp();
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
        this.category.setItems(
            FXCollections.observableArrayList(Type.Food, Type.Drinks, Type.Travel, Type.Other));
    }

    private void setTogglesUp() {
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (payer == null) {
                        System.out.println("Select payer");
                        return;
                    }
                    owing.addAll(rest);
                    owing.remove(payer);
                    splitList.setVisible(false);
                }
            }
        });
        selectSome.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (payer == null) {
                        System.out.println("Select payer");
                        return;
                    }
                    owing.clear();
                    splitList.setVisible(true);
                }
            }
        });
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
        boolean error = false;
        Date date = null;
        //link these to participants and then add the expense
        if (dateSelect.getValue() == null) {
            dateSelect.setPromptText("invalid Date");
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
        if(error) return;

        String description = whatFor.getText();

        try {
            LocalDate localDate = dateSelect.getValue();

            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            //add to database
            ExpenseDTO
                exp =
                new ExpenseDTO(eventCode, description, type, date, amountDouble, payer.getUuid());
            Expense expense = serverUtils.addExpense(exp);
            serverUtils.send("/app/addExpense", exp);
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
            serverUtils.generatePaymentsForEvent(eventCode);
            splittyCtrl.fetchExpenses();
            back();
        } catch (Exception e) {
            addExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(event1 -> addExpenseError.setVisible(false));
            visiblePause.play();
        }
    }


    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    // This part is never used because expenses doesn't save who should pay for it only the payer
    // this should be changed eventually but that is not part of the ExpenseController

    public void setSplitListUp() {
        splitList.setItems(rest);
        splitList.setCellFactory(new Callback<ListView<Participant>, ListCell<Participant>>() {
            @Override
            public ListCell call(ListView listView) {
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
