package commons.util;

import java.io.File;

public class FileIOutil implements FileIOutilInterface{

    @Override
    public boolean createFileStructure() {
        String dataPath = getDataFolder();
        File dataDirectory = new File(dataPath);
        if (!dataDirectory.isDirectory()) {
            dataDirectory.mkdir();
            System.out.println("Created data folder");
            return true;
        }
        return false;
    }
}
