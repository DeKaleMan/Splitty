package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testmocks.HttpResponseTest;
import server.api.testmocks.IOUtilsTest;
import server.util.ConversionResponse;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeTest {

    private CurrencyExchange currencyExchange;

    @BeforeEach
    void initialize() {
        currencyExchange = new CurrencyExchange(new IOUtilsTest(), new HttpResponseTest());
    }


    @Test
    void invalidDoubleTest() {
        ResponseEntity<ConversionResponse> response = currencyExchange.getConvertedAmount("eurusd", "50klj");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void invalidConversionTest() {
        ResponseEntity<ConversionResponse> response = currencyExchange.getConvertedAmount("eurucd", "50");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}