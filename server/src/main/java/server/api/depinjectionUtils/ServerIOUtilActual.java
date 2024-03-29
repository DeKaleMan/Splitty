package server.api.depinjectionUtils;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ServerIOUtilActual implements ServerIOUtil {

    @Override
    public boolean createFileStructure() {
        boolean changeStatus = false;
        String folder = getDataFolder();
        File file = new File(folder);
        if (!file.isDirectory()) {
            file.mkdir();
            changeStatus = true;
            System.out.println("Created data folder");
        }
        folder = getLanguagesFolder();
        file = new File(folder);
        if (!file.isDirectory()) {
            file.mkdir();
            changeStatus = true;
            System.out.println("Created languages folder");
        }
        folder = getCurrencyFolder();
        file = new File(folder);
        if (!file.isDirectory()) {
            file.mkdir();
            changeStatus = true;
            System.out.println("Created languages folder");
        }
        return changeStatus;
    }

    @Override
    public String read(File file) {
        try {
            Path path = Paths.get(file.getPath());
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void write(String string, File file) {
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean fileExists(File file) {
        return file.exists();
    }
}
