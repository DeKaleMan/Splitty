package server.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.depinjectionUtils.IOUtilActual;
import server.api.depinjectionUtils.LanguageResponse;
import server.api.testmocks.LanguageResponseTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}