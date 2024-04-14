package client.utils;

import client.scenes.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;


import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    private ClientFileIOutil io;

    private ServerUtils serverUtils;
    @Inject
    public SetLanguage(StartScreenCtrl startScreenCtrl, SplittyOverviewCtrl splittyOverviewCtrl,
                       AddExpenseCtrl addExpenseCtrl, AdminLoginCtrl adminLoginCtrl,
                       AdminOverviewCtrl adminOverviewCtrl, CreateEventCtrl createEventCtrl,
                       SettingsCtrl settingsCtrl, StatisticsCtrl statisticsCtrl, ServerCtrl serverCtrl,
                       InvitationCtrl invitationCtrl, ManageParticipantsCtrl manageParticipantsCtrl,
                       EditParticipantCtrl editParticipantCtrl, AddParticipantCtrl addParticipantCtrl,
                       EditExpenseCtrl editExpenseCtrl, EditEventCrtl editEventCrtl) {
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

    public void setServerUtilsIO(ServerUtils serverUtils, ClientFileIOutil io){
        this.serverUtils = serverUtils;
        this.io = io;
    }

    public void changeTo(String lang) {

        new Thread(() -> {
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
            settingsCtrl.setLatch();
        }).start();

    }

    //TODO probably read the values from a file but this way it is already possible to do it in every language

    public void setMainScreen(String lang) {
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

    public void setSpittyoverview(String lang) {
        splittyOverviewCtrl.setExpensesText(translate("Expenses", "en", lang));
        splittyOverviewCtrl.setParticipants(translate("Participants", "en", lang));
        splittyOverviewCtrl.setBackButton(translate("back", "en", lang));
        splittyOverviewCtrl.setSettleDebtsButton(translate("Settle debts", "en", lang));
        splittyOverviewCtrl.setStatisticsButton(translate("Statistics", "en", lang));
        splittyOverviewCtrl.setAddExpenseButton(translate("Add expense", "en", lang));
        splittyOverviewCtrl.setPaidByMe(translate("Paid by me", "en", lang));
        splittyOverviewCtrl.setDeleteExpenseButton(translate("Delete", "en", lang));
        splittyOverviewCtrl.setSendInvites(translate("Send invites", "en", lang));
        splittyOverviewCtrl.setAllExpenses(translate("All", "en", lang));
        splittyOverviewCtrl.setEditExpense(translate("Edit expense", "en", lang));
        splittyOverviewCtrl.setLeaveButton(translate("Leave", "en", lang));
        splittyOverviewCtrl.setmyDetails(translate("My details", "en", lang));
        splittyOverviewCtrl.setHostOptionsButton(translate("Host options", "en",lang));
        splittyOverviewCtrl.setUndo(translate("Undo", "en",lang));

        System.out.println("event overview translated");
    }

    public void setAddExpense(String lang) {
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
        addExpenseCtrl.setGivingMoneyToSomeone(translate("Giving money to someone", "en", lang));
        addExpenseCtrl.setSharedExpense(translate("Shared expense", "en", lang));
        System.out.println("addExpense translated");
    }

    public void setAdminLogin(String lang) {
        adminLoginCtrl.setSignIn(translate("Sign in", "en", lang));
        adminLoginCtrl.setInstruction(translate("Log into your server instance", "en", lang));
        adminLoginCtrl.setSignInButton(translate("Sign in", "en", lang));
        adminLoginCtrl.setPasswordField(translate("Password", "en", lang));
        adminLoginCtrl.setBack(translate("Back", "en", lang));
        adminLoginCtrl.setPasswordInstructionsText(translate(
                "You can find your password in the console of your server instance", "en", lang));
        System.out.println("admin login translated");
    }

    public void setAdminOverview(String lang) {
        adminOverviewCtrl.setAdminManagementOverviewText(translate("Admin management overview", "en", lang));
        adminOverviewCtrl.setImportEventButtonText(translate("Import event", "en", lang));
        adminOverviewCtrl.setExportEventButtonText(translate("Export event", "en", lang));
        adminOverviewCtrl.setDeleteEventButtonText(translate("Delete event", "en", lang));
        adminOverviewCtrl.setServerTagText(translate("Server", "en", lang));
        adminOverviewCtrl.setViewEventButtonText(translate("View Event", "en", lang));
        adminOverviewCtrl.setSortByText(translate("Sort by:", "en", lang));
        adminOverviewCtrl.setLogOutButtonText(translate("Log Out", "en", lang));
        System.out.println("admin overview translated");
    }

    public void setCreateEvent(String lang) {
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

    public void setEditEvent(String lang) {
        editEventCrtl.setEventNameText(translate("New event name", "en", lang));
        editEventCrtl.setCreateButton(translate("Confirm changes", "en", lang));
        editEventCrtl.setCancelButton(translate("Cancel", "en", lang));
        editEventCrtl.setTitleError(translate("New event name required", "en", lang));
        editEventCrtl.setSuccesFullyChanged(translate("Successfully changed the name", "en", lang));
        editEventCrtl.setOldEventnameText(translate("Old event name", "en", lang));

        System.out.println("setEditEvent translated     ");
    }

    public void setStatistics(String lang) {
        statisticsCtrl.setTotalCostText(translate("Total cost of event: ",
                "en", lang));
        statisticsCtrl.setStatisticsText(translate("Statistics", "en", lang));
        statisticsCtrl.setBackButton(translate("Back", "en", lang));
        statisticsCtrl.setHoverLabel(translate("Hover over a category to see the percentage", "en", lang));
    }

    public void setSettings(String lang) {
        settingsCtrl.setSettingsText(translate("settings", "en", lang));
        settingsCtrl.setAddLanguage(translate("Add language", "en", lang));
        settingsCtrl.setAddLangText(translate("Add language", "en", lang));
        settingsCtrl.setCurrency(translate("currency", "en", lang));
        settingsCtrl.setLanguage(translate("Language", "en", lang));
        settingsCtrl.setSaveButton(translate("Save", "en", lang));
        settingsCtrl.setCancelButton(translate("Cancel", "en", lang));
        settingsCtrl.setLangInstructions(translate("Enter the language code or " +
                "the name of your imaginary language" +
                "and an image for the flag of the language you want to add", "en", lang));
        settingsCtrl.setChangServerButton(translate("Change server", "en", lang));
        settingsCtrl.setLabelEmailToken(translate("Email password token", "en", lang));
        settingsCtrl.setSendEmail(translate("Default email", "en", lang));
        settingsCtrl.setSucces(translate("Email successfully sent!", "en", lang));
        settingsCtrl.setUploadFlag(translate("Upload flag", "en", lang));
        System.out.println("settings translated");
    }

    public void setServer(String lang) {
        serverCtrl.setServerText(translate("Server", "en", lang));
        serverCtrl.setConnectButton(translate("Connect", "en", lang));
        serverCtrl.setStartupNotification(translate("No server has been found," +
                " type here the server you want to connect to", "en", lang));
        serverCtrl.setNotConnectedError(translate("Could not connect to the server " +
                "make sure the server is properly turned on and there are no typos in the " +
                "url or try again by repressing connect*", "en", lang));
        serverCtrl.setTitle(translate("Change splitty server", "en", lang));

    }

    public void setInvite(String lang) {
        invitationCtrl.setBack(translate("Back", "en", lang));
        invitationCtrl.setInviteCodeText(translate("Invite Code:", "en", lang));
        invitationCtrl.setSendEmailInvitesText(translate("Send email invites:", "en", lang));
        invitationCtrl.setInviteCodeInstructions(translate("Invite people by passing " +
                "the invite code:", "en", lang));
        invitationCtrl.setEmailArea(translate("Add your emails to send an invite to", "en", lang));
        invitationCtrl.setSendInvites(translate("Send invites", "en", lang));
        invitationCtrl.setNoEmailCredentials(translate("Specify your email credentials to send invites.", "en", lang));
        invitationCtrl.setNoEmail(translate("Please fill in an email address", "en", lang));
        System.out.println("invitation translated");
    }

    public void setManageParticipants(String lang) {
        manageParticipantsCtrl.setEditEvent(translate("Edit event", "en", lang));
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

    public void setEditParticipant(String lang) {
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

    public void setAddPartiticipant(String lang) {
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

    public void setEditExpense(String lang) {
        editExpenseCtrl.setSceneTypeText(translate("Edit Expense", "en", lang));
        editExpenseCtrl.setWhoPaid(translate("Who paid?", "en", lang));
        editExpenseCtrl.setHowMuch(translate("How much?", "en", lang));
        editExpenseCtrl.setWhen(translate("When?", "en", lang));
        editExpenseCtrl.setHowToSplit(translate("How to split?", "en", lang));
        editExpenseCtrl.setDescription(translate("Description", "en", lang));
        editExpenseCtrl.setExpenseTypetext(translate("Expense type?", "en", lang));
        editExpenseCtrl.setCommit(translate("Edit", "en", lang));
        editExpenseCtrl.setAbort(translate("Cancel", "en", lang));
        editExpenseCtrl.setSelectAll(translate("Select all", "en", lang));
        editExpenseCtrl.setSelectWhoPaid(translate("Select who paid", "en", lang));
        editExpenseCtrl.setExpenseTypeBox(translate("Select category", "en", lang));
        editExpenseCtrl.setGivingMoneyToSomeone(translate("Giving money to someone", "en", lang));
        editExpenseCtrl.setSharedExpense(translate("Shared expense", "en", lang));
        System.out.println("EditExpense translated");
    }



    public String translate(String query, String sourceLang, String targetLang) {
        String res = serverUtils.translate(query, sourceLang, targetLang);
        if (Objects.equals(res, "no translation found")){
            mainCtrl.language = "en";
            mainCtrl.resetLanguage();
            //display error
            return query;
        }
        return res;
    }

    public Image getFlag(String lang) {
        Image image = null;
        try {
            String path = io.getFlagFolder() + File.separator + lang + "Flag.png";
            File file = new File(path);
            if(!io.fileExists(file)){
                throw new RuntimeException();
            }
            image = new Image("file:" +  File.separator + File.separator + File.separator + path);
        } catch (Exception e) {
            //System.out.println(e);
            if(Objects.equals(lang, "default")) throw new RuntimeException("no flag found");
            image = getFlag("default");
        }

        return image;
    }
    public boolean addFlag(File imageFile, String lang) {
        File flagFile = new File(io.getFlagFolder()  + File.separator + lang + "Flag.png");
        try {
            // Copy the image file to the flag folder with the specified language name
            Files.copy(imageFile.toPath(), flagFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true; // Flag added successfully
        } catch (IOException e) {
            System.err.println("Error adding flag: " + e.getMessage());
            return false; // Flag not added
        }
    }

    public List<String> getLanguages(){
        List<String> list = new ArrayList<>();
        File langFile = new File(io.getLangFile());
        try {
            list = new ObjectMapper().readValue(langFile, List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean addLang(String newLang){
        File langFile = new File(io.getLangFile());
        try {
            List<String> oldList = getLanguages();
            oldList.add(newLang);
            String newList = new ObjectMapper().writeValueAsString(oldList);
            io.write(newList, langFile);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
