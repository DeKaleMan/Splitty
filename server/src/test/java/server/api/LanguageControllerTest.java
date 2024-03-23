package server.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.depinjectionUtils.IOUtilActual;
import server.api.depinjectionUtils.LanguageResponse;
import server.api.testmocks.LanguageResponseTest;

import static org.junit.jupiter.api.Assertions.*;

class LanguageControllerTest {
    LanguageResponse response = new LanguageResponseTest();
    LanguageController l = new LanguageController(new IOUtilActual(), response);


    @Test
    public void testJSONFile(){
        String test = "TestString";
        ResponseEntity<String> result = l.translate(test, "en", "Du");

        assertEquals(ResponseEntity.ok("TestStringResult"), result);

    }
    @Test
    public void testWithAPI(){
        String test = "TestString2";
        ResponseEntity<String> result = l.translate(test, "en", "Du");

        assertEquals(ResponseEntity.ok("TestStringResult2"), result);

    }
}