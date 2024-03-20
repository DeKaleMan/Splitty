package client.utils;

import client.scenes.MainCtrl;
import client.scenes.StartScreenCtrl;

import commons.Quote;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class SetLanguage {
    private static final String SERVER = "http://localhost:8080/";
    private MainCtrl mainCtrl;
    private Language language;
    private StartScreenCtrl startScreenCtrl;
    public SetLanguage(StartScreenCtrl startScreenCtrl){
        this.mainCtrl = new MainCtrl();
        this.startScreenCtrl = startScreenCtrl;
        this.language = Language.ENGLISH;
    }


    public void setMainScreen(){
        startScreenCtrl.setCreateEventText(translate("Create event", "en", "nl"));
        startScreenCtrl.setJoinEventText(translate("Join event", "en", "nl"));
        startScreenCtrl.setAdminLoginText(translate("Admin login", "en", "nl"));
        startScreenCtrl.set
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
        return response.readEntity(String.class);

    }



}
