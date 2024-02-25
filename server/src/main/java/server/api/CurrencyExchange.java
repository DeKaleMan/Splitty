package server.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.util.ConversionResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/api/currency")
public class CurrencyExchange {

    private static final String CURRENCY_SERVER = "https://openexchangerates.org";

    // Conversion can have 6 values: eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
    @GetMapping("/{conversion}/{amount}")
    public ResponseEntity<ConversionResponse> getConvertedAmount(@PathVariable("conversion") String conversion,
                                       @PathVariable("amount") String stringAmount) {
        // Check if the provided amount is a valid double
        double doubleAmount;
        try {
            doubleAmount = Double.parseDouble(stringAmount);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ConversionResponse(0,
                    "You typed in something else than a double. Please provide a valid double"));
        }

        // Check for an invalid conversion
        if (!Arrays.asList("eurusd", "eurchf", "usdeur", "usdchf", "chfeur", "chfusd")
                .contains(conversion)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ConversionResponse(0,
                    "You did not provide a supported conversion. " +
                            "The supported conversions are: eurusd, eurchf, usdeur, usdchf, chfeur, chfusd"));
        }

        double exchangeRate = getExchangeRates(conversion);
        return ResponseEntity.status(HttpStatus.OK).body(new ConversionResponse(exchangeRate * doubleAmount,
                "The conversion was successful"));
    }

    /**
     * Util method to get the specified exchange rate either from cache or by contacting api
     *
     * @param conversion Can have 6 values eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
     * @return The exchange rate for the specified conversion
     */
    public double getExchangeRates(String conversion) {
        String baseCurrency = conversion.substring(0, 3).toUpperCase();
        String conversionCurrency = conversion.substring(3, 6).toUpperCase();
        File file = new File(getFileName(baseCurrency));

        // If a file (from today) is not cached it will update the conversion rates
        if (!file.exists()) {
            updateExchangeRates(baseCurrency);
        }

        // Read the file and extract the right conversion rate
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(";");

            // Creates a mapping from currency --> conversion rate
            // (See the files in the rates folder for the file structure)
            HashMap<String, Double> conversionMap = new HashMap<>();
            conversionMap.put(scanner.next(), Double.parseDouble(scanner.next()));
            conversionMap.put(scanner.next(), Double.parseDouble(scanner.next()));
            scanner.close();

            return conversionMap.get(conversionCurrency);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Util method to update the exchange rates using the api
     *
     * @param baseCurrency The base currency to use for conversion
     */
    public void updateExchangeRates(String baseCurrency) {
        // Doing the http request
        String response = ClientBuilder.newClient(new ClientConfig())
                .target(CURRENCY_SERVER).path("api/latest.json")
                .queryParam("app_id", "ecc02200bf6d4b95b136ec71eca463d5")
                .queryParam("base", "USD")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});

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
                    writeToFile(usdToEurRate, usdToChfRate, "EUR", "CHF", baseCurrency);
                }
                case "EUR" -> {
                    double eurToUsdRate = 1/(ratesNode.get("EUR").asDouble());
                    double eurToChfRate = (ratesNode.get("CHF").asDouble())/(ratesNode.get("EUR").asDouble());
                    writeToFile(eurToUsdRate, eurToChfRate, "USD", "CHF", baseCurrency);
                }
                case "CHF" -> {
                    double chfToUsdRate = 1/(ratesNode.get("CHF").asDouble());
                    double chfToEurRate = (ratesNode.get("EUR").asDouble())/(ratesNode.get("CHF").asDouble());
                    writeToFile(chfToUsdRate, chfToEurRate, "USD", "EUR", baseCurrency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Util method that writes the exchange rates to a file for caching
     *
     * @param exchangeOne The first exchange rate to be written to the file
     * @param exchangeTwo The second exchange rate to be written to the file
     * @param baseCurrency The base currency
     */
    public void writeToFile(double exchangeOne, double exchangeTwo,
                            String exchangeOneName, String exchangeTwoName ,String baseCurrency) {
        try {
            File ratesFile = new File(getFileName(baseCurrency));
            ratesFile.createNewFile();
            FileWriter fileWriter = new FileWriter(ratesFile);
            fileWriter.write(exchangeOneName + ";" + exchangeOne + ";" + exchangeTwoName + ";" + exchangeTwo);
            fileWriter.close();
            System.out.println("Wrote to file: " + ratesFile.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Util method to get the file name we use for caching based on the date and base currency
     *
     * @param baseCurrency The base currency
     * @return The file name
     */
    public String getFileName(String baseCurrency) {
        // Method makes sure that the file name will always be the right name
        // (removes the need for copying and pasting the file name)
        return "src/main/resources/rates/" + baseCurrency + ";" + LocalDate.now() + ".txt";
    }
}