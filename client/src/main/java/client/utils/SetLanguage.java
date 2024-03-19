package client.utils;

import client.scenes.MainCtrl;
import client.scenes.StartScreenCtrl;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;

public class SetLanguage {

    private String url = "https://api.mymemory.translated.net/get?q={toTranslate}&langpair={language})"; //language like: en|it
    private MainCtrl mainCtrl;
    private Language language;
    private StartScreenCtrl startScreenCtrl;
    public SetLanguage(StartScreenCtrl startScreenCtrl){
        this.mainCtrl = new MainCtrl();
        this.startScreenCtrl = startScreenCtrl;
        this.language = Language.ENGLISH;
    }


    public void setMainScreen(){
        startScreenCtrl.setCreateEventText("Maak evenement");
        startScreenCtrl.setJoinEventText("Treed toe tot evenement");
    }


    private static final String API_ENDPOINT = "https://api.mymemory.translated.net/get";

    public static String translate(String query, String sourceLang, String targetLang) {
        // Construct the URL with mandatory parameters
        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error encoding query: " + e.getMessage());
            return encodedQuery;
        }
        String langpair = sourceLang + "%7C" + targetLang;
        String apiUrl = "https://api.mymemory.translated.net/get";
        String url = String.format("%s?q=%s&langpair=%s", apiUrl, encodedQuery, langpair);

        // Create a JAX-RS client
        Client client = ClientBuilder.newClient();

        // Make a GET request to the MyMemory API
        Response response = client.target(url)
                .request()
                .get();
        // Retrieve and print the response body
        String responseBody = response.readEntity(String.class);
            // Parse the JSON response
        JSONObject jsonResponse1 = new JSONObject(responseBody);
        var responseData = jsonResponse1.get("responseData");
        Scanner scanner = new Scanner(responseData.toString());
        scanner.useDelimiter(",");
        String translated = scanner.next().substring("{_translatedText_:".length());
        translated = translated.substring(1, translated.length() -1);
        return translated;
    }


}
