package server.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.api.depinjectionUtils.ServerIOUtil;
import server.api.depinjectionUtils.LanguageResponse;

import java.io.*;
import java.util.Objects;


@RestController
@RequestMapping("/api/translate")
public class LanguageController {

    private final ServerIOUtil serverIoUtil;
    private String basepath;

    private LanguageResponse translator;

    @Autowired
    public LanguageController(ServerIOUtil serverIoUtil, LanguageResponse translator) {
        this.serverIoUtil = serverIoUtil;
        this.basepath = this.serverIoUtil.getLanguagesFolder() + File.separator;
        this.translator = translator;
    }

    private ResponseEntity<String> checkValid(String query, String sourceLang, String targetLang){
        if (query == null || query.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("empty query");
        }
        if(sourceLang.equals(targetLang)) return ResponseEntity.ok(query);
        return ResponseEntity.ok("NOPE");
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<String> translate(@RequestParam String query,
                                            @RequestParam String sourceLang, @RequestParam String targetLang) {
        ResponseEntity<String> checkValid = checkValid(query, sourceLang, targetLang);
        if(!Objects.equals(checkValid.getBody(), "NOPE")) return checkValid;
        File newfile = new File(basepath + targetLang + ".json");

        if (serverIoUtil.fileExists(newfile)) {
            // Read JSON file
            JsonObject object = translator.readJsonFile(newfile);
            // Check if translation exists in JSON
            String translation = getTranslationFromJson(query, object);
            if (translation != null) {
                return ResponseEntity.ok(translation);
            }

        }
        String translation = translator.translateWithAPI(query, sourceLang, targetLang);
        if (translation.equals(HttpStatus.FORBIDDEN.toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not a valid language");
        }
        if (serverIoUtil.createNewFile(newfile)) {
            serverIoUtil.write("{}", newfile);
        }
        // Translate using external API

        // Write translation to JSON file
        JsonObject object = translator.readJsonFile(newfile);
        object.addProperty(query, translation);
        //writeJsonFile(object, newfile);
        serverIoUtil.writeJson(object, newfile);
        return ResponseEntity.ok(translation);
    }

    private String getTranslationFromJson(String query, JsonObject object) {
        if (object.has(query)) {
            return object.get(query).getAsString();
        }
        return null;
    }

    //TODO NO FILEWRITE...move to ServerioUtils
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
