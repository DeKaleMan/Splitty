package server.api;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.depinjectionUtils.LanguageResponse;
import server.api.depinjectionUtils.ServerIOUtil;
import server.api.testmocks.LanguageResponseTest;
import server.api.testmocks.ServerIOUtilsTest;

import java.io.File;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class LanguageControllerTest {

    @Mock
    LanguageResponse response;
    @InjectMocks
    LanguageController l;


    @Test
    public void testJSONFile(){
        LanguageResponse responseMock = mock(LanguageResponse.class);
        String jsonObject = "{\"TestString\", \"TestStringResult\"}";
        HashMap<String, String> res = new HashMap<>();
        res.put("TestString", "TestStringResult");
        ServerIOUtil io = mock(ServerIOUtil.class);
        when(io.fileExists(any(File.class))).thenReturn(true);
        when(io.readJson(any(File.class))).thenReturn(res);
        LanguageController l = new LanguageController(io, responseMock);

        String test = "TestString";
        ResponseEntity<String> result = l.translate(test, "en", "de");
        assertEquals(ResponseEntity.ok("TestStringResult"), result);

    }
    @Test
    public void testWithAPI(){
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageResponse responseMock = mock(LanguageResponse.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TestString", "TestStringResult");
        String test = "TestString2";
        when(responseMock.readJsonFile(any(File.class))).thenReturn(jsonObject);
        when(responseMock.translateWithAPI(test, "en", "de"))
                .thenReturn("TestStringResult2");

        LanguageController l = new LanguageController(io, responseMock);


        ResponseEntity<String> result = l.translate(test, "en", "de");

        assertEquals(ResponseEntity.ok("TestStringResult2"), result);
    }

    @Test
    public void incorrectLanguage(){
        String test = "TestString";
        LanguageResponse responseMock = mock(LanguageResponse.class);
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageController l = new LanguageController(io, responseMock);
        when(responseMock.translateWithAPI(any(String.class), any(String.class), any(String.class)))
                .thenReturn(HttpStatus.FORBIDDEN.toString());
        when(responseMock.readJsonFile(any(File.class))).thenReturn(new JsonObject());
        ResponseEntity<String> response1 = l.translate(test, "en", "noLang");
        ResponseEntity<String> response2 = l.translate(test, "noLang", "en");
        ResponseEntity<String> response3 = l.translate(test, "en", "en");

        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
        assertEquals(HttpStatus.OK, response3.getStatusCode());

    }

    @Test
    public void emptyTranslation(){
        String test = "";
        LanguageResponse responseMock = mock(LanguageResponse.class);
        LanguageController l = new LanguageController(new ServerIOUtilsTest(), responseMock);
        ResponseEntity<String> response1 = l.translate(test, "en", "de");
        ResponseEntity<String> response2 = l.translate(null, "en", "es");
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());;
    }

    @Test
    public void writeJSONLang(){
        String jsonString = "{\"testString\": \"testsString\" }";
        String lang = "testLang";
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageController langC = new LanguageController(io, new LanguageResponseTest());
        langC.writeJSONLang(jsonString, lang);

        verify(io).write(anyString(), any(File.class));
    }
    @Test
    public void getJSONLang(){
        String lang = "test";
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageController langC = new LanguageController(io, new LanguageResponseTest());

        when(io.read(any(File.class))).thenReturn("succes");
        when(io.fileExists(any(File.class))).thenReturn(true);

        String res = langC.getJSONLang(lang).getBody();
        verify(io).read(any(File.class));
        assertEquals("succes", res);
    }
    @Test
    public void getWrongJSONLang(){
        String lang = "test";
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageController langC = new LanguageController(io, new LanguageResponseTest());

        when(io.read(any(File.class))).thenReturn("succes");
        when(io.fileExists(any(File.class))).thenReturn(false);

        int res = langC.getJSONLang(lang).getStatusCode().value();

        assertEquals(HttpStatus.NOT_FOUND.value(), res);
    }
    @Test
    public void getFailedToReadJSONLang(){
        String lang = "test";
        ServerIOUtil io = mock(ServerIOUtil.class);
        LanguageController langC = new LanguageController(io, new LanguageResponseTest());


        when(io.read(any(File.class))).thenThrow(new RuntimeException());
        when(io.fileExists(any(File.class))).thenReturn(true);

        ResponseEntity<String> res = langC.getJSONLang(lang);

        assertEquals(null, res);
    }
}