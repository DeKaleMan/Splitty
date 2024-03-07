package commons.dto;


public class PaymentDTO {

    private String payerEmail;

    private String payeeEmail;

    private int eventCode;

    private double amount;

    public PaymentDTO() {
    }

    public PaymentDTO(String payerEmail, String payeeEmail, int eventCode, double amount) {
        this.payerEmail = payerEmail;
        this.payeeEmail = payeeEmail;
        this.eventCode = eventCode;
        this.amount = amount;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public double getAmount() {
        return amount;
    }

    public int getEventCode() {
            return eventCode;
        }
}
