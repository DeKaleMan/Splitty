package server.util;

import java.util.Objects;

public class ConversionResponse {

    private double amount;
    private String message;

    public ConversionResponse(double amount, String message) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionResponse that = (ConversionResponse) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, message);
    }
}
