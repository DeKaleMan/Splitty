package commons.dto;

import java.util.Objects;

/**
 * This Transaction object will store a balance CHANGE for a participant.
 * When an expense is added to the event it will hold a list of these objects to store which participant
 * gained or lost what balance.
 */

public class DebtDTO {


    private double balance; // The balance change (can be negative)

    private int eventId;

    private int expenseId; //is the expense that is added to the event


    private String participantUuid;

    public DebtDTO() {

    }

    public DebtDTO(double balance, int eventId, int expenseId, String participantUuid) {
        this.balance = balance;
        this.eventId = eventId;
        this.expenseId = expenseId;
        this.participantUuid = participantUuid;
    }

    public double getBalance() {
        return balance;
    }

    public int getEventId() {
        return eventId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public String getParticipantUuid() {
        return participantUuid;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebtDTO debtDTO = (DebtDTO) o;
        return Double.compare(balance, debtDTO.balance) == 0 && eventId == debtDTO.eventId &&
                expenseId == debtDTO.expenseId &&
                Objects.equals(participantUuid, debtDTO.participantUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, eventId, expenseId, participantUuid);
    }
}

