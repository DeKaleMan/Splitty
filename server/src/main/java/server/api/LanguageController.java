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
import server.api.depinjectionUtils.IOUtil;
import server.api.depinjectionUtils.LanguageResponse;

import java.io.*;


@RestController
@RequestMapping("/api/translate")
public class LanguageController {

    private final IOUtil io;
    private String filepath;
    private String basepath;

    private LanguageResponse translator;

    @Autowired
    public LanguageController(IOUtil ioUtil, LanguageResponse translator) {
        this.io = ioUtil;
        this.filepath = getClass().getClassLoader().getResource("Languages/nl.json").getFile();
        this.filepath = this.filepath.replaceAll("%20", " ");
        this.basepath = getClass().getClassLoader().getResource("Languages/").getFile();
        this.basepath = this.basepath.replaceAll("%20", " ");
        this.translator = translator;
    }

    @GetMapping(path = {"/", ""})
    public ResponseEntity<String> translate(@RequestParam String query,
                                            @RequestParam String sourceLang, @RequestParam String targetLang) {
        if (query == null || query.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("empty query");
        }
        File newfile = new File(basepath + targetLang + ".json");
        //File mynewFile= new File("src/main/resources/Languages/" + targetLang + ".json");
        if(newfile.exists()){
            // Read JSON file
            JsonObject object = translator.readJsonFile(newfile);
            // Check if translation exists in JSON
            String translation = getTranslationFromJson(query, object);
            if (translation != null) {
                return ResponseEntity.ok(translation);
            }

        }
        String translation = translator.translateWithAPI(query, sourceLang, targetLang);
        if(translation.equals(HttpStatus.FORBIDDEN.toString())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not a valid language");
        }
        try {
            if (newfile.createNewFile()) {
                try (FileWriter fileWriter = new FileWriter(newfile)) {
                    // Write an empty JSON object to the file
                    fileWriter.write("{}");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // mynewFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Translate using external API

        // Write translation to JSON file
        JsonObject object = translator.readJsonFile(newfile);
        object.addProperty(query, translation);
        writeJsonFile(object, newfile);

        return ResponseEntity.ok(translation);


    }
    private String getTranslationFromJson(String query, JsonObject object) {
        if (object.has(query)) {
            return object.get(query).getAsString();
        }
        return null;
    }

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
