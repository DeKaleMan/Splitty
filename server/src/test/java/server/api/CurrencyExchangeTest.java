package server.api;

import commons.Conversion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.testmocks.HttpResponseTest;
import server.api.testmocks.ServerIOUtilsTest;
import server.util.ConversionResponse;
import server.util.OkConversionResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeTest {

    private CurrencyExchange currencyExchange;
    private ServerIOUtilsTest ioTest;
    private HttpResponseTest httpResponseTest;
    private List<Conversion> conversionCacheList = new ArrayList<>(List.of(
            new Conversion("USD", "EUR", 0.923288, "30-03-2024"),
            new Conversion("EUR", "USD", 1.083229975139872, "30-03-2024"),
            new Conversion("CHF", "USD", 1.1306525561227663, "30-03-2024")));

    @BeforeEach
    void initialize() {
        ioTest = new ServerIOUtilsTest();
        httpResponseTest = new HttpResponseTest();
        currencyExchange = new CurrencyExchange(ioTest, httpResponseTest);
    }

    @Test
    void invalidDateTest() {
        ResponseEntity<ConversionResponse> response = currencyExchange.getConversion("EUR", "USD", "20r-03-20e3");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void invalidConversionTest() {
        ResponseEntity<ConversionResponse> response = currencyExchange.getConversion("EURs", "USDg", "20-03-2024");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getConvertedAmountCachedTest() {
        // epsilon is the required precision for the assertEquals to pass
        double epsilon = 0.00001d;

        // tests if the amounts are calculated correctly if the rate is cached
        ioTest.nextConversionRead = conversionCacheList;
        ResponseEntity<ConversionResponse> response = currencyExchange.getConversion("USD", "EUR", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(), 0.923288, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion")), ioTest.clearCallList());

        ioTest.nextConversionRead = conversionCacheList;
        response = currencyExchange.getConversion("EUR", "USD", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(),
                1.083229975139872, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion")), ioTest.clearCallList());

        ioTest.nextConversionRead = conversionCacheList;
        response = currencyExchange.getConversion("CHF", "USD", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(),
                1.1306525561227663, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion")), ioTest.clearCallList());
    }

    @Test
    void getConvertedAmountNotCachedTest() {
        // epsilon is the required precision for the assertEquals to pass
        double epsilon = 0.00001d;

        // tests if the amounts are calculated correctly if the rate is cached
        ioTest.nextConversionRead = new ArrayList<>();
        ResponseEntity<ConversionResponse> response = currencyExchange.getConversion("USD", "EUR", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(), 0.924813, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion", "readConversion", "writeConversion", "readConversion")),
                ioTest.clearCallList());

        ioTest.nextConversionRead = new ArrayList<>();
        response = currencyExchange.getConversion("EUR", "USD", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(),
                1.0812996789621254, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion", "readConversion", "writeConversion", "readConversion")),
                ioTest.clearCallList());

        ioTest.nextConversionRead = new ArrayList<>();
        response = currencyExchange.getConversion("CHF", "USD", "30-03-2024");
        assertEquals(((OkConversionResponse) response.getBody()).getConversion().conversionRate(),
                1.1263214566490134, epsilon);
        assertEquals(new ArrayList<>(List.of("readConversion", "readConversion", "writeConversion", "readConversion")),
                ioTest.clearCallList());
    }
}