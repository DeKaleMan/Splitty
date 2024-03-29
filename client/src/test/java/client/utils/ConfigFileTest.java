package client.utils;

import commons.Currency;
import org.junit.jupiter.api.Test;

public class ConfigFileTest {

    @Test
    public void test(){
        Config config = new Config(null, null, null, null, null, null, null, null);
        config.write();
        config.read();
    }

}
