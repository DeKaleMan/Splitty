package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testmocks.HttpResponseTest;
import server.api.testmocks.IOUtilsTest;
import server.util.ConversionResponse;
import server.util.OkConversionResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeTest {

    private CurrencyExchange currencyExchange;
    private IOUtilsTest ioTest;
    private HttpResponseTest httpResponseTest;

    @BeforeEach
    void initialize() {
        ioTest = new IOUtilsTest();
        httpResponseTest = new HttpResponseTest();
        currencyExchange = new CurrencyExchange(ioTest, httpResponseTest);
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

    @Test
    void getConvertedAmountCachedTest() {
        double epsilon = 0.00001d;
        ioTest.rateCached = true;

        ioTest.nextRead = "EUR;0.923288;CHF;0.884445";
        ResponseEntity<ConversionResponse> response = currencyExchange.getConvertedAmount("usdeur", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 46.1644, epsilon);
        assertEquals(new ArrayList<>(List.of("read")), ioTest.clearCallList());

        ioTest.nextRead = "USD;1.083229975139872;CHF;0.9539583931366549";
        response = currencyExchange.getConvertedAmount("eurusd", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 54.16149, epsilon);
        assertEquals(new ArrayList<>(List.of("read")), ioTest.clearCallList());

        ioTest.nextRead = "USD;1.1306525561227663;EUR;1.0439179372374765";
        response = currencyExchange.getConvertedAmount("chfusd", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 56.53262, epsilon);
        assertEquals(new ArrayList<>(List.of("read")), ioTest.clearCallList());
    }

    @Test
    void getConvertedAmountNotCachedTest() {
        double epsilon = 0.00001d;
        ioTest.rateCached = false;

        ioTest.nextRead = "EUR;0.923288;CHF;0.884445";
        ResponseEntity<ConversionResponse> response = currencyExchange.getConvertedAmount("usdeur", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 46.1644, epsilon);
        assertEquals(new ArrayList<>(List.of("write", "read")), ioTest.clearCallList());
        assertEquals("EUR;0.924813;CHF;0.887846", ioTest.lastWrite);

        ioTest.nextRead = "USD;1.083229975139872;CHF;0.9539583931366549";
        response = currencyExchange.getConvertedAmount("eurusd", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 54.16149, epsilon);
        assertEquals(new ArrayList<>(List.of("write", "read")), ioTest.clearCallList());
        assertEquals("USD;1.0812996789621254;CHF;0.9600275947678072", ioTest.lastWrite);

        ioTest.nextRead = "USD;1.1306525561227663;EUR;1.0439179372374765";
        response = currencyExchange.getConvertedAmount("chfusd", "50");
        assertEquals(((OkConversionResponse) response.getBody()).getAmount(), 56.53262, epsilon);
        assertEquals(new ArrayList<>(List.of("write", "read")), ioTest.clearCallList());
        assertEquals("USD;1.1263214566490134;EUR;1.0416367252879442", ioTest.lastWrite);
    }
}