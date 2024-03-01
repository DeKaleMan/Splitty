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
public class FileIOUtil implements IOUtil{

    private File file;

    @Override
    public void setFile(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public String read() {
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
    public void write(String string) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
