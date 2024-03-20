package client.utils;

import client.scenes.MainCtrl;
import client.scenes.StartScreenCtrl;

import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SetLanguage {
    private static final String SERVER = "http://localhost:8080/";
    private MainCtrl mainCtrl;
    private Language language;
    private StartScreenCtrl startScreenCtrl;
    public SetLanguage(StartScreenCtrl startScreenCtrl){
        this.mainCtrl = new MainCtrl();
        this.startScreenCtrl = startScreenCtrl;
        this.language = Language.en;
    }

    public void changeTo(String lang){
        if (lang.equals("en")){
            return;
        }
        setMainScreen(lang);
    }


    public void setMainScreen(String lang){
        startScreenCtrl.setCreateEventText(translate("Create event", "en", lang));
        startScreenCtrl.setJoinEventText(translate("Join event", "en", lang));
        startScreenCtrl.setAdminLogin(translate("Admin login", "en", lang));
        startScreenCtrl.setShowAllEvents(translate("Show all events", "en", lang));
        startScreenCtrl.setJoinButtonText(translate("Join", "en", lang));
        startScreenCtrl.setCreateButtonText(translate("Create", "en", lang));
        startScreenCtrl.setNoEventLabel(translate("You do not have any events to list still", "en", lang));
    }


    private static final String API_ENDPOINT = "https://api.mymemory.translated.net/get";


    public static String translate(String query, String sourceLang, String targetLang) {
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



}
