package server.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.depinjectionUtils.HttpResponse;
import server.api.depinjectionUtils.ServerIOUtil;
import server.util.BadConversionResponse;
import commons.Conversion;
import server.util.ConversionResponse;
import server.util.OkConversionResponse;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyExchange {

    private final ServerIOUtil io;
    private final HttpResponse httpResponse;

    @Autowired
    public CurrencyExchange(ServerIOUtil io, HttpResponse httpResponse) {
        this.io = io;
        this.httpResponse = httpResponse;
    }

    // 6 conversions are supported: eur->usd, eur->chf, usd->eur, usd->chf, chf->eur, chf->usd
    @GetMapping({"", "/"})
    public ResponseEntity<ConversionResponse> getConversion(@RequestParam("from") String from,
                                                                 @RequestParam("to") String to,
                                                                 @RequestParam("date") String date) {
                                                                // date has to be formatted dd-mm-yyyy
        // Check for a valid date
        String[] dateArray = date.split("-");
        try {
            Integer.parseInt(dateArray[0]);
            Integer.parseInt(dateArray[1]);
            Integer.parseInt(dateArray[2]);
            if (dateArray.length != 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadConversionResponse(
                        "You typed in a wrong date. Please provide a valid date (dd-mm-yyyy)"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadConversionResponse(
                    "You typed in a wrong date. Please provide a valid date (dd-mm-yyyy)"));
        }

        // Check for an invalid conversion
        if (!Arrays.asList("EUR", "CHF", "USD")
                .contains(from) || !Arrays.asList("EUR", "CHF", "USD")
                .contains(to)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadConversionResponse(
                    "You did not provide a supported conversion. " +
                            "The supported conversions are: " +
                            "eur->usd, eur->chf, usd->eur, usd->chf, chf->eur, chf->usd"));
        }

        Conversion conversionObject = getExchangeRates(from, to, date);
        if (conversionObject == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BadConversionResponse(
                    "Something unexpected happened on the server side"
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new OkConversionResponse(conversionObject,
                "The conversion was successful"));
    }

    /**
     * Util method to get the specified exchange rate either from cache or by contacting api
     *
     * @param baseCurrency Can have 3 values eur, usd, chf
     * @param conversionCurrency Can have 3 values eur, usd, chf
     * @return The conversion object for the specified conversion
     */
    public Conversion getExchangeRates(String baseCurrency, String conversionCurrency, String date) {
        String[] dateArray = date.split("-");

        // If a file (from today) is not cached it will update the conversion rates
        Conversion possibleCacheConversion = checkForCachedConversion(date, baseCurrency, conversionCurrency);
        if (possibleCacheConversion == null) {
            updateExchangeRates(baseCurrency, date);
            possibleCacheConversion = checkForCachedConversion(date, baseCurrency, conversionCurrency);
        }

        return possibleCacheConversion;
    }

    /**
     * Util method to update the exchange rates using the api
     *
     * @param baseCurrency The base currency to use for conversion
     */
    public void updateExchangeRates(String baseCurrency, String date) {
        // Doing the http request
        String[] dateArray = date.split("-");
        String response = httpResponse.getExchangeRateResponse(dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0]);

        // Parsing the json response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
            JsonNode ratesNode = rootNode.get("rates");

            // I had to do some weird calculations because the api doesn't allow me
            // to change the base currency of the request
            switch (baseCurrency) {
                case "USD" -> {
                    double usdToEurRate = ratesNode.get("EUR").asDouble();
                    double usdToChfRate = ratesNode.get("CHF").asDouble();
                    writeCurrencyCache(List.of(new Conversion("USD", "EUR", usdToEurRate, date),
                            new Conversion("USD", "CHF", usdToChfRate, date)));
                }
                case "EUR" -> {
                    double eurToUsdRate = 1/(ratesNode.get("EUR").asDouble());
                    double eurToChfRate = (ratesNode.get("CHF").asDouble())/(ratesNode.get("EUR").asDouble());
                    writeCurrencyCache(List.of(new Conversion("EUR", "USD", eurToUsdRate, date),
                            new Conversion("EUR", "CHF", eurToChfRate, date)));
                }
                case "CHF" -> {
                    double chfToUsdRate = 1/(ratesNode.get("CHF").asDouble());
                    double chfToEurRate = (ratesNode.get("EUR").asDouble())/(ratesNode.get("CHF").asDouble());
                    writeCurrencyCache(List.of(new Conversion("CHF", "USD", chfToUsdRate, date),
                            new Conversion("CHF", "EUR", chfToEurRate, date)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Util method that writes the exchange rates to a file for caching
     */
    public void writeCurrencyCache(List<Conversion> conversionsToAdd) {
        List<Conversion> newCacheList = getConversionCache();
        newCacheList.addAll(conversionsToAdd);
        io.writeConversionObjects(new File(io.getCurrencyCacheFile()), newCacheList);
    }

    public List<Conversion> getConversionCache() {
        return io.readConversionObjects(new File(io.getCurrencyCacheFile()));
    }

    public Conversion checkForCachedConversion(String date, String baseCurrency, String conversionCurrency) {
        for (Conversion conversion : getConversionCache()){
            if (date.equals(conversion.date())
                    && conversion.from().equals(baseCurrency) && conversion.to().equals(conversionCurrency)) {
                return conversion;
            }
        }
        return null;
    }
}