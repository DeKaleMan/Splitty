package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientFileIOutilActual implements ClientFileIOutil {

    private final String configJson =
            "{\"connection\":\"No server configured\",\"language\":\"en\"," +
                    "\"currency\":\"EUR\",\"email\":null,\"id\":\"" +
                    UUID.randomUUID() + "\",\"name\":null,\"iban\":null,\"bic\":null}";
    private final List<String> languages = List.of("en", "nl", "es", "zh", "is");

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
        String flagFolder = getFlagFolder();
        File flagFile = new File(flagFolder);
        if (!flagFile.isDirectory()) {
            flagFile.mkdir();
            changeStatus = true;
            System.out.println("Created flag folder");
        }
        folder = getConfigFile();
        file = new File(folder);
        try {
            if (file.createNewFile()) {
                changeStatus = true;
                // Set up basic json structure
                write(configJson, file);
                System.out.println("Created config file");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        folder = getLangFile();
        file = new File(folder);
        try {
            if (file.createNewFile()) {
                changeStatus = true;
                // Set up basic json structure
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(languages);
                write(json, file);
                System.out.println("Created languages file");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return changeStatus;
    }

    @Override
    public String read(File file) {
        try {
            createFileStructure();
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
            createFileStructure();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(string);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean createNewFile(File newfile){
        try {
            return newfile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean fileExists(File file) {
        return file.exists();
    }
}
