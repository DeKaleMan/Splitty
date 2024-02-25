package server.util;

public class ConversionResponse {

    private double responseAmount;
    private String responseMessage;

    public ConversionResponse(double responseAmount, String responseMessage) {
        this.responseAmount = responseAmount;
        this.responseMessage = responseMessage;
    }

    public double getResponseAmount() {
        return responseAmount;
    }

    public void setResponseAmount(double responseAmount) {
        this.responseAmount = responseAmount;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
