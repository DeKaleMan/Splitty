package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Currency;
import commons.*;
import commons.dto.ParticipantDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class SplittyOverviewCtrl implements Initializable {

    private int eventId;
    boolean translating = false;
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    private boolean admin;

    //these are for the css:
    @FXML
    private AnchorPane background;
    @FXML
    private ComboBox<String> languageSelect;
    @FXML
    private ImageView flag;
    @FXML
    private Label expenses;
    @FXML
    private Label participants;
    @FXML
    private Button backButton;
    @FXML
    private Button settleDebtsButton;
    @FXML
    private Button statisticsButton;
    @FXML
    private Button addExpenseButton;
    @FXML
    public Label noExpenseError;
    @FXML
    public Label expenseNotDeletedError;
    @FXML
    private Button editEvent;

    @FXML
    private Label eventCreatedLabel;

    @FXML
    private Tab paidByMe;

    @FXML
    private Tab involvingMe;

    @FXML
    private Button deleteExpenseButton;
    @FXML
    private Button editExpenseButton;


    @FXML
    public Button hostOptionsButton;


    @FXML
    private Button sendInvites;
    @FXML
    private Label titleLabel;

    @FXML
    public Tab allExpenses;
    private ListView<Expense> allExpensesList;
    private ListView<Expense> paidByMeList;
    private ListView<Expense> includingMeList;
    @FXML
    private TabPane expensesTabPane;

    @FXML
    public Button leaveButton;
    @FXML
    public Label joinedEventLabel;
    @FXML
    public Label inviteCode;
    @FXML
    private Button myDetails;
    @FXML
    public Button manageTagsButton;

    @FXML
    private ListView<Participant> participantListView;

    ObservableList<Participant> participantsList;

    @FXML
    private Button undo;
    
    Stack<Pair<String,Expense>> undoExpenseStack;
    Stack<Debt> undoDebtStack;

    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
        admin = false;
        participantsList = FXCollections.observableArrayList();
        undoExpenseStack = new Stack<>();
        undoDebtStack = new Stack<>();
    }

    public void initializeAll(Event event) {
        if (admin || event.getHost().equals(config.getId())) {
            hostOptionsButton.setVisible(true);
        } else {
            hostOptionsButton.setVisible(false);
        }
        setTitle(event.getName());
        this.eventId = event.getId();
        fetchParticipants();
        fetchExpenses();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ImageView stats = new ImageView(new Image("statistics.png"));
        stats.setFitHeight(15);
        stats.setFitWidth(15);
        statisticsButton.setGraphic(stats);
        mainCtrl.setButtonRedProperty(deleteExpenseButton);
        mainCtrl.setButtonRedProperty(leaveButton);
        participantListView.setItems(participantsList);
        participantListView.setCellFactory(param -> new ListCell<Participant>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.getName() == null) {
                        setText("unknown");
                    } else {
                        setText(item.getName());
                    }
                }
            }
        });
        undo.setVisible(false);
        serverUtils.registerForParticipantLongPolling(this::handleUpdate, this::handleDeletion);
        serverUtils.registerForExpenseWS("/topic/updateExpense", Expense.class, this::handleUpdateExpense);
        serverUtils.registerForExpenseWS("/topic/deleteExpense", Expense.class, this::handleDeleteExpense);
    }

    private void handleDeleteExpense(Expense expense) {
        if(expense.getEvent().getId() != eventId) return;
        Platform.runLater(()->{
            deleteExpenseInAllListViews(expense);
        });
    }

    private void deleteExpenseInAllListViews(Expense expense) {
        allExpensesList.getItems().removeIf(x -> x.getEvent().id == expense.getEvent().id
            && expense.getExpenseId() == x.getExpenseId());
        paidByMeList.getItems().removeIf(x -> x.getEvent().id == expense.getEvent().id
            && expense.getExpenseId() == x.getExpenseId());
        includingMeList.getItems().removeIf(x -> x.getEvent().id == expense.getEvent().id
            && expense.getExpenseId() == x.getExpenseId());
    }

    private void handleUpdateExpense(Expense expense) {
        if(expense.getEvent().getId() != eventId) return;
        Platform.runLater(()->{
            deleteExpenseInAllListViews(expense);
            addExpenseToAllListViews(expense);
        });
    }

    private void addExpenseToAllListViews(Expense expense) {
        allExpensesList.getItems().add(expense);
        if(expense.getPayer().getUuid().equals(config.getId())) paidByMeList.getItems().add(expense);
        List<String> including = serverUtils.getDebtByExpense(eventId,
            expense.getExpenseId()).stream().map(x -> x.getParticipant().getUuid()).toList();
        if (including.contains(config.getId())) includingMeList.getItems().add(expense);
    }

    private void handleUpdate(Participant p){
        if(p.getEvent().getId() != eventId) return;
        Platform.runLater(() -> {
            if(!participantsList.contains(p)) {
                participantsList.add(p);
                return;
            }
            participantsList.remove(p);
            participantsList.add(p);
        });
    }

    private void handleDeletion(Participant p){
        if(p.getEvent().getId() != eventId) return;
        Platform.runLater(() -> {
            participantsList.remove(p);
        });
    }


    public void setEventCode(int eventId) {
        this.eventId = eventId;
        this.inviteCode.setText(serverUtils.getEventById(eventId).getInviteCode());
    }

    /**
     * Shows the invitation scene (sends it the title to retain it)
     */
    @FXML
    public void sendInvitesOnClick() {
        mainCtrl.showInvitation(this.eventId);
    }

    /**
     * Sets the title of the event
     *
     * @param title event's title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }


    @FXML
    public void showAddExpense() {
        mainCtrl.showAddExpense(eventId);
    }

    @FXML
    public void showParticipantManager() {
        mainCtrl.showParticipantManager(eventId);
    }


    @FXML
    public void showStatistics() {
        mainCtrl.showStatistics(titleLabel.getText(), this.eventId);
    }

    /**
     * go back to Start screen
     */
    @FXML
    public void back() {
        if (admin) {
            mainCtrl.showAdminOverview();
        } else {
            mainCtrl.showStartScreen();
        }
        hideUndo();
        undoExpenseStack.clear();
        undoDebtStack.clear();
    }

    @FXML
    private void viewDebts() {
        mainCtrl.viewDeptsPerEvent(eventId);
    }


    @FXML
    public void editExpense() {
        Expense toEdit;
        try {
            toEdit = ((ListView<Expense>) expensesTabPane.getSelectionModel()
                    .getSelectedItem().getContent()).getSelectionModel().getSelectedItems().getFirst();
        } catch (NoSuchElementException e) {
            toEdit = null;
        }
        if (toEdit == null) {
            expenseNotDeletedError.setVisible(false);
            noExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                    event -> noExpenseError.setVisible(false)
            );
            return;
        }
        mainCtrl.showEditExpense(toEdit);
    }

    @FXML
    public Expense deleteExpense() {
        Expense toDelete;
        try {
            toDelete = ((ListView<Expense>) expensesTabPane.getSelectionModel()
                .getSelectedItem().getContent()).getSelectionModel().getSelectedItems().getFirst();
            if (toDelete == null) {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException e) {
            expenseNotDeletedError.setVisible(false);
            noExpenseError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                event -> noExpenseError.setVisible(false)
            );
            visiblePause.play();
            return null;

        }
        try {
            List<Debt> debts = serverUtils.getDebtByExpense(toDelete.getEvent().getId(), toDelete.getExpenseId());
            deleteExpenseAndDebts(debts, toDelete);
            pushUndoStacks(toDelete,debts,"delete");
            showUndo();
        } catch (RuntimeException e) {
            noExpenseError.setVisible(false);
            expenseNotDeletedError.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(
                event -> expenseNotDeletedError.setVisible(false)
            );
            visiblePause.play();
        }
        System.out.println("OK");
        fetchExpenses();
        serverUtils.generatePaymentsForEvent(eventId);
        return toDelete;
    }

    private void deleteExpenseAndDebts(List<Debt> debts, Expense toDelete) {
        for(Debt d : debts){
//                undoDebtStack.push(d);
            Participant p = d.getParticipant();
            serverUtils.updateParticipant(p.getUuid(),new ParticipantDTO(p.getName(),
                p.getBalance() - d.getBalance(), p.getIBan(),p.getBIC(),p.getEmail(),
                p.getAccountHolder(),p.getEvent().getId(),p.getUuid()));
        }
        serverUtils.deleteDebtsOfExpense(toDelete.getEvent().getId(), toDelete.getExpenseId());
        serverUtils.deleteExpense(toDelete);
        serverUtils.send("/app/deleteExpense", toDelete);
    }


    /**
     * fetches all expenses of this event and shows them in the assigned box
     */
    public void fetchExpenses() {
        Callback<ListView<Expense>, ListCell<Expense>>
            cellFactory = getExpenseListCellFactory();
        List<Expense> expenses = new ArrayList<>();
        try {
            expenses = serverUtils.getExpense(eventId);
        } catch (BadRequestException e) {
            System.out.println(e);
        }
        allExpensesList = new ListView<>();
        allExpensesList.getItems().addAll(expenses);
        allExpensesList.setCellFactory(cellFactory);
        allExpenses.setContent(allExpensesList);
        paidByMeList = new ListView<>();
        paidByMeList.getItems().addAll(expenses
            .stream()
            .filter(x -> x.getPayer().getUuid().equals(config.getId()))
            .toList()
        );
        paidByMeList.setCellFactory(cellFactory);
        paidByMe.setContent(paidByMeList);
        includingMeList = new ListView<>();
        for (Expense e : expenses) {
            List<String> including = serverUtils.getDebtByExpense(e.getEvent().getId(),
                e.getExpenseId()).stream().map(x -> x.getParticipant().getUuid()).toList();
            if (including.contains(config.getId())) includingMeList.getItems().add(e);
        }
        includingMeList.setCellFactory(cellFactory);
        involvingMe.setContent(includingMeList);
    }


    private Callback<ListView<Expense>, ListCell<Expense>> getExpenseListCellFactory() {
        Callback<ListView<Expense>,
            ListCell<Expense>> cellFactory = new Callback<ListView<Expense>, ListCell<Expense>>() {
                @Override
                public ListCell<Expense> call(ListView<Expense> expenseListView) {
                    return new ListCell<Expense>() {
                        @Override
                        protected void updateItem(Expense expense, boolean b) {
                            super.updateItem(expense, b);
                            if (expense == null || b) {
                                setGraphic(null);
                                setBackground(Background.EMPTY);
                            }
                            else {
                                GridPane grid = new GridPane();
                                Color bgColor = getBgColor(expense.getTag());
                                String  textColor = getTextColor(bgColor);
                                if (isSelected()) {
                                    setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
                                    textColor = "#FFFFFF";
                                } else {
                                    setBackground(new Background(new BackgroundFill(bgColor, null, null)));
                                }
                                Date date = expense.getDate();
                                Label dateLabel = getDateLabel(date, textColor);
                                List<Participant> involved =
                                    serverUtils.getDebtByExpense(expense.getEvent().getId(),
                                            expense.getExpenseId()).stream()
                                        .map(x -> x.getParticipant()).toList();
                                double totalExpense = expense.getTotalExpense();
                                try {
                                    totalExpense =
                                        mainCtrl.getAmountInDifferentCurrency(Currency.EUR,
                                            config.getCurrency(), date,
                                            totalExpense);
                                } catch (RuntimeException e){
                                    grid.add(new Text(e.getMessage()),0,0);
                                    setGraphic(grid);
                                    return;
                                }
                                Label mainInfo = getMainInfo(expense, totalExpense,
                                        involved, textColor);
                                grid.add(dateLabel, 0, 0);
                                grid.add(mainInfo, 1, 0);
                                Label list = new Label(involved.stream().map(x -> x.getName())
                                        .toList().toString());
                                list.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textColor + ";");
                                grid.add(list, 1, 1);
                                setGraphic(grid);
                            }}};}
            };
        return cellFactory;
    }

    private Color getBgColor(Tag tag) {
        if (tag.getColour() == null || tag.getColour().isEmpty()) {
            return Color.WHITE;
        }
        return Color.web(tag.getColour());
    }
    private String  getTextColor(Color color) {
        if (color.getBrightness() > 0.5) {
            return "#000000";
        }
        return "#FFFFFF";
    }
    private Label getMainInfo(Expense expense, double totalExpense, List<Participant> involved, String textColor) {
        Label mainInfo = new Label();
        if (expense.isSharedExpense()) {
            String paid =  " " + mainCtrl.translate("paid") + " ";
            String forT =  " " + mainCtrl.translate("for") + " " ;
            mainInfo.setText(expense.getPayer().getName()
                + paid
                + mainCtrl.getFormattedDoubleString(totalExpense)
                + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol()
                + "\n" + forT +  expense.getTag().getName());
        } else {
            String gave = " " + mainCtrl.translate("gave") + " " ;
            String to = " " + mainCtrl.translate("to") + " " ;
            mainInfo.setText(expense.getPayer().getName()
                + gave
                + mainCtrl.getFormattedDoubleString(totalExpense)
                + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol()
                + to
                + involved
                    .stream()
                    .filter(x -> !x.equals(expense.getPayer()))
                    .map(x -> x.getName())
                    .findFirst().get());
        }
        mainInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textColor + ";");
        return mainInfo;
    }

    private static Label getDateLabel(Date date, String  textColor) {
        Label dateLabel = new Label(
            date.getDate() + "." + (date.getMonth() < 9 ? "0" : "")
                + (date.getMonth() + 1) + "."
                + (date.getYear() + 1900));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textColor + ";");
        dateLabel.setPrefWidth(70);
        return dateLabel;
    }

    public void fetchParticipants() {
        participantsList.clear();
        List<Participant> participants = new ArrayList<>();
        try {
            participants = serverUtils.getParticipants(eventId);
        } catch (BadRequestException | NotFoundException e) {
            System.out.println(e);
        }
        participantsList.addAll(participants);
    }



    public void setExpensesText(String text) {
        Platform.runLater(() -> {
            this.expenses.setText(text);
        });
    }

    public void setParticipants(String text) {
        Platform.runLater(() -> {
            this.participants.setText(text);
        });
    }

    public void setBackButton(String text) {
        Platform.runLater(() -> {
            this.backButton.setText(text);
        });
    }

    public void setSettleDebtsButton(String text) {
        Platform.runLater(() -> {
            this.settleDebtsButton.setText(text);
        });
    }

    public void setStatisticsButton(String text) {
        Platform.runLater(() -> {
            this.statisticsButton.setText(text);
        });
    }

    public void setAddExpenseButton(String text) {
        Platform.runLater(() -> {
            this.addExpenseButton.setText(text);
        });
    }

    public void setPaidByMe(String text) {
        Platform.runLater(() -> {
            this.paidByMe.setText(text);
        });
    }

    public void setDeleteExpenseButton(String text) {
        Platform.runLater(() -> {
            this.deleteExpenseButton.setText(text);
        });
    }

    public void setSendInvites(String text) {
        Platform.runLater(() -> {
            this.sendInvites.setText(text);
        });
    }

    public void setAllExpenses(String text) {
        Platform.runLater(() -> {
            this.allExpenses.setText(text);
        });
    }

    public void setEditEvent(String text) {
        Platform.runLater(() -> {
            this.editEvent.setText(text);
        });
    }

    public void setEditExpense(String text) {
        Platform.runLater(() -> {
            this.editExpenseButton.setText(text);
        });
    }

    public void setLeaveButton(String text) {
        Platform.runLater(() -> {
            this.leaveButton.setText(text);
        });
    }

    public void leaveEvent() {
        if (admin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You can't leave the event");
            alert.setContentText("You cannot leave the event since you are in admin mode");
            alert.showAndWait();
        }
//        can only leave if balance is 0
        Participant me;
        try {
            me = serverUtils.getParticipant(config.getId(), eventId);
        } catch (RuntimeException e) {
            // label or error?
            return;
        }
        // balance margin of error is 0.0001
        if (Math.abs(me.getBalance()) > 0.0001){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(mainCtrl.translate("Leaving an event"));
            alert.setHeaderText(mainCtrl.translate("You can't leave the event"));
            alert.setContentText(mainCtrl.translate("You owe/are owed money."));
            alert.showAndWait();
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(mainCtrl.translate("Confirmation"));
        confirmation.setHeaderText(mainCtrl.translate("Leaving event"));
        confirmation.setContentText(mainCtrl.translate("Are you sure you want to leave the event?"));
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                serverUtils.deleteParticipant(config.getId(), eventId);
                mainCtrl.showStartScreen();
            }
        });
    }


    public void editEvent() {
        mainCtrl.showEditEvent(this.eventId);
    }

    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
        KeyCodeCombination k = new KeyCodeCombination(KeyCode.N,
            KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN);
        if (k.match(press)) {
            showAddExpense();
        }
        KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        if(ctrlZ.match(press)){
            undo();
        }

    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public void setEventCreatedLabel() {
        eventCreatedLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(
            event1 -> eventCreatedLabel.setVisible(false)
        );
        visiblePause.play();
    }

    public void setJoinedEventLabel() {
        joinedEventLabel.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
        visiblePause.setOnFinished(
            event1 -> joinedEventLabel.setVisible(false)
        );
        visiblePause.play();
    }


    public int getCurrentEventId(){
        return this.eventId;
    }

    public void editMyDetails() {
        mainCtrl.showEditParticipant(eventId);
    }

    public int getCurrentEventCode() {
        return this.eventId;
    }

    public void stopUpdates(){
        serverUtils.stop();
    }
    public void setmyDetails(String txt){
        Platform.runLater(() -> {this.myDetails.setText(txt);});
    }
    public void setHostOptionsButton(String txt){
        Platform.runLater(() -> {
            this.hostOptionsButton.setText(txt);
        });
    }

    @FXML
    public void showLangOptions(){
        languageSelect.show();
        flag.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!languageSelect.getBoundsInParent().contains(event.getX(), event.getY())) {
                // Clicked outside the choice box, hide it
                languageSelect.setVisible(false);
            }
        });
        flag.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // Hide the choice box when any key is pressed
            languageSelect.setVisible(false);
        });
    }

    public void setFlag(Image image) {
        flag.setImage(image);
    }
    @FXML
    public void changeLanguage(){
        if(translating) return;
        try {
            if (languageSelect.getSelectionModel().getSelectedItem() != null) {
                String selected = languageSelect.getSelectionModel().getSelectedItem();
                if(selected.equals(mainCtrl.language)){
                    return;
                }
                //Language toLang = Language.valueOf(selected);
                if (mainCtrl.languages.contains(selected)) {
                    config.setLanguage(selected);
                    config.write();
                    String toLang = selected;
                    mainCtrl.changeLanguage(toLang);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void setLanguageSelect(){
        translating = true;
        ObservableList<String> languages = FXCollections.observableArrayList();
        mainCtrl.language = config.getLanguage();
        if (mainCtrl.language == null) {
            mainCtrl.language = "en";
        }
        languages.addAll(mainCtrl.languages);
        languageSelect.setItems(languages);
        languageSelect.setValue(mainCtrl.language);
        //languageSelect.setValue(flag);
        Image flag = mainCtrl.getFlag();
        setFlag(flag);
        translating = false;
    }
    
    public void pushUndoStacks(Expense expense, List<Debt> debts, String method){
        undoExpenseStack.push(new Pair<>(method,expense));
        debts.forEach(undoDebtStack::push);
    }
    
    public void undo(){
        if(undoExpenseStack.isEmpty()) return;
        Pair<String,Expense> expensePair = undoExpenseStack.pop();
        List<Debt> debts = new ArrayList<>();
        while(!undoDebtStack.isEmpty() && undoDebtStack.peek().getExpense()
            .equals(expensePair.getValue())) debts.add(undoDebtStack.pop());
        if(expensePair.getKey().equals("add")){
            undoAdd(expensePair.getValue());
        }else if(expensePair.getKey().equals("edit")){
            undoEdit(expensePair.getValue(), debts);
        }else{
            undoDelete(expensePair.getValue(),debts);
        }
        fetchExpenses();
        if(undoExpenseStack.isEmpty()) undo.setVisible(false);
        serverUtils.generatePaymentsForEvent(eventId);
    }

    private void undoEdit(Expense expense, List<Debt> debts) {
        mainCtrl.editExpense(expense.getExpenseId(),expense.getDescription(), expense.getTag(),
            expense.getDate(),expense.getTotalExpense(),
            expense.getPayer(), expense.getEvent().getId(),
            expense.isSharedExpense(),
            debts
                .stream()
                .filter(x -> x.getBalance() < 0)
                .map(x -> x.getParticipant())
                .toList());
    }

    private void undoDelete(Expense expense, List<Debt> debts) {
        mainCtrl.addExpense(expense.getDescription(), expense.getTag(),
                expense.getDate(),expense.getTotalExpense(),
                serverUtils.getParticipant(expense.getPayer().getUuid(),
                    expense.getPayer().getEvent().getId()), expense.getEvent().getId(),
                expense.isSharedExpense(),
                debts
                        .stream()
                        .filter(x -> x.getBalance() < 0)
                        .map(x -> serverUtils.getParticipant(x.getParticipant().getUuid(),
                            x.getParticipant().getEvent().getId()))
                        .toList());
        serverUtils.generatePaymentsForEvent(eventId);
    }

    private void undoAdd(Expense toDelete) {
        List<Debt> debts = serverUtils.getDebtByExpense(toDelete.getEvent().getId(), toDelete.getExpenseId());
        deleteExpenseAndDebts(debts, toDelete);
    }

    public void showUndo(){
        undo.setVisible(true);
    }

    public void hideUndo(){
        undo.setVisible(false);
    }

    public void setUndo(String t){
        this.undo.setText(t);
    }

    public void viewManageTags() {
        mainCtrl.showManageTags(eventId, false, null, true);
    }
}

