package commons.util;

import java.io.File;

public interface FileIOutilInterface {

    default String getDataFolder() {
        return System.getProperty("user.dir") + File.separator + "SplittyData";
    };

    boolean createFileStructure();

}
