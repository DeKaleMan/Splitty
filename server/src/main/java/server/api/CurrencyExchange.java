package server.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/api/currency")
public class CurrencyExchange {

    private static final String CURRENCY_SERVER = "https://openexchangerates.org";

    // Conversion can have 6 values: eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
    @GetMapping("/{conversion}/{amount}")
    public double getConvertedAmount(@PathVariable("conversion") String conversion,
                                     @PathVariable("amount") double amount) {
        updateExchangeRates("USD");
        return 0;
    }

    /**
     * Util function to get the specified exchange rate either from cache or by contacting api
     *
     * @param conversion Can have 6 values eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
     * @return The exchange rate for the specified conversion
     */
    public double getExchangeRates(String conversion) {
        return 0;
    }

    /**
     * Method to update the exchange rates using the api
     *
     * @param baseCurrency The base currency to use for conversion
     */
    public void updateExchangeRates(String baseCurrency) {
        // Doing the http request
        String response = ClientBuilder.newClient(new ClientConfig())
                .target(CURRENCY_SERVER).path("api/latest.json")
                .queryParam("app_id", "ecc02200bf6d4b95b136ec71eca463d5")
                .queryParam("base", baseCurrency.toUpperCase())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});

        // Parsing the json response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
            JsonNode ratesNode = rootNode.get("rates");

            switch (baseCurrency) {
                case "USD" -> {
                    double usdToEurRate = ratesNode.get("EUR").asDouble();
                    double usdToChfRate = ratesNode.get("CHF").asDouble();
                    writeToFile(usdToEurRate, usdToChfRate, baseCurrency);
                }
                case "EUR" -> {
                    double eurToUsdRate = ratesNode.get("USD").asDouble();
                    double eurToChfRate = ratesNode.get("CHF").asDouble();
                    writeToFile(eurToUsdRate, eurToChfRate, baseCurrency);
                }
                case "CHF" -> {
                    double chfToUsdRate = ratesNode.get("USD").asDouble();
                    double chfToEurRate = ratesNode.get("EUR").asDouble();
                    writeToFile(chfToUsdRate, chfToEurRate, baseCurrency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    public void writeToFile(double exchangeOne, double exchangeTwo, String baseCurrency) {
        File ratesFile = new File("rates/" + baseCurrency + ".txt");
    }
}
