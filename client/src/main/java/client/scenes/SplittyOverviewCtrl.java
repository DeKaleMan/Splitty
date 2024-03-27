package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import commons.dto.ExpenseDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class SplittyOverviewCtrl implements Initializable {

    //We need to store the eventCode right here
    private int eventCode;

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
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
    private Tab tab2;
    @FXML
    private Button deleteExpenseButton;


    @FXML
    private Button sendInvites;
    @FXML
    private Label titleLabel;

    @FXML
    public Tab allExpenses;
    private ListView<Expense> expenseList;
    private TabPane tabPane;

    @FXML
    private ListView<Participant> participantListView;
    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
        admin = false;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        participantListView.setCellFactory(param -> new ListCell<Participant>(){
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
//        fetchExpenses();
//        fetchParticipants();
    }

    public void setEventCode(int eventCode){
        this.eventCode = eventCode;
    }

    /**
     * Shows the invitation scene (sends it the title to retain it)
     */
    @FXML
    public void sendInvitesOnClick(){
        mainCtrl.showInvitation(titleLabel.getText());
    }

    /**
     * Sets the title of the event
     * @param title event's title
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }


    @FXML
    public void showAddExpense() {
        mainCtrl.showAddExpense(titleLabel.getText());
    }

    @FXML
    public void viewParticipantManager(){
        mainCtrl.showParticipantManager(titleLabel.getText());
    }


    @FXML
    public void showStatistics(){
        mainCtrl.showStatistics(titleLabel.getText());
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
    private void viewDebts(){
        mainCtrl.viewDeptsPerEvent();
    }

    public void addExpense(String description, Type type, Date date, Double totalExpense, String payerEmail){
        try{
            ExpenseDTO exp = new ExpenseDTO(eventCode, description, type, date, totalExpense, payerEmail);
            serverUtils.addExpense(exp);
            serverUtils.send("/app/addExpense", exp);
            serverUtils.generatePaymentsForEvent(eventCode);
        }catch (NotFoundException ep) {
            // Handle 404 Not Found error
            // Display an error message or log the error
            System.err.println("Expense creation failed: Resource not found.");
            ep.printStackTrace();
            // Optionally, notify the user or perform error recovery actions
        }
    }
    @FXML
    public Expense deleteExpense() {

        Expense toDelete =  (Expense) expenseList.getSelectionModel().getSelectedItems().getFirst();

        if(toDelete == null){
            throw new NoSuchElementException("No element selected");
        }
        try{
            serverUtils.deleteExpense(toDelete);
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("OK");
        fetchExpenses();
        return toDelete;
    }



    /**
     * fetches all expenses of this event and shows them in the assigned box
     */
    public void fetchExpenses(){
        expenseList = new ListView<>();
        List<Expense> expenses = new ArrayList<>();
        try{
            expenses = serverUtils.getExpense(eventCode);
        }catch (BadRequestException e){
            System.out.println(e);
        }

        expenseList.getItems().addAll(expenses);
        allExpenses.setContent(expenseList);
    }
    public void fetchParticipants(){

        List<Participant> participants = new ArrayList<>();
        try{
            participants = serverUtils.getParticipants(eventCode);
        }catch (BadRequestException | NotFoundException e){
            System.out.println(e);
        }
        participantListView.setItems(FXCollections.observableArrayList(participants));
    }
    // all textSetters


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


    public void setTab2(String text) {
        this.tab2.setText(text);
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
        serverUtils.deleteParticipant(mainCtrl.getMyUuid(), eventCode);
        mainCtrl.showStartScreen();
    }
    @FXML
    public void onKeyPressed(KeyEvent press) {
        if (press.getCode() == KeyCode.ESCAPE) {
            back();
        }
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}

