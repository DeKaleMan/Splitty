package server.api.testmocks;

import server.api.depinjectionUtils.IOUtil;

import java.io.File;
import java.util.ArrayList;

public class IOUtilsTest implements IOUtil {

    public File file;
    public String lastWrite;
    public String nextRead = "";
    public boolean rateCached = false;
    public ArrayList<String> ioCalls = new ArrayList<>();

    @Override
    public String read(File file) {
        this.file = file;
        ioCalls.add("read");
        return nextRead;
    }

    @Override
    public void write(String string, File file) {
        this.file = file;
        ioCalls.add("write");
        lastWrite = string;
    }

    @Override
    public boolean fileExists(File file) {
        // Using this you can control what this method returns
        // by either making the file null or just providing an empty file object
        return rateCached;
    }

    public ArrayList<String> clearCallList() {
        ArrayList<String> result = new ArrayList<>(ioCalls);
        ioCalls.clear();
        return result;
    }
}
