package client.scenes;

import client.utils.ServerUtils;
import commons.*;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class SplittyOverviewCtrl implements Initializable {

    //We need to store the eventCode right here
    private final int eventCode = 1; //replace with the actual eventCode

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    //these are for the css:
    @FXML
    private AnchorPane background;
    @FXML
    private Label expenses;
    @FXML
    private Label participants;

    @FXML
    private Button sendInvites;
    @FXML
    private Label titleLabel;

    @FXML
    public Tab allExpenses;
    private ListView expenseList;
    private TabPane tabPane;
    @Inject
    public SplittyOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.serverUtils = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        fetchExpenses();
        fetchParticipants();
    }

//    public void setEventCode(int eventCode){
//        this.eventCode = eventCode;
//    }

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
    private void back() {
        mainCtrl.showStartScreen();
    }
    @FXML
    private void viewDebts(){
        mainCtrl.viewDeptsPerEvent();
    }

    public void addExpense(String description, Type type, Date date, Double totalExpense, String payerEmail){
        try{
            serverUtils.addExpense(new ExpenseDTO(eventCode, description, type, date, totalExpense, payerEmail));
        }catch (Exception e){
            System.out.println(e);
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
        ListView<Participant> participantsList = new ListView<>();
        List<Participant> participants = new ArrayList<>();
        try{
            participants = serverUtils.getParticipants(eventCode);
        }catch (BadRequestException e){
            System.out.println(e);
        }
        catch (NotFoundException e){
            System.out.println(e);
        }
        participantsList.getItems().addAll(participants);
        //.setContent(expenseList);
    }


}

