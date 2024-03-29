package server.api.depinjectionUtils;

import java.io.File;

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

}
