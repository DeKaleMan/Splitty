package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyExchange {

    // Conversion can have 6 values: eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
    @GetMapping("/{conversion}/{amount}")
    public double getConvertedAmount(@PathVariable("conversion") String conversion,
                                     @PathVariable("amount") double amount) {
        return 0;
    }

    /**
     * Util function to get the specified exchange rate either from cache or by contacting api
     *
     * @param conversion Can have 6 values eurusd, eurchf, usdeur, usdchf, chfeur, chfusd
     * @return The exchange rate for the specified conversion
     */
    public double getExhangeRates(String conversion) {
        return 0;
    }

    /**
     * Method to update the exchange rates using the api
     *
     * @param baseCurrency The base currency to use for conversion
     */
    public void updateExchangeRates(String baseCurrency) {

    }



}
