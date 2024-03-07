package commons.dto;

public class ParticipantDTO {
    private String name;

    private double balance;

    private String iBan;

    private String bIC;

    private String accountHolder;

    private String email;
    private int eventId;

    public ParticipantDTO() {
    }
    public ParticipantDTO(String name, double balance, String iBan, String bIC, String accountHolder, String email, int eventId) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.accountHolder = accountHolder;
        this.email = email;
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getiBan() {
        return iBan;
    }

    public String getbIC() {
        return bIC;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getEmail() {
        return email;
    }

    public int getEventId() {
        return eventId;
    }
}
