package server.api.depinjectionUtils;

import com.google.gson.JsonObject;

import java.io.File;

public interface LanguageResponse {
    JsonObject readJsonFile(File file);
    String translateWithAPI(String query, String sourceLang, String targetLang);

    String getTranslationFromJson(String query, JsonObject object);
    void writeJsonFile(JsonObject object, File file);
}
