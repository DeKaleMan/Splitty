package client.utils;

import client.scenes.*;

import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.core.Response;
import javafx.scene.image.Image;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SetLanguage {
    private MainCtrl mainCtrl;
    private StartScreenCtrl startScreenCtrl;
    private SplittyOverviewCtrl splittyOverviewCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private AddExpenseCtrl addExpenseCtrl;
    private AdminOverviewCtrl adminOverviewCtrl;
    private CreateEventCtrl createEventCtrl;
    private SettingsCtrl settingsCtrl;
    private StatisticsCtrl statisticsCtrl;
    private ServerCtrl serverCtrl;
    private InvitationCtrl invitationCtrl;
    private ManageParticipantsCtrl manageParticipantsCtrl;
    private EditParticipantCtrl editParticipantCtrl;
    private AddParticipantCtrl addParticipantCtrl;
    private EditExpenseCtrl editExpenseCtrl;
    private EditEventCrtl editEventCrtl;

    public SetLanguage(StartScreenCtrl startScreenCtrl, SplittyOverviewCtrl splittyOverviewCtrl,
                       AddExpenseCtrl addExpenseCtrl, AdminLoginCtrl adminLoginCtrl,
                       AdminOverviewCtrl adminOverviewCtrl, CreateEventCtrl createEventCtrl,
                       SettingsCtrl settingsCtrl, StatisticsCtrl statisticsCtrl, ServerCtrl serverCtrl,
                       InvitationCtrl invitationCtrl, ManageParticipantsCtrl manageParticipantsCtrl,
                       EditParticipantCtrl editParticipantCtrl, AddParticipantCtrl addParticipantCtrl,
                       EditExpenseCtrl editExpenseCtrl, EditEventCrtl editEventCrtl){
        this.mainCtrl = new MainCtrl();
        this.startScreenCtrl = startScreenCtrl;
        this.splittyOverviewCtrl = splittyOverviewCtrl;
        this.addExpenseCtrl = addExpenseCtrl;
        this.adminLoginCtrl = adminLoginCtrl;
        this.adminOverviewCtrl = adminOverviewCtrl;
        this.createEventCtrl = createEventCtrl;
        this.settingsCtrl = settingsCtrl;
        this.statisticsCtrl = statisticsCtrl;
        this.serverCtrl = serverCtrl;
        this.invitationCtrl = invitationCtrl;
        this.manageParticipantsCtrl = manageParticipantsCtrl;
        this.editParticipantCtrl = editParticipantCtrl;
        this.addParticipantCtrl = addParticipantCtrl;
        this.editExpenseCtrl = editExpenseCtrl;
        this.editEventCrtl = editEventCrtl;
                //this.language = Language.en;
    }
    public void changeTo(String lang){
        System.out.println("Translate to: " + lang + "::\n");
        setMainScreen(lang);
        setSpittyoverview(lang);
        setAddExpense(lang);
        setAdminLogin(lang);
        setAdminOverview(lang);
        setCreateEvent(lang);
        setSettings(lang);
        setStatistics(lang);
        setServer(lang);
        setInvite(lang);
        setManageParticipants(lang);
        setEditParticipant(lang);
        setAddPartiticipant(lang);
        setEditExpense(lang);
        setEditEvent(lang);
        System.out.println("\nFINISHED\n");
    }

    //TODO probably read the values from a file but this way it is already possible to do it in every language

    public void setMainScreen(String lang){
        startScreenCtrl.setCreateEventText(translate("Create event", "en", lang));
        startScreenCtrl.setJoinEventText(translate("Join event", "en", lang));
        startScreenCtrl.setAdminLogin(translate("Admin Login", "en", lang));
        startScreenCtrl.setShowAllEvents(translate("Show all events", "en", lang));
        startScreenCtrl.setJoinButtonText(translate("Join", "en", lang));
        startScreenCtrl.setCreateButtonText(translate("Create", "en", lang));
        startScreenCtrl.setNoEventLabel(translate("You do not have any events to list still", "en", lang));
        startScreenCtrl.setSettings(translate("Settings", "en", lang));
        startScreenCtrl.setmyEventsText(translate("My events", "en", lang));
        startScreenCtrl.setCodeNotFoundError(translate("Event code not found", "en", lang));
        startScreenCtrl.setInvalidCodeError(translate("Event code is a number", "en", lang));
        startScreenCtrl.setSettingsSavedLabel(translate("Settings saved succesfully*", "en", lang));
        startScreenCtrl.setAlreadyParticipantError(translate("You already participate in this event",
                "en", lang));
        startScreenCtrl.setNoConnectionError(translate("Go to settings to check your connection to " +
                "the server and try to refresh or change the server*", "en", lang));
        startScreenCtrl.setMyEventsNotFoundError(translate("Could not fetch my events, " +
                "check the server availability", "en", lang));

        System.out.println("mainscreen translated");
    }
    public void setSpittyoverview(String lang){
        splittyOverviewCtrl.setExpensesText(translate("Expenses", "en", lang));
        splittyOverviewCtrl.setParticipants(translate("Participants", "en", lang));
        splittyOverviewCtrl.setBackButton(translate("back", "en", lang));
        splittyOverviewCtrl.setSettleDebtsButton(translate("Settle debts", "en", lang));
        splittyOverviewCtrl.setStatisticsButton(translate("Statistics", "en", lang));
        splittyOverviewCtrl.setAddExpenseButton(translate("Add expense", "en", lang));
        splittyOverviewCtrl.setPaidByMe(translate("Paid by me", "en", lang));
        splittyOverviewCtrl.setDeleteExpenseButton(translate("Delete expense", "en", lang));
        splittyOverviewCtrl.setSendInvites(translate("Send invites", "en", lang));
        splittyOverviewCtrl.setAllExpenses(translate("All", "en", lang));
        splittyOverviewCtrl.setEditExpense(translate("Edit expense", "en",lang));
        splittyOverviewCtrl.setEditEvent(translate("Edit event", "en",lang));
        splittyOverviewCtrl.setLeaveButton(translate("Leave", "en",lang));
        splittyOverviewCtrl.setmyDetails(translate("My details", "en",lang));
        splittyOverviewCtrl.setHostOptionsButton(translate("Host options", "en",lang));
        System.out.println("event overview translated");
    }
    public void setAddExpense(String lang){
        addExpenseCtrl.setSceneTypeText(translate("Add Expense", "en", lang));
        addExpenseCtrl.setWhoPaid(translate("Who paid?", "en", lang));
        addExpenseCtrl.setHowMuch(translate("How much?", "en", lang));
        addExpenseCtrl.setWhen(translate("When?", "en", lang));
        addExpenseCtrl.setHowToSplit(translate("How to split?", "en", lang));
        addExpenseCtrl.setDescription(translate("Description", "en", lang));
        addExpenseCtrl.setExpenseTypetext(translate("Expense type?", "en", lang));
        addExpenseCtrl.setCommit(translate("Add expense", "en", lang));
        addExpenseCtrl.setAbort(translate("Cancel", "en", lang));
        addExpenseCtrl.setSelectAll(translate("Select all", "en", lang));
        addExpenseCtrl.setSelectWhoPaid(translate("Select who paid", "en", lang));
        addExpenseCtrl.setExpenseTypeBox(translate("Select category", "en", lang));
        //addExpenseCtrl.setGivingMoneyToSomeone(translate("Giving money to someone", "en", lang));
        //addExpenseCtrl.setSharedExpense(translate("Shared expense", "en", lang));
        System.out.println("addExpense translated");
    }
    public void setAdminLogin(String lang){
        adminLoginCtrl.setSignIn(translate("Sign in", "en", lang));
        adminLoginCtrl.setInstruction(translate("Log into your server instance", "en", lang));
        adminLoginCtrl.setPasswordInstructionLink(translate("Don't know how to get a password?", "en", lang));
        adminLoginCtrl.setSignInButton(translate("Sign in", "en", lang));
        adminLoginCtrl.setUrlField(translate("Server Url e.g. 'localhost:8080'", "en", lang));
        adminLoginCtrl.setPasswordField(translate("Password", "en", lang));
        adminLoginCtrl.setBack(translate("Back", "en", lang));
        adminLoginCtrl.setPasswordInstructionsText(translate(
                "You can find your password in the console of your server instance", "en", lang));
        System.out.println("admin login translated");
    }
    public void setAdminOverview(String lang){
        adminOverviewCtrl.setAdminManagementOverviewText(translate("Admin management overview", "en", lang));
        adminOverviewCtrl.setImportEventButtonText(translate("Import event", "en", lang));
        adminOverviewCtrl.setExportEventButtonText(translate("Export event", "en", lang));
        adminOverviewCtrl.setDeleteEventButtonText(translate("Delete event", "en", lang));
        adminOverviewCtrl.setServerTagText(translate("Server: Localhost:8080", "en", lang));
        adminOverviewCtrl.setViewEventButtonText(translate("View Event", "en", lang));
        adminOverviewCtrl.setSortByText(translate("Sort by:", "en", lang));
        adminOverviewCtrl.setLogOutButtonText(translate("Log Out", "en", lang));
        System.out.println("admin overview translated");
    }
    public void setCreateEvent(String lang){
        createEventCtrl.setEventNameText(translate("Event name", "en", lang));
        createEventCtrl.setDateText(translate("Date", "en", lang));
        createEventCtrl.setEventDescriptionText(translate("Event description", "en", lang));
        createEventCtrl.setEventDescriptionArea(translate("What is the event about...", "en", lang));
        //createEventCtrl.setNameText(translate("Name", "en", lang));
        createEventCtrl.setCreateButton(translate("Create event", "en", lang));
        createEventCtrl.setCancelButton(translate("Cancel", "en", lang));
        createEventCtrl.setRequired(translate("required", "en", lang));
        System.out.println("setCreateEvent translated");
    }
    public void setEditEvent(String lang){
        editEventCrtl.setEventNameText(translate("New event name", "en", lang));
        editEventCrtl.setCreateButton(translate("Confirm event name", "en", lang));
        editEventCrtl.setCancelButton(translate("Cancel", "en", lang));
        editEventCrtl.setTitleError(translate("New event name required", "en", lang));
        editEventCrtl.setSuccesFullyChanged(translate("Successfully changed the name", "en", lang));
        editEventCrtl.setOldEventnameText(translate("Old event name", "en", lang));

        System.out.println("setEditEvent translated     ");
    }
    public void setStatistics(String lang){
        statisticsCtrl.setTotalCostText(translate("Total cost of event: ",
                "en", lang));
        statisticsCtrl.setStatisticsText(translate("Statistics", "en", lang));
        statisticsCtrl.setBackButton(translate("Back", "en", lang));
    }
    private void setSettings(String lang){
        settingsCtrl.setSettingsText(translate("settings", "en", lang));
        settingsCtrl.setAddLanguage(translate("Add language", "en", lang));
        settingsCtrl.setAddLangText(translate("Add language", "en", lang));
        settingsCtrl.setCurrency(translate("currency", "en", lang));
        settingsCtrl.setLanguage(translate("Language", "en", lang));
        settingsCtrl.setSaveButton(translate("Save", "en", lang));
        settingsCtrl.setCancelButton(translate("Cancel", "en", lang));
        settingsCtrl.setLangInstructions(translate("Enter the languagecode " +
                "and an image for the flag of the language you want to add", "en", lang));
        settingsCtrl.setChangServerButton(translate("Change server", "en", lang));
        System.out.println("settings translated");
    }
    private void setServer(String lang){
        serverCtrl.setServerText(translate("Server", "en", lang));
        serverCtrl.setConnectButton(translate("Connect", "en", lang));
        serverCtrl.setStartupNotification(translate("No server has been found," +
                " type here the server you want to connect to", "en", lang));
        serverCtrl.setNotConnectedError(translate("Could not connect to the server " +
                "make sure the server is properly turned on and there are no typos in the " +
                "url or try again by repressing connect*", "en", lang));
        serverCtrl.setTitle(translate("Change splitty server", "en", lang));

    }
    private void setInvite(String lang){
        invitationCtrl.setBack(translate("Back", "en", lang));
        invitationCtrl.setInviteCodeText(translate("Invite Code:", "en", lang));
        invitationCtrl.setSendEmailInvitesText(translate("Send email invites:", "en", lang));
        invitationCtrl.setInviteCodeInstructions(translate("Invite people by passing " +
                "the invite code:", "en", lang));
        invitationCtrl.setEmailArea(translate("Add your emails to send an invite to", "en", lang));
        invitationCtrl.setSendInvites(translate("Send invites", "en", lang));
        System.out.println("invitation translated");
    }
    private void setManageParticipants(String lang){
        manageParticipantsCtrl.setAddButton(translate("Add participants", "en", lang));
        manageParticipantsCtrl.setBackButton(translate("Back", "en", lang));
        manageParticipantsCtrl.setRemoveButton(translate("Remove participants", "en", lang));
        manageParticipantsCtrl.setEditButton(translate("Edit participants", "en", lang));
        manageParticipantsCtrl.setParticipantsText(translate("Participants", "en", lang));
        manageParticipantsCtrl.setNoParticipantSelectedError(translate("No participant selected*",
                "en", lang));
        manageParticipantsCtrl.setParticipantAddedConfirmation(translate("Participant successfully added*",
                "en", lang));
        manageParticipantsCtrl.setParticipantDeletedConfirmation(translate("Participant successfully deleted*",
                "en", lang));
        System.out.println("participant manager translated");
    }
    private void setEditParticipant(String lang){
        editParticipantCtrl.setApplyChangesButton(translate("Apply change", "en", lang));
        editParticipantCtrl.setTitle(translate("Edit participant", "en", lang));
        editParticipantCtrl.setName(translate("Name", "en", lang));
        editParticipantCtrl.setCancelButton(translate("Cancel", "en", lang));
        editParticipantCtrl.setUnknownError(translate("An unexpected error happened*", "en", lang));
        editParticipantCtrl.setInvalidBicLabel(translate("Please enter a valid BIC* (between 8-11 characters)*",
                "en", lang));
        editParticipantCtrl.setInvalidIbanLabel(translate("Please enter a valid IBAN (between 15-34 characters)*",
                "en", lang));
        editParticipantCtrl.setInvalidEmailLabel(translate("Please enter a valid email address*", "en", lang));
    }
    private void setAddPartiticipant(String lang){
        addParticipantCtrl.setApplyChangesButton(translate("Apply change", "en", lang));
        addParticipantCtrl.setTitle(translate("Add participant", "en", lang));
        addParticipantCtrl.setName(translate("Name", "en", lang));
        addParticipantCtrl.setCancelButton(translate("Cancel", "en", lang));
        addParticipantCtrl.setUnknownError(translate("An unexpected error happened*", "en", lang));
        addParticipantCtrl.setInvalidBicLabel(translate("Please enter a valid BIC* (between 8-11 characters)*",
                "en", lang));
        addParticipantCtrl.setInvalidIbanLabel(translate("Please enter a valid IBAN (between 15-34 characters)*",
                "en", lang));
        addParticipantCtrl.setInvalidEmailLabel(translate("Please enter a valid email address*", "en", lang));
    }
    public void setEditExpense(String lang){
        editExpenseCtrl.setSceneTypeText(translate("Add Expense", "en", lang));
        editExpenseCtrl.setWhoPaid(translate("Who paid?", "en", lang));
        editExpenseCtrl.setHowMuch(translate("How much?", "en", lang));
        editExpenseCtrl.setWhen(translate("When?", "en", lang));
        editExpenseCtrl.setHowToSplit(translate("How to split?", "en", lang));
        editExpenseCtrl.setDescription(translate("Description", "en", lang));
        editExpenseCtrl.setExpenseTypetext(translate("Expense type?", "en", lang));
        editExpenseCtrl.setCommit(translate("Add expense", "en", lang));
        editExpenseCtrl.setAbort(translate("Cancel", "en", lang));
        editExpenseCtrl.setSelectAll(translate("Select all", "en", lang));
        editExpenseCtrl.setSelectWhoPaid(translate("Select who paid", "en", lang));
        editExpenseCtrl.setExpenseTypeBox(translate("Select category", "en", lang));
        addExpenseCtrl.setGivingMoneyToSomeone(translate("Giving money to someone", "en", lang));
        addExpenseCtrl.setSharedExpense(translate("Shared expense", "en", lang));
        System.out.println("EditExpense translated");
    }

    private static final String API_ENDPOINT = "https://api.mymemory.translated.net/get";


    public String translate(String query, String sourceLang, String targetLang) {
        Response response = ClientBuilder.newClient()
                .target(ServerUtils.server)
                .path("api/translate")
                .queryParam("query", query)
                .queryParam("sourceLang", sourceLang)
                .queryParam("targetLang", targetLang)
                .request(APPLICATION_JSON)
                .get();
        if(response.getStatus() != Response.Status.OK.getStatusCode()) {
            response.close();
            throw new RuntimeException("Failed to retrieve language. Status code: " + response.getStatus());
        }
        String res = response.readEntity(String.class);

        return res;

    }

    public Image getFlag(String lang){
        Image image;
        try{
            String path = lang + "Flag.png";
            image =  new Image(path);
        }
        catch (Exception e){
            return null;
        }

        return image;
    }



}
