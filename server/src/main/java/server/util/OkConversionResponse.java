package server.util;

import commons.Conversion;

public class OkConversionResponse implements ConversionResponse{

    private Conversion conversion;
    private String message;

    public OkConversionResponse(Conversion conversion, String message) {
        this.conversion = conversion;
        this.message = message;
    }

    public Conversion getAmount() {
        return conversion;
    }

    public void setAmount(Conversion conversion) {
        this.conversion = conversion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
