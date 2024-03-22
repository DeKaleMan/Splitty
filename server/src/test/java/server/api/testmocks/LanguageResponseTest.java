package server.api.testmocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.api.depinjectionUtils.LanguageResponse;

import java.io.File;

public class LanguageResponseTest implements LanguageResponse {
    @Override
    public JsonObject readJsonFile(File file) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("TestString", JsonParser.parseString("TestStringResult"));
        return jsonObject;
    }

    @Override
    public String translateWithAPI(String query, String sourceLang, String targetLang) {
        return "TestStringResult2";
    }

    @Override
    public void writeJsonFile(JsonObject object, File file) {
        System.out.println("written");
    }
}
