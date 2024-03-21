package commons.dto;


public class PaymentDTO {

    private String payerUuid;

    private String payeeUuid;

    private int eventCode;

    private double amount;

    private boolean paid;

    public PaymentDTO() {
    }

    public PaymentDTO(String payerUuid, String payeeUuid, int eventCode, double amount, boolean paid) {
        this.payerUuid = payerUuid;
        this.payeeUuid = payeeUuid;
        this.eventCode = eventCode;
        this.amount = amount;
        this.paid = paid;
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

    public boolean isPaid() {
        return paid;
    }
}
