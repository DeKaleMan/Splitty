package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class ExpenseCtrl {
    protected final ServerUtils serverUtils;
    protected int eventId;
    protected final MainCtrl mainCtrl;
    protected final Config config;


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
    protected Button addTagButton;
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
        setImages();
    }

    private void setImages() {
        ImageView tag = new ImageView(new Image("tagIcon.png"));
        tag.setFitWidth(15);
        tag.setFitHeight(15);
        addTagButton.setGraphic(tag);
    }


    public void setTagsUp() {
        this.category.getItems().clear();
        this.category.setItems(
                FXCollections.observableArrayList(serverUtils.getTagsByEvent(eventId)));
    }

    protected void setCurrencyUp() {
        currencyComboBox.setItems(FXCollections.observableArrayList(Currency.EUR, Currency.CHF, Currency.USD));
        currencyComboBox.setCellFactory(new Callback<ListView<Currency>, ListCell<Currency>>() {
            @Override
            public ListCell<Currency> call(ListView<Currency> currencyListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Currency currency, boolean b) {
                        super.updateItem(currency, b);
                        if (currency == null || b) {
                            setText(null);
                        } else {
                            setText(currency.toString());
                        }
                    }
                };
            }
        });
        currencyComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Currency currency, boolean b) {
                super.updateItem(currency, b);
                if (currency == null || b) {
                    setText("Select currency");
                } else {
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
        category.setCellFactory(param -> new ListCell<Tag>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                } else {
                    setText(mainCtrl.translate(tag.getName()));
                }
            }
        });
        category.setButtonCell(new ListCell<Tag>() {
            @Override
            protected void updateItem(Tag tag, boolean b) {
                super.updateItem(tag, b);
                if (tag == null || b) {
                    setText("Select category");
                } else {
                    setText("" + tag.getName());
                }
            }
        });
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
                if (newValue) {
                    isSharedExpense = true;
                    selectAll.setVisible(true);
                    selectSome.setVisible(true);
                    howToSplit.setVisible(true);
                    receiverHBox.setVisible(false);
                    if (selectSome.isSelected()) splitList.setVisible(true);
                }
            }
        });

        givingMoneyToSomeone.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue, Boolean newValue) {
                if (newValue) {
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
                        return new ListCell<>() {
                            @Override
                            protected void updateItem(Participant participant, boolean b) {
                                super.updateItem(participant, b);
                                if (participant == null || b) {
                                    setText(null);
                                } else {
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
            String amountText = amount.getText().replace(',', '.');
            amountDouble = Double.parseDouble(amountText);
            if (amountDouble <= 0.0) {
                amountError.setVisible(true);
                amountError.setText("Amount cannot be negative or zero*");
                return null;
            }
            if (currencyComboBox.getValue() == null) {
                System.out.println("Select currency");
                return null;
            }
            amountDouble = mainCtrl.getAmountInDifferentCurrency(currencyComboBox.getValue(),
                    Currency.EUR, date, amountDouble);
        } catch (NumberFormatException e) {
            amountError.setVisible(true);
            amountError.setText("Not a number, format e.g 13.99");
            return null;
        } catch (RuntimeException e) {
            amountError.setVisible(true);
            amountError.setText(e.getMessage());
            return null;
        }
        return amountDouble;
    }

    protected Tag getTag() {
        Tag tag = category.getValue();
        if (tag == null) {
            return serverUtils.getOtherTagById(eventId);
        }
        return tag;
    }

    protected Date getDate() {
        Date date;
        //link these to participants and then add the expense
        if (dateSelect.getValue() == null || checkDate(dateSelect.getEditor().getText())) {
            dateSelect.setPromptText("invalid Date");
            dateInvalidError.setVisible(true);
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
        Platform.runLater(() -> {
            mainCtrl.showSplittyOverview(eventId);
        });
    }


    public void setSceneTypeText(String text) {
        Platform.runLater(() -> {
            this.sceneTypeText.setText(text);
        });
    }

    public void setWhoPaid(String text) {
        Platform.runLater(() -> {
            this.whoPaid.setText(text);
        });
    }

    public void setHowMuch(String text) {
        Platform.runLater(() -> {
            this.howMuch.setText(text);
        });
    }

    public void setWhen(String text) {
        Platform.runLater(() -> {
            this.when.setText(text);
        });
    }

    public void setHowToSplit(String text) {
        Platform.runLater(() -> {
            this.howToSplit.setText(text);
        });
    }

    public void setDescription(String text) {
        Platform.runLater(() -> {
            this.description.setText(text);
        });
    }

    public void setExpenseTypetext(String text) {
        Platform.runLater(() -> {
            this.expenseTypetext.setText(text);
        });
    }

    public void setCommit(String text) {
        Platform.runLater(() -> {
            this.commit.setText(text);
        });
    }

    public void setAbort(String text) {
        Platform.runLater(() -> {
            this.cancel.setText(text);
        });
    }

    public void setSelectAll(String text) {
        Platform.runLater(() -> {
            this.selectAll.setText(text);
        });
    }

    public void setSelectSome(String text) {
        Platform.runLater(() -> {
            this.selectSome.setText(text);
        });
    }

    public void setSelectWhoPaid(String text) {
        Platform.runLater(() -> {
            this.personComboBox.setPromptText(text);
        });
    }

    public void setExpenseTypeBox(String text) {
        Platform.runLater(() -> {
            this.category.setPromptText(text);
        });
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }


    public void setSharedExpense(String txt) {
        Platform.runLater(() -> {
            this.sharedExpense.setText(txt);
        });
    }

    public void setGivingMoneyToSomeone(String txt) {
        Platform.runLater(() -> {
            this.givingMoneyToSomeone.setText(txt);
        });
    }

    public void setCommitExpenseError(String txt) {
        Platform.runLater(() -> {
            this.commitExpenseError.setText(txt);
        });
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
    public boolean checkDate(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }
}
