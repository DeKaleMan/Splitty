package commons.dto;


public class PaymentDTO {

    private String payerUuid;

    private String payeeUuid;

    private int eventCode;

    private double amount;

    public PaymentDTO() {
    }

    public PaymentDTO(String payerUuid, String payeeUuid, int eventCode, double amount) {
        this.payerUuid = payerUuid;
        this.payeeUuid = payeeUuid;
        this.eventCode = eventCode;
        this.amount = amount;
    }

    public String getPayerUuid() {
        return payerUuid;
    }

    public String getPayeeUuid() {
        return payeeUuid;
    }

    public double getAmount() {
        return amount;
    }

    public int getEventCode() {
        return eventCode;
    }
}
