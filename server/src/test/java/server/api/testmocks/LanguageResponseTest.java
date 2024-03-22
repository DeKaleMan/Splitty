package server.api.testmocks;

import com.google.gson.JsonObject;
import server.api.depinjectionUtils.LanguageResponse;

import java.io.File;

public class LanguageResponseTest implements LanguageResponse {
    @Override
    public JsonObject readJsonFile(File file) {
        return null;
    }

    @Override
    public String translateWithAPI(String query, String sourceLang, String targetLang) {
        return null;
    }

    @Override
    public String getTranslationFromJson(String query, JsonObject object) {
        return null;
    }

    @Override
    public void writeJsonFile(JsonObject object, File file) {

    }
}
