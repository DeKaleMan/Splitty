package server.api.depinjectionUtils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.Scanner;
@Component
public class LanguageResponseActual implements LanguageResponse {

    public JsonObject readJsonFile(File file) {
        try (Reader reader = new FileReader(file.getPath())) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }
    @Override
    public String translateWithAPI(String query, String sourceLang, String targetLang) {
        String apiUrl = "https://api.mymemory.translated.net/get";
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String langpair = sourceLang + "%7C" + targetLang;
            String url = String.format("%s?q=%s&langpair=%s", apiUrl, encodedQuery, langpair);

            Client client = ClientBuilder.newClient();
            Response response = client
                    .target(url)
                    .request()
                    .get();

            if(response.getStatus() == HttpStatus.FORBIDDEN.value()){
                return HttpStatus.FORBIDDEN.toString();
            }
            if(response.getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()){
                return "not a valid language";
            }


            //Retrieve and print the response body
            String responseBody = response.readEntity(String.class);
            // Parse the JSON response
            JSONObject jsonResponse1 = new JSONObject(responseBody);
            String translated = jsonResponse1.getJSONObject("responseData").get("translatedText").toString();
            if(translated.contains("INVALID TARGET LANGUAGE")) translated = "null";
            //            Scanner scanner = new Scanner(responseData.toString());
//            scanner.useDelimiter(",");
//            String translated = scanner.next().substring("{_translatedText_:".length());
//            translated = translated.substring(1, translated.length() -1);

            return translated;
        } catch (IOException e) {
            throw new RuntimeException("Error translating with API", e);
        }
    }
}
