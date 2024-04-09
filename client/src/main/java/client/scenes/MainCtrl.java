/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.AdminWindows;
import client.utils.EventPropGrouper;
import client.utils.ServerUtils;
import client.utils.SetLanguage;
import commons.*;
import commons.Currency;
import commons.dto.DebtDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MainCtrl {
    private final String css = this.getClass().getResource("/general.css").toExternalForm();

    protected String language = "en";
    protected String currentLang = "en";
    protected List<String> languages;


    private SetLanguage setLanguage;
    private Stage primaryStage;

    private Scene invitation;
    private InvitationCtrl invitationCtrl;

    private Scene splittyOverview;
    private SplittyOverviewCtrl splittyOverviewCtrl;

    private Scene startScreen;
    private StartScreenCtrl startScreenCtrl;

    private Scene addExpense;
    private AddExpenseCtrl addExpenseCtrl;

    private Scene contactDetails;
    private ContactDetailsCtrl contactDetailsCtrl;

    private Scene adminLogin;
    private AdminLoginCtrl adminLoginCtrl;

    private Scene adminOverview;
    private AdminOverviewCtrl adminOverviewCtrl;

    private Scene manageParticipants;
    private ManageParticipantsCtrl manageParticipantsCtrl;

    private Scene addParticipant;
    private AddParticipantCtrl addParticipantCtrl;

    private Scene editParticipant;
    private EditParticipantCtrl editParticipantCtrl;

    private Scene statistics;
    private StatisticsCtrl statisticsCtrl;
    private Scene debts;
    private DebtCtrl debtCtrl;
    private Scene userEventList;
    private UserEventListCtrl userEventListCtrl;
    private Scene createEvent;
    private Scene editEvent;
    private EditEventCrtl editEventCrtl;
    private CreateEventCtrl createEventCtrl;
    private Scene editExpense;
    private EditExpenseCtrl editExpenseCtrl;
    private ServerUtils serverUtils;

    private Scene settings;
    private SettingsCtrl settingCtrl;

    private Scene server;
    private ServerCtrl serverCtrl;


    // probably not the best place to put that here but works for now
    // maybe in the future isolate it into an eventctrl?
    private List<Event> events = new ArrayList<>();

    public void initialize(Stage primaryStage, Pair<ServerCtrl, Parent> server,
                           Pair<SettingsCtrl, Parent> settings) {
        this.primaryStage = primaryStage;
        this.serverCtrl = server.getKey();
        this.server = new Scene(server.getValue());
        this.settingCtrl = settings.getKey();
        this.settings = new Scene(settings.getValue());
        settingCtrl.initializeConfig();
        showServerStartup(true);
        primaryStage.show();
    }


    public void initialize(Stage primaryStage, Pair<InvitationCtrl, Parent> invitation,
                           Pair<SplittyOverviewCtrl, Parent> splittyOverview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<ContactDetailsCtrl, Parent> contactDetails,
                           EventPropGrouper eventPropGrouper,
                           Pair<UserEventListCtrl, Parent> userEventList,
                           Pair<CreateEventCtrl, Parent> createEvent,
                           AdminWindows adminWindows,
                           Pair<SettingsCtrl, Parent> settings,
                           Pair<ServerCtrl, Parent> server) {
        this.primaryStage = primaryStage;
        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());
        this.splittyOverviewCtrl = splittyOverview.getKey();
        this.splittyOverview = new Scene(splittyOverview.getValue());
        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());
        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetails = new Scene(contactDetails.getValue());
        this.adminLoginCtrl = adminWindows.adminLogin().getKey();
        this.adminLogin = new Scene(adminWindows.adminLogin().getValue());
        this.adminOverviewCtrl = adminWindows.adminOverview().getKey();
        this.adminOverview = new Scene(adminWindows.adminOverview().getValue());
        this.addExpenseCtrl = eventPropGrouper.addExpense().getKey();
        this.addExpense = new Scene(eventPropGrouper.addExpense().getValue());
        this.manageParticipantsCtrl = eventPropGrouper.manageParticipants().getKey();
        this.manageParticipants = new Scene(eventPropGrouper.manageParticipants().getValue());
        this.addParticipantCtrl = eventPropGrouper.addParticipant().getKey();
        this.addParticipant = new Scene(eventPropGrouper.addParticipant().getValue());
        this.editParticipantCtrl = eventPropGrouper.editParticipant().getKey();
        this.editParticipant = new Scene(eventPropGrouper.editParticipant().getValue());
        this.statisticsCtrl = eventPropGrouper.statistics().getKey();
        this.statistics = new Scene(eventPropGrouper.statistics().getValue());
        this.debtCtrl = eventPropGrouper.debts().getKey();
        this.debts = new Scene(eventPropGrouper.debts().getValue());
        this.userEventListCtrl = userEventList.getKey();
        this.userEventList = new Scene(userEventList.getValue());
        this.createEventCtrl = createEvent.getKey();
        this.createEvent = new Scene(createEvent.getValue());
        this.settingCtrl = settings.getKey();
        this.settings = new Scene(settings.getValue());
        this.editEvent = new Scene(eventPropGrouper.editEvent().getValue());
        this.editEventCrtl = eventPropGrouper.editEvent().getKey();
        this.editExpense = new Scene(eventPropGrouper.editExpense().getValue());
        this.editExpenseCtrl = eventPropGrouper.editExpense().getKey();
        this.serverCtrl = server.getKey();
        this.server = new Scene(server.getValue());

        settingCtrl.initializeConfig();
        serverUtils = new ServerUtils();
        ServerUtils.serverDomain = settingCtrl.getConnection();
        ServerUtils.resetServer();
        setupConnection();
        setLanguage();
        showStartScreen();
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setupConnection() {
        ServerUtils.serverDomain = settingCtrl.getConnection();
        ServerUtils.resetServer();
    }

    public void showServerStartup(boolean connectionDown) {
        primaryStage.setScene(server);
        primaryStage.setTitle("Server");
        serverCtrl.setFields(connectionDown);
    }

    private void setLanguage() {
        languages = new ArrayList<>();
        //TODO we should add the available languages perhaps to a file
        languages.addAll(List.of("en", "nl", "is", "zh", "es"));
        this.setLanguage = new SetLanguage(startScreenCtrl, splittyOverviewCtrl,
                addExpenseCtrl, adminLoginCtrl, adminOverviewCtrl, createEventCtrl,
                settingCtrl, statisticsCtrl, serverCtrl, invitationCtrl, manageParticipantsCtrl,
                editParticipantCtrl, addParticipantCtrl, editExpenseCtrl, editEventCrtl);
        startScreenCtrl.setLanguageSelect();
        splittyOverviewCtrl.setLanguageSelect();
        startScreenCtrl.changeLanguage();

    }

    public void changeLanguage(String toLang) {
        this.language = toLang;
        setLanguage.changeTo(toLang);
        splittyOverviewCtrl.setLanguageSelect();
        startScreenCtrl.setLanguageSelect();
    }

    public String translate(String query) {
        if (this.language.equals("en")) return query;
        return setLanguage.translate(query, "en", this.language);
    }

    public void showSplittyOverview(int id) {
        try {
            Event event = serverUtils.getEventById(id);
            splittyOverviewCtrl.initializeAll(event);
            splittyOverview.getStylesheets().add(css);
            splittyOverviewCtrl.setEventCode(id);
            primaryStage.setTitle("Event overview");
            primaryStage.setScene(splittyOverview);
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }

    }

    public void setAdmin(Boolean admin) {
        splittyOverviewCtrl.setAdmin(admin);
    }

    public Image getFlag() {
        return setLanguage.getFlag(this.language);

    }

    public List<Event> getMyEvents() {
        if (settingCtrl != null) {
            events = serverUtils.getEventsByParticipant(settingCtrl.getId());
            return events;
        }
        return null;
    }

    public Participant joinEvent(String inviteCode) throws RuntimeException {
        String name = settingCtrl.getName();
        if (name == null || name.isEmpty()) {
            name = "Unknown";
        }

        ParticipantDTO participantDTO = new ParticipantDTO(
                name,
                0,
                settingCtrl.getIban(),
                settingCtrl.getBic(),
                settingCtrl.getEmail(),
                settingCtrl.getName(),
                -1,
                settingCtrl.getId(),
                inviteCode
        );
        participantDTO.setGhostStatus(false);
        Participant participant = serverUtils.createParticipant(participantDTO);
        if (participant != null) {
            events.add(serverUtils.getEventById(participant.getEvent().getId()));
        }
        return participant;
    }


    public void showAddExpense(String title, int eventCode) {
        try {
            primaryStage.setTitle("Add expense");
            addExpenseCtrl.refresh(eventCode);
//            addExpenseCtrl.setTitle(title);
            primaryStage.setScene(addExpense);
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }
    }

    public void showInvitation(int eventID) {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
        Event event = serverUtils.getEventById(eventID);
        invitationCtrl.setEventCode(eventID);
        invitationCtrl.setInviteCode(event.getInviteCode());
        invitationCtrl.showInviteCode();
        invitationCtrl.setTitle(event.getName());
    }

    public void showAdminLogin() {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setTitle("Server management login");
        adminLoginCtrl.reset();
        primaryStage.setScene(adminLogin);
    }

    public void showAdminOverview() {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        adminOverviewCtrl.refreshEvents();
        primaryStage.setTitle("Admin management overview");
        primaryStage.setScene(adminOverview);
    }

    /**
     * show start screen normal
     */
    public void showStartScreen() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(startScreen);
        try {
            startScreenCtrl.fetchList();
            startScreenCtrl.setNoEventsError(false);
        } catch (RuntimeException e) {
            startScreenCtrl.setNoEventsError(true);
        }
    }


    public void showUserEventList() {
        try {
            userEventListCtrl.initialize();
            primaryStage.setScene(userEventList);
            userEventListCtrl.reset();
            primaryStage.setTitle("Event List");
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }
    }

    public void showCreateEvent(String name) {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setTitle("Create Event");
        primaryStage.setScene(createEvent);
        createEventCtrl.resetValues();
        createEventCtrl.setTitle(name);
    }

    /**
     * Shows the participants manager
     *
     * @param eventID the title of the current event
     */
    public void showParticipantManager(int eventID) {
        try {
            primaryStage.setTitle("ManageParticipants");
            primaryStage.setScene(manageParticipants);
            manageParticipantsCtrl.setupParticipants(eventID);
        } catch (RuntimeException e) {
            //checkConnection();
        }
    }

    /**
     * This shows the statistics window
     *
     * @param title     the title of the current event
     * @param eventCode
     */
    public void showStatistics(String title, int eventCode) {
        try {
            primaryStage.setTitle("Statistics");
            primaryStage.setScene(statistics);
            statisticsCtrl.setTitle(title);
            //this sets the statistics, eventually this should be linked to the statistics class
            statisticsCtrl.setEventCode(eventCode);
            statisticsCtrl.refresh();
            statisticsCtrl.fetchStat();
            //set the pieChart
            statisticsCtrl.setPieChart();
            statisticsCtrl.showHoverLabel();
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }
    }

    public int getCurrentEventCode() {
        if (splittyOverviewCtrl == null) {
            throw new RuntimeException("Splitty overview controller is null," +
                    " exception thrown in MainCtrl getCurrentEventCode()");
        }
        return splittyOverviewCtrl.getCurrentEventId();
    }

    /**
     * this shows the statistics window
     */
    public void viewDeptsPerEvent(int eventCode) {
        try {
            primaryStage.setTitle("Debts per event");
            primaryStage.setScene(debts);
            debtCtrl.refresh(eventCode);
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }
    }

    public void showSettings(boolean startup) {
        primaryStage.setScene(settings);
        primaryStage.setTitle("Settings");
        settingCtrl.initializeFields(startup);
    }


    public void showEditEvent(int eventID) {
        primaryStage.setTitle("EditEvent");
        editEventCrtl.setEventId(eventID);
        editEventCrtl.setOldEventName();
        editEventCrtl.reset();
        primaryStage.setScene(editEvent);
    }

    public void setConfirmationSettings() {
        startScreenCtrl.setSettingsSavedLabel();
    }

    public void setConfirmationJoinedEvent() {
        splittyOverviewCtrl.setJoinedEventLabel();
    }

    public void setConfirmationEventCreated() {
        splittyOverviewCtrl.setEventCreatedLabel();
    }

    public void addEvent(Event event) {
        startScreenCtrl.addEvent(event);
    }

    public void setButtonGreenProperty(Button button) {
        button.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #2a8000; -fx-border-color: #365eff; " +
                        "-fx-border-width: 1px; -fx-border-radius: 2");
            } else {
                button.setStyle("-fx-background-color: #2a8000;");
            }
        });
    }

    public void setButtonRedProperty(Button button) {
        button.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #c50000; -fx-border-color: #365eff; " +
                        "-fx-border-width: 1px; -fx-border-radius: 2");
            } else {
                button.setStyle("-fx-background-color: #c50000;");
            }
        });
    }

    public void showEditExpense(Expense expense) {
        try {
            primaryStage.setTitle("Edit expense");
            editExpenseCtrl.refresh(expense);
            primaryStage.setScene(editExpense);
        } catch (RuntimeException e) {
            e.printStackTrace();
            checkConnection();
        }
    }

    public boolean getConnection() {
        try {
            serverUtils.getAllEvents();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void checkConnection() {
        if (!getConnection()) {
            startScreenCtrl.setNoEventsError(true);
            primaryStage.setScene(startScreen);
        } else {
            //something went wrong...
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Something went wrong");
            error.setContentText("We are sorry for the inconvenience but something went wrong, " +
                    "you can try to restart the server and app");
            error.showAndWait();
        }
    }

    public void closeStage() {
        primaryStage.close();
    }


    public double getAmountInDifferentCurrency(commons.Currency from, Currency to,
                                               Date date, double amount) {
        if (from == to) return amount;
        String dateString = getDateString(date);
        Conversion conversion = serverUtils.getConversion(from, to, dateString);
        if (conversion == null) throw new RuntimeException("Failed to convert amount");
        return amount * conversion.conversionRate();
    }

    public String getDateString(Date date) {
        String dateString = ((date.getDate() < 10) ? "0" : "")
                + date.getDate() + "-"
                + ((date.getMonth() < 9) ? "0" : "")
                + (date.getMonth() + 1)
                + "-" + (1900 + date.getYear());
        return dateString;
    }

    public String getFormattedDoubleString(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(d);
    }

    public void showAddParticipant(int eventId) {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setScene(addParticipant);
        primaryStage.setTitle("Add participant");
        this.addParticipantCtrl.setEventId(eventId);
    }

    public void showEditParticipant(int eventId, String participantId) {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setScene(editParticipant);
        primaryStage.setTitle("Edit participant");
        this.editParticipantCtrl.setEventId(eventId);
        this.editParticipantCtrl.autoFillWithMyData(participantId);
        this.editParticipantCtrl.setHost(true);
    }

    // one is accessed through the participant manager and the other through Splitty overview
    public void showEditParticipant(int eventId) {
        if (!getConnection()) {
            showStartScreen();
            return;
        }
        primaryStage.setScene(editParticipant);
        primaryStage.setTitle("Edit participant");
        this.editParticipantCtrl.setEventId(eventId);
        this.editParticipantCtrl.autoFillWithMyData();
        this.editParticipantCtrl.setHost(false);
    }

    public Expense addExpense(String description, Type type, Date date, Double amountDouble,
                              Participant payer, int eventCode, boolean isSharedExpense, List<Participant> owing) {
        ExpenseDTO exp =
                new ExpenseDTO(eventCode, description, type, date, amountDouble, payer.getUuid(),isSharedExpense);
        Expense expense = serverUtils.addExpense(exp);
        if(isSharedExpense) addSharedExpense(amountDouble, expense, payer,owing, eventCode);
        else addGivingMoneyToSomeone(amountDouble, expense, payer, owing.getFirst(), eventCode);
        serverUtils.send("/app/updateExpense", expense);
        return expense;
    }


    private void addGivingMoneyToSomeone(double amountDouble, Expense expense, Participant payer,
                                         Participant receiver, int eventCode) {
        serverUtils.saveDebt(
                new DebtDTO(-amountDouble, eventCode, expense.getExpenseId(), receiver.getUuid()));
        serverUtils.updateParticipant(receiver.getUuid(),
                new ParticipantDTO(receiver.getName(), receiver.getBalance() - amountDouble, receiver.getIBan(),
                        receiver.getBIC(), receiver.getEmail(),
                        receiver.getAccountHolder(), receiver.getEvent().getId(),
                        receiver.getUuid()));
        serverUtils.saveDebt(
                new DebtDTO(amountDouble, eventCode, expense.getExpenseId(), payer.getUuid()));
        serverUtils.updateParticipant(payer.getUuid(),
                new ParticipantDTO(payer.getName(), payer.getBalance() + amountDouble, payer.getIBan(),
                        payer.getBIC(), payer.getEmail(), payer.getAccountHolder(), payer.getEvent().getId(),
                        payer.getUuid()));
    }

    private void addSharedExpense(double amountDouble, Expense expense, Participant payer,
                                  Collection<Participant> owing, int eventCode) {
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
                new ParticipantDTO(payer.getName(),
                    payer.getBalance() + amountDouble - amountPerPerson, payer.getIBan(),
                        payer.getBIC(), payer.getEmail(), payer.getAccountHolder(), payer.getEvent().getId(),
                        payer.getUuid()));
    }

    public void editExpense(int expenseId, String description, Type type, Date date, Double amountDouble,
                            Participant payer, int eventCode, boolean isSharedExpense, List<Participant> owing) {
        ExpenseDTO
                exp =
                new ExpenseDTO(eventCode, description, type, date, amountDouble,
                        payer.getUuid(),isSharedExpense);
        Expense editedExpense = serverUtils.updateExpense(expenseId, exp);
        if(isSharedExpense) editSharedExpense(editedExpense, payer, amountDouble, eventCode, owing);
        else editGivingMoneyToSomeone(editedExpense, payer, amountDouble, owing.getFirst(), eventCode);
        serverUtils.generatePaymentsForEvent(eventCode);
        serverUtils.send("/app/updateExpense", editedExpense);
    }

    private void editGivingMoneyToSomeone(Expense editedExpense, Participant oldPayer,
                                          double amountDouble, Participant receiver, int eventCode) {
        Participant newReceiver = serverUtils.getParticipant(
                receiver.getUuid(), receiver.getEvent().getId());
        serverUtils.saveDebt(
                new DebtDTO(-amountDouble, eventCode, editedExpense.getExpenseId(),
                        newReceiver.getUuid()));
        serverUtils.updateParticipant(newReceiver.getUuid(),
                new ParticipantDTO(newReceiver.getName(),
                        newReceiver.getBalance() - amountDouble, newReceiver.getIBan(),
                        newReceiver.getBIC(), newReceiver.getEmail(), newReceiver.getAccountHolder(),
                        newReceiver.getEvent().getId(),
                        newReceiver.getUuid()));
        Participant newPayer = serverUtils.getParticipant(
                oldPayer.getUuid(), oldPayer.getEvent().getId());
        serverUtils.saveDebt(
                new DebtDTO(amountDouble, eventCode, editedExpense.getExpenseId(),
                        newPayer.getUuid()));
        serverUtils.updateParticipant(newPayer.getUuid(),
                new ParticipantDTO(newPayer.getName(),
                        newPayer.getBalance() + amountDouble, newPayer.getIBan(),
                        newPayer.getBIC(), newPayer.getEmail(), newPayer.getAccountHolder(),
                        newPayer.getEvent().getId(),
                        newPayer.getUuid()));
    }

    private void editSharedExpense(Expense editedExpense,
                                   Participant oldPayer, double amountDouble, int eventCode,
                                   Collection<Participant> owing) {
        double amountPerPerson = editedExpense.getTotalExpense() / (owing.size()+1);
        for (Participant oldP : owing) {
            Participant p = serverUtils.getParticipant(
                    oldP.getUuid(), oldP.getEvent().getId());
            serverUtils.saveDebt(
                    new DebtDTO(-amountPerPerson, eventCode, editedExpense.getExpenseId(),
                            p.getUuid()));
            serverUtils.updateParticipant(p.getUuid(),
                    new ParticipantDTO(p.getName(), p.getBalance() - amountPerPerson,
                            p.getIBan(),
                            p.getBIC(), p.getEmail(), p.getAccountHolder(), p.getEvent().getId(),
                            p.getUuid()));
        }
        Participant newPayer = serverUtils.getParticipant(
                oldPayer.getUuid(), oldPayer.getEvent().getId());
        serverUtils.saveDebt(
                new DebtDTO(amountDouble - amountPerPerson,
                        eventCode, editedExpense.getExpenseId(), newPayer.getUuid()));
        serverUtils.updateParticipant(newPayer.getUuid(),
                new ParticipantDTO(newPayer.getName(),
                        newPayer.getBalance() + amountDouble - amountPerPerson, newPayer.getIBan(),
                        newPayer.getBIC(), newPayer.getEmail(), newPayer.getAccountHolder(),
                        newPayer.getEvent().getId(),
                        newPayer.getUuid()));
    }

    public void setConfirmationEditParticipant() {
        manageParticipantsCtrl.setParticipantEditedConfirmation();
    }

    public void setConfirmationAddParticipant() {
        manageParticipantsCtrl.setParticipantAddedConfirmation();
    }

    public void stopLongPolling() {
        if (splittyOverviewCtrl != null) splittyOverviewCtrl.stopUpdates();
    }

    public void updateOverviewUndoStacks(Expense expense, List<Debt> debts, String method){
        splittyOverviewCtrl.pushUndoStacks(expense, debts, method);
    }

    public void showUndoInOverview(){
        splittyOverviewCtrl.showUndo();
    }
}

