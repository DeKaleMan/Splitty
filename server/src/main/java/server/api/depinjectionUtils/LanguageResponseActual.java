package server.api.depinjectionUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
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
    public String getTranslationFromJson(String query, JsonObject object) {
        if (object.has(query)) {
            return object.get(query).getAsString();
        }
        return null;
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

            //Retrieve and print the response body
            String responseBody = response.readEntity(String.class);
            // Parse the JSON response
            JSONObject jsonResponse1 = new JSONObject(responseBody);
            var responseData = jsonResponse1.get("responseData");
            Scanner scanner = new Scanner(responseData.toString());
            scanner.useDelimiter(",");
            String translated = scanner.next().substring("{_translatedText_:".length());
            translated = translated.substring(1, translated.length() -1);

            return translated;
        } catch (IOException e) {
            throw new RuntimeException("Error translating with API", e);
        }
    }
    @Override
    public void writeJsonFile(JsonObject object, File file) {
        try (FileWriter fileWriter = new FileWriter(file.getPath())) {
            Gson gson = new Gson();
            gson.toJson(object, fileWriter);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON object to file", e);
        }
    }
}
