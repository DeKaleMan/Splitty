package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.CurrencyExchangeService;
import server.util.BadConversionResponse;
import commons.Conversion;
import server.util.ConversionResponse;
import server.util.OkConversionResponse;

import java.util.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyExchange {

    CurrencyExchangeService currencyExchangeService;

    @Autowired
    public CurrencyExchange(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
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

        Conversion conversionObject = currencyExchangeService.getExchangeRates(from, to, date);
        if (conversionObject == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BadConversionResponse(
                    "Something unexpected happened on the server side"
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new OkConversionResponse(conversionObject,
                "The conversion was successful"));
    }
}