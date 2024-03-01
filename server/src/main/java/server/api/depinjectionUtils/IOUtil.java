package server.api.depinjectionUtils;

public interface IOUtil {

    void setFile(String filePath);

    String read();

    void write(String string);

}
