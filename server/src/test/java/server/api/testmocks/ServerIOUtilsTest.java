package server.api.testmocks;

import com.google.gson.JsonObject;
import commons.Conversion;
import server.api.depinjectionUtils.ServerIOUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerIOUtilsTest implements ServerIOUtil {

    public File file;
    public String lastWrite;
    public String nextRead = "";
    public String lastConversionWrite;
    public List<Conversion> nextConversionRead = new ArrayList<>();
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
        return true;
    }

    @Override
    public boolean createFileStructure() {
        ioCalls.add("createFileStructure");
        return true;
    }

    @Override
    public void writeConversionObjects(File file, List<Conversion> conversionList) {
        this.file = file;
        ioCalls.add("writeConversion");
        nextConversionRead.addAll(conversionList);
        lastConversionWrite = conversionList.toString();
    }

    @Override
    public List<Conversion> readConversionObjects(File file) {
        this.file = file;
        ioCalls.add("readConversion");
        List<Conversion> tmpConversionRead = nextConversionRead;
        return tmpConversionRead;
    }


    @Override
    public boolean createNewFile(File newfile) {
        ioCalls.add("createdFile");
        return true;
    }

    @Override
    public HashMap<String, String> readJson(File file) {
        ioCalls.add("readJson");
        return new HashMap<>();
    }


    public ArrayList<String> clearCallList() {
        ArrayList<String> result = new ArrayList<>(ioCalls);
        ioCalls.clear();
        return result;
    }
}
