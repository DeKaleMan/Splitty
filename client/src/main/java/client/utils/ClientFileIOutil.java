package client.utils;

import java.io.File;

public interface ClientFileIOutil {

    default String getDataFolder() {
        return System.getProperty("user.dir") + File.separator + "SplittyClientData";
    }

    default String getConfigFile(){
        return getDataFolder() + File.separator + "Config.json";
    }

    String read(File file);

    void write(String string, File file);

    boolean fileExists(File file);

    boolean createFileStructure();
}
