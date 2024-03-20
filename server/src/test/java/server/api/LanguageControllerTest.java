package server.api;

import org.junit.jupiter.api.Test;
import server.api.depinjectionUtils.IOUtilActual;

import static org.junit.jupiter.api.Assertions.*;

class LanguageControllerTest {
    LanguageController l = new LanguageController(new IOUtilActual());

    @Test
    public void testCheckFile(){
        System.out.println(l.translate("Admin overview", "en", "nl").getBody());
    }
}