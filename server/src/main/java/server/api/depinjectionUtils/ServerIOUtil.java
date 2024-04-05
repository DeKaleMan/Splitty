package server.api.depinjectionUtils;

import com.google.gson.JsonObject;
import commons.Conversion;

import java.io.File;
import java.util.List;

public interface ServerIOUtil {

    String read(File file);

    void write(String string, File file);

    boolean fileExists(File file);

    default String getDataFolder() {
        return System.getProperty("user.dir") + File.separator + "SplittyServerData";
    }

    default String getLanguagesFolder(){
        return getDataFolder() + File.separator + "Languages";
    }

    default String getCurrencyCacheFile(){
        return getDataFolder() + File.separator + "CurrencyCache.json";
    }

    boolean createFileStructure();

    void writeConversionObjects(File file, List<Conversion> conversionList);

    List<Conversion> readConversionObjects(File file);
    void writeJson(JsonObject object, File file);
    boolean createNewFile(File newfile);

}
