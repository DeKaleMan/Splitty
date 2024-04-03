package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Type;
import commons.dto.ExpenseDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;


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
    public Button leaveConfirmationButton;
    @FXML
    public Button cancelLeaveButton;
    @FXML
    public Label confirmationLabel;
    @FXML
    public Label joinedEventLabel;


    @FXML
    private ListView<Participant> participantListView;

    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        this.config = config;
        admin = false;
    }

    public void initializeAll(Event event) {
        if (admin || event.getHost().equals(config.getId())) {
            hostOptionsButton.setVisible(true);
        } else {
            hostOptionsButton.setVisible(false);
        }
        setTitle(event.getName());
        setEventId(event.getId());
        fetchParticipants();
        fetchExpenses();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        mainCtrl.setButtonRedProperty(deleteExpenseButton);
        mainCtrl.setButtonRedProperty(leaveButton);
        mainCtrl.setButtonRedProperty(leaveConfirmationButton);
        mainCtrl.setButtonGreenProperty(cancelLeaveButton);
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
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Shows the invitation scene (sends it the title to retain it)
     */
    @FXML
    public void sendInvitesOnClick() {
        mainCtrl.showInvitation(titleLabel.getText());
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
        Expense toEdit = ((ListView<Expense>) expensesTabPane.getSelectionModel()
            .getSelectedItem().getContent()).getSelectionModel().getSelectedItems().getFirst();

        if (toEdit == null) {
            throw new NoSuchElementException("No element selected");
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
                                Label dateLabel = new Label(
                                    date.getDate() + "." + (date.getMonth() < 9 ? "0" : "")
                                        + (date.getMonth() + 1) + "."
                                        + (date.getYear() + 1900));
                                dateLabel.setStyle("-fx-font-size: 12px");
                                dateLabel.setPrefWidth(70);
                                List<String> involved =
                                    serverUtils.getDebtByExpense(expense.getEvent().getId(),
                                            expense.getExpenseId()).stream()
                                        .map(x -> x.getParticipant().getName()).toList();
                                Text mainInfo = new Text();
                                if (expense.isSharedExpense()) {
                                    mainInfo.setText(expense.getPayer().getName() + " paid " +
                                        expense.getTotalExpense() + " for " + expense.getType());
                                } else {
                                    mainInfo.setText(expense.getPayer().getName() + " gave " +
                                        expense.getTotalExpense() + " to " +
                                        involved
                                            .stream()
                                            .filter(x -> !x.equals(expense.getPayer().getName()))
                                            .findFirst().get());
                                }
                                grid.add(dateLabel, 0, 0);
                                grid.add(mainInfo, 1, 0);
                                grid.add(new Text(involved.toString()), 1, 1);
                                setGraphic(grid);
                            }
                        }
                    };
                }
            };
        return cellFactory;
    }

    public void fetchParticipants() {
        participantListView.getItems().clear();
        List<Participant> participants = new ArrayList<>();
        try {
            participants = serverUtils.getParticipants(eventId);
        } catch (BadRequestException | NotFoundException e) {
            System.out.println(e);
        }
        participantListView.setItems(FXCollections.observableArrayList(participants));
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

    public void leaveEvent(ActionEvent actionEvent) {
        // can only leave if balance is 0
        Participant me = serverUtils.getParticipant(config.getId(), eventId);
        if (me.getBalance() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You can't leave the event");
            alert.setContentText("You can only leave the event if your balance is 0");
            alert.showAndWait();
            return;
        }

        serverUtils.deleteParticipant(config.getId(), eventId);
        mainCtrl.showStartScreen();
        confirmationLabel.setVisible(false);
        cancelLeaveButton.setVisible(false);
        leaveConfirmationButton.setVisible(false);
    }


    public void editEvent() {
        mainCtrl.editEvent();
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

    public void setConfirmation(ActionEvent actionEvent) {
        confirmationLabel.setVisible(true);
        cancelLeaveButton.setVisible(true);
        leaveConfirmationButton.setVisible(true);
    }

    public void cancelLeave(ActionEvent actionEvent) {
        confirmationLabel.setVisible(false);
        cancelLeaveButton.setVisible(false);
        leaveConfirmationButton.setVisible(false);
    }

    public int getCurrentEventId(){
        return this.eventId;
    }

    public void editMyDetails(ActionEvent actionEvent) {
        mainCtrl.showMyDetails(eventId);
    }
}

