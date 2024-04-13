package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.depinjectionUtils.ServerIOUtil;
import server.api.depinjectionUtils.LanguageResponse;

import java.io.*;
import java.util.*;


@RestController
@RequestMapping("/api/translate")
public class LanguageController {

    private final ServerIOUtil serverIoUtil;
    private String basepath;

    private LanguageResponse translator;
    private boolean translationInProgress = false;


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
            try{
                HashMap<String, String> object = serverIoUtil.readJson(newfile);
                String translation = object.get(query);
                if (translation != null && !translation.isEmpty()) {
                    return ResponseEntity.ok(translation);
                }
            }catch (Exception e){
                System.out.println(e);
            }
            // Check if translation exists in JSON
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

        ObjectMapper objectMapper = new ObjectMapper();
        synchronized (newfile){
            try {
                HashMap<String, String> object = serverIoUtil.readJson(newfile);
                object.put(query, translation);
                serverIoUtil.write(objectMapper.writeValueAsString(object), newfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok(translation);
    }

    @GetMapping("json")
    public synchronized ResponseEntity<String> getJSONLang(@RequestParam String lang){

        try {
            File file = new File(basepath + lang + ".json");
            if (serverIoUtil.fileExists(file)) {
                String jsonString = serverIoUtil.read(file);
                return ResponseEntity.ok(jsonString);
            }
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @PutMapping("write")
    public synchronized ResponseEntity<String> writeJSONLang(@RequestBody String jsonObject, @RequestParam String lang){
        try{
            File file = new File(basepath + lang + ".json");
            serverIoUtil.write(jsonObject, file);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("succes");
    }

}
