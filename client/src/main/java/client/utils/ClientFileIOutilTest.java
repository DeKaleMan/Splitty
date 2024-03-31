package client.utils;

import java.io.File;
import java.util.ArrayList;

public class ClientFileIOutilTest implements ClientFileIOutil {

    public File file;
    public String lastWrite;
    public String nextRead = "";
    public boolean rateCached = false; // flag to control if it should run the program as if the rate is cached or not
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
        return rateCached;
    }

    public ArrayList<String> clearCallList() {
        ArrayList<String> result = new ArrayList<>(ioCalls);
        ioCalls.clear();
        return result;
    }

    @Override
    public boolean createFileStructure() {
        return false;
    }
}
