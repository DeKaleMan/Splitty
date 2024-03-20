package server.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.api.depinjectionUtils.IOUtil;

import java.io.*;
import java.net.URLEncoder;
import java.util.Scanner;

@RestController
@RequestMapping("/api/translate")
public class LanguageController {

    private final IOUtil io;
    private final String filepath;

    @Autowired
    public LanguageController(IOUtil ioUtil) {
        this.io = ioUtil;
        this.filepath = getClass().getClassLoader().getResource("LanguageDutch.json").getFile();

    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<String> translate(@RequestParam String query,
                                            @RequestParam String sourceLang, @RequestParam String targetLang) {


        // Read JSON file
        JsonObject object = readJsonFile();

        // Check if translation exists in JSON
        String translation = getTranslationFromJson(query, object);
        if (translation != null) {
            return ResponseEntity.ok(translation);
        }

        // Translate using external API
        translation = translateWithAPI(query, sourceLang, targetLang);

        // Write translation to JSON file
        object.addProperty(query, translation);
        writeJsonFile(object);

        return ResponseEntity.ok(translation);
    }

    private JsonObject readJsonFile() {
        try (Reader reader = new FileReader(filepath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    private String getTranslationFromJson(String query, JsonObject object) {
        if (object.has(query)) {
            return object.get(query).getAsString();
        }
        return null;
    }

    private String translateWithAPI(String query, String sourceLang, String targetLang) {
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

    private void writeJsonFile(JsonObject object) {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            Gson gson = new Gson();
            gson.toJson(object, fileWriter);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON object to file", e);
        }
    }
}
