package server.util;

public class BadConversionResponse implements ConversionResponse{

    private String message;

    public BadConversionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
