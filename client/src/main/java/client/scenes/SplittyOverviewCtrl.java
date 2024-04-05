package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.*;
import commons.Currency;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import commons.dto.ExpenseDTO;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class SplittyOverviewCtrl implements Initializable {


    //We need to store the eventCode right here
    private int eventId;

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Config config;
    private boolean admin;

    //these are for the css:
    @FXML
    private AnchorPane background;
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
    private ListView<Participant> participantListView;

    ObservableList<Participant> participantsList;

    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
        admin = false;
        participantsList = FXCollections.observableArrayList();
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
        serverUtils.registerForParticipantLongPolling(this::handleUpdate, this::handleDeletion);
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
        mainCtrl.showAddExpense(titleLabel.getText(), eventId);
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
    }

    @FXML
    private void viewDebts() {
        mainCtrl.viewDeptsPerEvent(eventId);
    }


    public void addExpense(String description, Type type, Date date,
                           Double totalExpense, String payerEmail) {
        try {
            ExpenseDTO exp = new ExpenseDTO(eventId, description, type,
                date, totalExpense, payerEmail, true);
            serverUtils.addExpense(exp);
            serverUtils.send("/app/addExpense", exp);
            serverUtils.generatePaymentsForEvent(eventId);
        } catch (NotFoundException ep) {
            // Handle 404 Not Found error
            // Display an error message or log the error
            System.err.println("Expense creation failed: Resource not found.");
            ep.printStackTrace();
            // Optionally, notify the user or perform error recovery actions
        }

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
            for(Debt d : debts){
                Participant p = d.getParticipant();
                serverUtils.updateParticipant(p.getUuid(),new ParticipantDTO(p.getName(),
                    p.getBalance() - d.getBalance(), p.getIBan(),p.getBIC(),p.getEmail(),
                    p.getAccountHolder(),p.getEvent().getId(),p.getUuid()));
            }
            serverUtils.deleteDebtsOfExpense(toDelete.getEvent().getId(), toDelete.getExpenseId());
            serverUtils.deleteExpense(toDelete);
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
                            if (expense == null || b) setGraphic(null);
                            else {
                                GridPane grid = new GridPane();
                                Date date = expense.getDate();
                                Label dateLabel = getDateLabel(date);
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
                                }catch (RuntimeException e){
                                    grid.add(new Text(e.getMessage()),0,0);
                                    setGraphic(grid);
                                    return;
                                }
                                Text mainInfo = getMainInfo(expense, totalExpense,
                                        involved);
                                grid.add(dateLabel, 0, 0);
                                grid.add(mainInfo, 1, 0);
                                grid.add(new Text(involved.stream().map(x -> x.getName())
                                    .toList().toString()), 1, 1);
                                setGraphic(grid);
                            }
                        }
                    };
                }
            };
        return cellFactory;
    }

    private Text getMainInfo(Expense expense, double totalExpense, List<Participant> involved) {
        Text mainInfo = new Text();
        if (expense.isSharedExpense()) {
            String paid = mainCtrl.translate("paid");
            String forT = mainCtrl.translate("for");
            mainInfo.setText(expense.getPayer().getName()
                + paid
                + mainCtrl.getFormattedDoubleString(totalExpense)
                + java.util.Currency.getInstance(config.getCurrency().toString()).getSymbol()
                + forT + expense.getType());
        } else {
            String gave = mainCtrl.translate("gave");
            String to = mainCtrl.translate("to");
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
        return mainInfo;
    }

    private static Label getDateLabel(Date date) {
        Label dateLabel = new Label(
            date.getDate() + "." + (date.getMonth() < 9 ? "0" : "")
                + (date.getMonth() + 1) + "."
                + (date.getYear() + 1900));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black");
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
        this.expenses.setText(text);
    }

    public void setParticipants(String text) {
        this.participants.setText(text);
    }

    public void setBackButton(String text) {
        this.backButton.setText(text);
    }

    public void setSettleDebtsButton(String text) {
        this.settleDebtsButton.setText(text);
    }

    public void setStatisticsButton(String text) {
        this.statisticsButton.setText(text);
    }

    public void setAddExpenseButton(String text) {
        this.addExpenseButton.setText(text);
    }


    public void setPaidByMe(String text) {
        this.paidByMe.setText(text);
    }

    public void setDeleteExpenseButton(String text) {
        this.deleteExpenseButton.setText(text);
    }

    public void setSendInvites(String text) {
        this.sendInvites.setText(text);
    }

    public void setAllExpenses(String text) {
        this.allExpenses.setText(text);
    }
    public void setEditEvent(String text) {
        this.editEvent.setText(text);
    }
    public void setEditExpense(String text) {
        this.editExpenseButton.setText(text);
    }
    public void setLeaveButton(String text) {
        this.leaveButton.setText(text);
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

        if (me.getBalance() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You can't leave the event");
            alert.setContentText("You can only leave the event if your balance is 0");
            alert.showAndWait();
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Leaving event");
        confirmation.setContentText("Are you sure you want to leave the event?");
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
}

