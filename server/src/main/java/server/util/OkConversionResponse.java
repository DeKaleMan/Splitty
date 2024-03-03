package server.util;

public class OkConversionResponse implements ConversionResponse{

    private double amount;
    private String message;

    public OkConversionResponse(double amount, String message) {
        this.amount = amount;
        this.message = message;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
