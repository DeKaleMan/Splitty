package client.utils;

import java.io.File;

public interface ClientFileIOutil {

    default String getDataFolder() {
        return System.getProperty("user.dir") + File.separator + "SplittyClientData";
    }
    default String getFlagFolder(){
        return getDataFolder() + File.separator + "flags";
    }
    default String getConfigFile(){
        return getDataFolder() + File.separator + "Config.json";
    }
    default String getLangFile(){
        return getDataFolder() + File.separator + "langFile.json";
    }

    String read(File file);

    void write(String string, File file);

    boolean fileExists(File file);

    public boolean createNewFile(File file);

    boolean createFileStructure();
}
