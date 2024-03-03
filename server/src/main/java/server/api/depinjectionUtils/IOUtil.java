package server.api.depinjectionUtils;

import java.io.File;

public interface IOUtil {

    String read(File file);

    void write(String string, File file);

    boolean fileExists(File file);

}
