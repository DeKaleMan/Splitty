package commons.util;

import java.io.File;

public interface FileIOutilInterface {

    default String getDataFolder() {
        return System.getProperty("user.dir") + File.separator + "SplittyData";
    }

    default String getLanguagesFolder(){
        return getDataFolder() + File.separator + "Languages";
    }

    default String getConfigFile(){
        return getDataFolder() + File.separator + "Config.json";
    }

    default String getCurrencyFolder(){
        return getDataFolder() + File.separator + "CurrencyCache";
    }

    String read(File file);

    void write(String string, File file);

    boolean fileExists(File file);



    boolean createFileStructure();

}
