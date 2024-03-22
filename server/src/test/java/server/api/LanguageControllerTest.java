package server.api;

import org.junit.jupiter.api.Test;
import server.api.depinjectionUtils.IOUtilActual;
import server.api.depinjectionUtils.LanguageResponse;
import server.api.testmocks.LanguageResponseTest;

import static org.junit.jupiter.api.Assertions.*;

class LanguageControllerTest {
    LanguageResponse response = new LanguageResponseTest();
    LanguageController l = new LanguageController(new IOUtilActual(), response);


    @Test
    public void testCheckFile(){
        System.out.println(l.translate("Admin overview", "en", "it").getBody());
    }
}