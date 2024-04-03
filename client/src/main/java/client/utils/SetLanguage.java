package client.utils;

import client.scenes.*;

import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.core.Response;
import javafx.scene.image.Image;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SetLanguage {
    private static final String SERVER = "http://localhost:8080/";
    private MainCtrl mainCtrl;
    private StartScreenCtrl startScreenCtrl;
    private SplittyOverviewCtrl splittyOverviewCtrl;
    private AdminLoginCtrl adminLoginCtrl;
    private AddExpenseCtrl addExpenseCtrl;

    private AdminOverviewCtrl adminOverviewCtrl;
    public SetLanguage(StartScreenCtrl startScreenCtrl, SplittyOverviewCtrl splittyOverviewCtrl,
                       AddExpenseCtrl addExpenseCtrl, AdminLoginCtrl adminLoginCtrl,
                       AdminOverviewCtrl adminOverviewCtrl){
        this.mainCtrl = new MainCtrl();
        this.startScreenCtrl = startScreenCtrl;
        this.splittyOverviewCtrl = splittyOverviewCtrl;
        this.addExpenseCtrl = addExpenseCtrl;
        this.adminLoginCtrl = adminLoginCtrl;
        this.adminOverviewCtrl = adminOverviewCtrl;
        //this.language = Language.en;
    }

    public void changeTo(String lang){
        if (lang.equals("en")){
            return;
        }
        setMainScreen(lang);
        setSpittyoverview(lang);
        setAddExpense(lang);
        setAdminLogin(lang);
        setAdminOverview(lang);
    }

    //TODO probably read the values from a file but this way it is already possible to do it in every language

    public void setMainScreen(String lang){
        startScreenCtrl.setCreateEventText(translate("Create event", "en", lang));
        startScreenCtrl.setJoinEventText(translate("Join event", "en", lang));
        startScreenCtrl.setAdminLogin(translate("Admin login", "en", lang));
        startScreenCtrl.setShowAllEvents(translate("Show all events", "en", lang));
        startScreenCtrl.setJoinButtonText(translate("Join", "en", lang));
        startScreenCtrl.setCreateButtonText(translate("Create", "en", lang));
        startScreenCtrl.setNoEventLabel(translate("You do not have any events to list still", "en", lang));
    }
    public void setSpittyoverview(String lang){
        splittyOverviewCtrl.setExpensesText(translate("Expenses", "en", lang));
        splittyOverviewCtrl.setParticipants(translate("Participants", "en", lang));
        splittyOverviewCtrl.setBackButton(translate("back", "en", lang));
        splittyOverviewCtrl.setSettleDebtsButton(translate("Settle debts", "en", lang));
        splittyOverviewCtrl.setStatisticsButton(translate("Statistics", "en", lang));
        splittyOverviewCtrl.setAddExpenseButton(translate("Add expense", "en", lang));
//        splittyOverviewCtrl.setEditParticipant(translate("Edit", "en", lang));
        splittyOverviewCtrl.setPaidByMe(translate("Paid by me", "en", lang));
        splittyOverviewCtrl.setDeleteExpenseButton(translate("Delete expense", "en", lang));
        splittyOverviewCtrl.setSendInvites(translate("Send invites", "en", lang));
        splittyOverviewCtrl.setAllExpenses(translate("All", "en", lang));
    }

    public void setAddExpense(String lang){
        addExpenseCtrl.setAddExpenseText(translate("Add Expense", "en", lang));
        addExpenseCtrl.setWhoPaid(translate("Who paid?", "en", lang));
        addExpenseCtrl.setHowMuch(translate("How much?", "en", lang));
        addExpenseCtrl.setWhen(translate("When?", "en", lang));
        addExpenseCtrl.setHowToSplit(translate("How to split?", "en", lang));
        addExpenseCtrl.setDescription(translate("Description", "en", lang));
        addExpenseCtrl.setExpenseTypetext(translate("Expense type?", "en", lang));
        addExpenseCtrl.setAdd(translate("Add expense", "en", lang));
        addExpenseCtrl.setAbort(translate("Cancel", "en", lang));
        addExpenseCtrl.setSelectAll(translate("Select all", "en", lang));
        addExpenseCtrl.setSelectWhoPaid(translate("Select who paid", "en", lang));
        addExpenseCtrl.setExpenseTypeBox(translate("Select category", "en", lang));
    }

    public void setAdminLogin(String lang){
        adminLoginCtrl.setSignIn(translate("Sign in", "en", lang));
        adminLoginCtrl.setInstruction(translate("Log into your server instance", "en", lang));
        adminLoginCtrl.setPasswordInstructionLink(translate("Don't know how to get a password?", "en", lang));
        adminLoginCtrl.setSignInButton(translate("Sign in", "en", lang));
        adminLoginCtrl.setUrlField(translate("Server Url e.g. 'localhost:8080'", "en", lang));
        adminLoginCtrl.setPasswordField(translate("Password", "en", lang));
        adminLoginCtrl.setPasswordInstructionsText(translate(
                "You can find your password in the console of your server instance", "en", lang));
    }

    public void setAdminOverview(String lang){
        adminOverviewCtrl.setAdminManagementOverviewText(translate("Admin management overview", "en", lang));
        adminOverviewCtrl.setImportEventButtonText(translate("Import event", "en", lang));
        adminOverviewCtrl.setExportEventButtonText(translate("Export event", "en", lang));
        adminOverviewCtrl.setDeleteEventButtonText(translate("Delete event", "en", lang));
        adminOverviewCtrl.setServerTagText(translate("Server: Localhost:8080", "en", lang));
        adminOverviewCtrl.setViewEventButtonText(translate("View Event", "en", lang));
        adminOverviewCtrl.setJsonImportTextAreaPromptText(translate("Paste your exported JSON dump here", "en", lang));
        adminOverviewCtrl.setSortByText(translate("Sort by:", "en", lang));
        adminOverviewCtrl.setLogOutButtonText(translate("Log Out", "en", lang));
    }

    private static final String API_ENDPOINT = "https://api.mymemory.translated.net/get";


    public String translate(String query, String sourceLang, String targetLang) {
        Response response = ClientBuilder.newClient()
                .target(SERVER)
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
