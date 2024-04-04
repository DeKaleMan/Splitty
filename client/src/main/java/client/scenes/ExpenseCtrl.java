package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Participant;
import commons.Type;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


import java.net.URL;
import java.time.LocalDate;

import java.util.*;

public abstract class ExpenseCtrl {
    protected final ServerUtils serverUtils;
    protected int eventCode;
    protected final MainCtrl mainCtrl;
    protected final Config config;


//    @FXML
//    protected Label titleLabel;



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

    protected Participant payer;

    protected ObservableList<Participant> rest;

    protected Set<Participant> owing;

    protected boolean isSharedExpense;


    public ExpenseCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Config config) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.config = config;
        rest = FXCollections.observableArrayList();
        owing = new HashSet<>();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.setButtonRedProperty(cancel);
        mainCtrl.setButtonGreenProperty(commit);
        setListViewsUp();
        setTogglesUp();
        setCategoriesUp();
        setCurrencyUp();
    }

    protected void setCurrencyUp() {
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.EUR,Currency.CHF,Currency.USD));
        currencyComboBox.setCellFactory(new Callback<ListView<Currency>, ListCell<Currency>>() {
            @Override
            public ListCell<Currency> call(ListView<Currency> currencyListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(Currency currency, boolean b) {
                        super.updateItem(currency, b);
                        if(currency == null || b){
                            setText(null);
                        }else{
                            setText(currency.toString());
                        }
                    }
                };
            }
        });
        currencyComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Currency currency, boolean b) {
                super.updateItem(currency, b);
                if(currency == null || b){
                    setText("Select currency");
                }else{
                    setText(currency.toString());
                }
            }
        });

        currencyComboBox.setValue(Currency.EUR);
    }

    protected void setListViewsUp() {
        setSplitListUp();
        setReceiverListUp();
    }

    abstract void setSplitListUp();

    protected void setCategoriesUp() {
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
        category.setButtonCell(new ListCell<Type>(){
            @Override
            protected void updateItem(Type type, boolean b) {
                super.updateItem(type, b);
                if(type == null || b){
                    setText("Select category");
                }else{
                    setText("" + type);
                }
            }
        });
        this.category.setItems(
            FXCollections.observableArrayList(Type.Food, Type.Drinks, Type.Travel, Type.Other));
    }

    protected void setTogglesUp() {
        setSelectionTogglesUp();
        setExpenseTypeTogglesUp();
    }

    private void setExpenseTypeTogglesUp() {
        sharedExpense.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if(newValue){
                    isSharedExpense = true;
                    selectAll.setVisible(true);
                    selectSome.setVisible(true);
                    howToSplit.setVisible(true);
                    receiverHBox.setVisible(false);
                    if(selectSome.isSelected()) splitList.setVisible(true);
                }
            }
        });

        givingMoneyToSomeone.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if(newValue){
                    isSharedExpense = false;
                    selectAll.setVisible(false);
                    selectSome.setVisible(false);
                    howToSplit.setVisible(false);
                    splitList.setVisible(false);
                    receiverHBox.setVisible(true);
                }
            }
        });
    }

    private void setSelectionTogglesUp() {
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
                    splitList.refresh();
                    splitList.setVisible(true);
                }
            }
        });
    }

    private void setReceiverListUp() {
        receiverListView.setItems(rest);
        receiverListView.setCellFactory(
            new Callback<ListView<Participant>, ListCell<Participant>>() {
                @Override
                public ListCell<Participant> call(ListView<Participant> participantListView) {
                    return new ListCell<>(){
                        @Override
                        protected void updateItem(Participant participant, boolean b) {
                            super.updateItem(participant, b);
                            if(participant == null || b){
                                setText(null);
                            }else{
                                setText(participant.getName());
                            }
                        }
                    };
                }
            });
    }

    protected Double getAmountDouble(Date date) {
        double amountDouble = 0.0;
        try {
            if (amount.getText() == null || amount.getText().isEmpty()) {
                amountError.setText("An amount is required");
                amountError.setVisible(true);
                return null;
            }
            amountDouble = Double.parseDouble(amount.getText());
            if (amountDouble <= 0.0) {
                amountError.setVisible(true);
                amountError.setText("Amount cannot be negative or zero*");
                return null;
            }
            if(currencyComboBox.getValue() == null){
                System.out.println("Select currency");
                return null;
            }
            amountDouble = mainCtrl.getAmountInDifferentCurrency(currencyComboBox.getValue(),
                Currency.EUR,date,amountDouble);
        } catch (NumberFormatException e) {
            amountError.setVisible(true);
            amountError.setText("Not a number, format e.g 13.99");
            return null;
        } catch (RuntimeException e){
            amountError.setVisible(true);
            amountError.setText(e.getMessage());
            return null;
        }
        return amountDouble;
    }

    protected Type getType() {
        Type type = (Type) category.getValue();
        if (type == null) type= Type.Other;
        return type;
    }

    protected Date getDate() {
        Date date;
        //link these to participants and then add the expense
        if (dateSelect.getValue() == null) {
            dateSelect.setPromptText("invalid Date");
            return null;
        }

        LocalDate localDate = dateSelect.getValue();
        date = java.sql.Date.valueOf(localDate);
        return date;
    }

    protected void setComboboxUp(ObservableList<Participant> list) {
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
                    setText("Select who paid");
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @FXML
    protected void back() {
        mainCtrl.showSplittyOverview(eventCode);
    }

//    public void setTitle(String title) {
//        titleLabel.setText(title);
//    }

    public void setSceneTypeText(String text) {
        this.sceneTypeText.setText(text);
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

    public void setCommit(String text) {
        this.commit.setText(text);
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
        amountError.setVisible(false);
    }

    public void resetDateErrors() {
        dateInvalidError.setVisible(false);
    }
}
