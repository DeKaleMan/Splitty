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
import commons.Event;
import client.utils.Language;
import client.utils.SetLanguage;
import commons.Participant;
import commons.dto.ParticipantDTO;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class MainCtrl {
    private final String css = this.getClass().getResource("/general.css").toExternalForm();
    protected Language language = Language.en;

    private SetLanguage setLanguage;
    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private Scene invitation;
    private InvitationCtrl invitationCtrl;

    private Scene splittyOverview;
    private SplittyOverviewCtrl splittyOverviewCtrl;

    private Scene startScreen;
    private StartScreenCtrl startScreenCtrl;

    private Scene addExpense;
    private AddExpenseCtrl addExpenseCtrl ;

    private Scene contactDetails;
    private ContactDetailsCtrl contactDetailsCtrl;

    private Scene adminLogin;
    private AdminLoginCtrl adminLoginCtrl;

    private Scene adminOverview;
    private AdminOverviewCtrl adminOverviewCtrl;

    private Scene manageParticipants;
    private ManageParticipantsCtrl manageParticipantsCtrl;

    private Scene statistics;
    private StatisticsCtrl statisticsCtrl;
    private Scene debts;
    private DebtCtrl debtCtrl;
    private Scene userEventList;
    private UserEventListCtrl userEventListCtrl;
    private Scene createEvent;
    private CreateEventCtrl createEventCtrl;
    private ServerUtils serverUtils;

    //mainCtrl.initialize(primaryStage, overview, add, invitation,splittyOverview,
    //            startScreen, contactDetails, eventPropGrouper, addExpense, userEventList, createEvent);
    private Scene settings;
    private SettingsCtrl settingCtrl;


    // probably not the best place to put that here but works for now
    // maybe in the future isolate it into an eventctrl?
    private List<Event> events;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add, Pair<InvitationCtrl, Parent> invitation,
                           Pair<SplittyOverviewCtrl, Parent> splittyOverview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<ContactDetailsCtrl, Parent> contactDetails,
                           EventPropGrouper eventPropGrouper,
                           Pair<UserEventListCtrl, Parent> userEventList,
                           Pair<CreateEventCtrl, Parent> createEvent,
                           AdminWindows adminWindows,
                           Pair<SettingsCtrl, Parent> settings ) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
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
        serverUtils = new ServerUtils();
        settingCtrl.initializeConfig();
        showStartScreen();
        primaryStage.show();
        setLanguage();
    }

    public void setLanguage(){
        this.setLanguage = new SetLanguage(startScreenCtrl, splittyOverviewCtrl,
                addExpenseCtrl, adminLoginCtrl, adminOverviewCtrl);
    }

    public void changeLanguage(Language toLang){
        this.language = toLang;
        setLanguage.changeTo(toLang.toString());
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    // We should add the eventID to the parameters here so that it opens the splittyoverview of a specific event
    public void showSplittyOverview(int id){
        primaryStage.setTitle("Event overview");
        primaryStage.setScene(splittyOverview);
        splittyOverview.getStylesheets().add(css);
        Event event = serverUtils.getEventById(id);
        splittyOverviewCtrl.setTitle(event.getName());
        splittyOverviewCtrl.setEventCode(id);
        splittyOverviewCtrl.fetchParticipants();
        splittyOverviewCtrl.fetchExpenses();
    }

    public List<Event> getMyEvents(){
        if(settingCtrl!=null){
            events = serverUtils.getEventsByParticipant(settingCtrl.getId());
            return events;
        }
        return null;
    }

    public Participant joinEvent(int id){ // needs some more error handling
        Participant participant = serverUtils.createParticipant(
                new ParticipantDTO(
                        settingCtrl.getName(),
                        0,
                        settingCtrl.getIban(),
                        settingCtrl.getBic(),
                        settingCtrl.getEmail(),
                        settingCtrl.getName(),
                        id,
                        settingCtrl.getId()
                ));
        if (participant != null) {
            events.add(serverUtils.getEventById(id));
        }
        return participant;
    }


    public void showAddExpense(String title) {
        primaryStage.setTitle("Add expense");
        primaryStage.setScene(addExpense);
        addExpenseCtrl.setTitle(title);
    }

    public void showInvitation(String title){
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
        invitationCtrl.showInviteCode();
        invitationCtrl.setTitle(title);
    }

    public void showAdminLogin() {
        primaryStage.setTitle("Server management login");
        primaryStage.setScene(adminLogin);
    }

    public void showAdminOverview() {
        primaryStage.setTitle("Admin management overview");
        primaryStage.setScene(adminOverview);
    }

    /**
     * show start screen normal
     */
    public void showStartScreen(){
        primaryStage.setTitle("Splitty");
        startScreenCtrl.setLanguageSelect(language.toString());
        startScreenCtrl.initialize();
        primaryStage.setScene(startScreen);

    }

    /**
     * show start screen but with the event title which was being created
     * @param eventTitle the title of the event someone was creating
     */
    public void showStartScreen(String eventTitle){
        primaryStage.setTitle("Splitty");
        startScreenCtrl.initialize();
        primaryStage.setScene(startScreen);
        startScreenCtrl.setTitle(eventTitle);
    }

    public void showUserEventList() {
        userEventListCtrl.initialize();
        primaryStage.setScene(userEventList);
        primaryStage.setTitle("Event List");
    }

    public void showCreateEvent (String name) {
        primaryStage.setTitle("Create Event");
        primaryStage.setScene(createEvent);
        createEventCtrl.setTitle(name);
    }

    /**
     * Shows the participants manager
     * @param title the title of the current event
     */
    public void showParticipantManager(String title){
        primaryStage.setTitle("ManageParticipants");
        primaryStage.setScene(manageParticipants);
        manageParticipantsCtrl.setTitle(title);
    }

    /**
     * This shows the statistics window
     * @param title the title of the current event
     */
    public void showStatistics(String title){
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statistics);
        statisticsCtrl.setTitle(title);
        //this sets the statistics, eventually this should be linked to the statistics class
        statisticsCtrl.setFood(2);
        statisticsCtrl.setDrinks(2);
        statisticsCtrl.setTransport(2);
        statisticsCtrl.setOther(2);
        //set the pieChart
        statisticsCtrl.setPieChart();
    }

    /**
     * this shows the statistics window
     */
    public void viewDeptsPerEvent(){
        primaryStage.setTitle("Debts per event");
        primaryStage.setScene(debts);

    }

    public void showSettings() {
        primaryStage.setScene(settings);
        primaryStage.setTitle("Settings");
        settingCtrl.initializeFields();
    }

    public String getMyUuid(){
        return settingCtrl.getId();
    }

}