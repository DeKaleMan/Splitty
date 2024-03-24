package server.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.depinjectionUtils.IOUtilActual;
import server.api.depinjectionUtils.LanguageResponse;
import server.api.testmocks.LanguageResponseTest;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class LanguageControllerTest {

    @Mock
    private IOUtilActual ioUtil;

    @Mock
    LanguageResponse response;
    @InjectMocks
    LanguageController l;


    @Test
    public void testJSONFile(){
        LanguageResponse responseMock = mock(LanguageResponse.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TestString", "TestStringResult");
        when(responseMock.readJsonFile(any(File.class))).thenReturn(jsonObject);

        LanguageController l = new LanguageController(null, responseMock);

        String test = "TestString";
        ResponseEntity<String> result = l.translate(test, "en", "de");
        assertEquals(ResponseEntity.ok("TestStringResult"), result);

    }
    @Test
    public void testWithAPI(){
        LanguageResponse responseMock = mock(LanguageResponse.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TestString", "TestStringResult");
        String test = "TestString2";
        when(responseMock.readJsonFile(any(File.class))).thenReturn(jsonObject);
        when(responseMock.translateWithAPI(test, "en", "de"))
                .thenReturn("TestStringResult2");

        LanguageController l = new LanguageController(null, responseMock);


        ResponseEntity<String> result = l.translate(test, "en", "de");

        assertEquals(ResponseEntity.ok("TestStringResult2"), result);
    }

    @Test
    public void incorrectLanguage(){
        String test = "TestString";
        LanguageResponse responseMock = mock(LanguageResponse.class);
        LanguageController l = new LanguageController(null, responseMock);
        ResponseEntity<String> response1 = l.translate(test, "en", "noLang");
        ResponseEntity<String> response2 = l.translate(test, "noLang", "en");
        ResponseEntity<String> response3 = l.translate(test, "en", "en");

        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response3.getStatusCode());

    }

    @Test
    public void emptyTranslation(){
        String test = "";
        LanguageResponse responseMock = mock(LanguageResponse.class);
        LanguageController l = new LanguageController(null, responseMock);
        ResponseEntity<String> response1 = l.translate(test, "en", "de");
        ResponseEntity<String> response2 = l.translate(null, "en", "es");
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());;
    }

    @Test
    public void writeTest(){

        LanguageResponse responseMock = mock(LanguageResponse.class);
        LanguageController l = new LanguageController(null, responseMock);
        // Prepare test data
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        // Create a temporary file for testing
        File tempFile;
        try {
            tempFile = File.createTempFile("test", ".json");
        } catch (IOException e) {
            fail("Failed to create temporary file: " + e.getMessage());
            return;
        }

        // Call the method under test
        l.writeJsonFile(jsonObject, tempFile);

        // Verify that the file was written correctly
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(tempFile)) {
            JsonObject parsedObject = gson.fromJson(fileReader, JsonObject.class);
            assertEquals(jsonObject, parsedObject);
        } catch (IOException e) {
            fail("Failed to read file: " + e.getMessage());
        }
    }
}