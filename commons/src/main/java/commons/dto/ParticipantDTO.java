package commons.dto;

public class ParticipantDTO {
    private String name;

    private double balance;

    private String iBan;

    private String bIC;

    private String email;

    private String accountHolder;

    private String eventInviteCode;
    private int eventId;
    private boolean isGhost;
    private String uuid;

    public ParticipantDTO() {
    }

    public ParticipantDTO(String name, double balance, String iBan, String bIC,
                          String email, String accountHolder, int eventId, String uuid) {
        // DELETE THIS CONSTRUCTOR
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.email = email;
        this.accountHolder = accountHolder;
        this.eventId = eventId;
        this.uuid = uuid;
        this.isGhost = false;
    }
    public ParticipantDTO(String name, double balance, String iBan, String bIC,
                          String email, String accountHolder, int eventId, String uuid, String eventInviteCode) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.email = email;
        this.accountHolder = accountHolder;
        this.eventId = eventId;
        this.uuid = uuid;
        this.eventInviteCode = eventInviteCode;
        this.isGhost = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getiBan() {
        return iBan;
    }

    public void setiBan(String iBan) {
        this.iBan = iBan;
    }

    public String getbIC() {
        return bIC;
    }

    public void setbIC(String bIC) {
        this.bIC = bIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEventInviteCode() {
        return eventInviteCode;
    }

    public void setEventInviteCode(String eventInviteCode) {
        this.eventInviteCode = eventInviteCode;
    }

    public boolean isGhost() {
        return isGhost;
    }

    public void setGhostStatus(boolean ghost) {
        this.isGhost = ghost;
    }
}
