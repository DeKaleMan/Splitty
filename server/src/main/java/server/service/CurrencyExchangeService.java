package server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.api.depinjectionUtils.HttpResponse;
import server.api.depinjectionUtils.ServerIOUtil;

import java.io.File;
import java.util.List;

@Service
public class CurrencyExchangeService {

    private final ServerIOUtil io;
    private final HttpResponse httpResponse;

    @Autowired
    public CurrencyExchangeService(ServerIOUtil io, HttpResponse httpResponse) {
        this.io = io;
        this.httpResponse = httpResponse;
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
