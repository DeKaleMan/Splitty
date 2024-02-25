package commons;

/**
 * This Transaction object will store a balance CHANGE for a participant.
 * When an expense is added to the event it will hold a list of these objects to store which participant
 * gained or lost what balance.
 */
public class Transaction {
    private String participant; // The participant whose balance has changed
    private double balance; // The balance change (can be negative)

    public Transaction(String participant, double balance) {
        this.participant = participant;
        this.balance = balance;
    }

    public boolean isPositive() {
        return balance > 0;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) return false;
        Transaction that = (Transaction) o;
        return that.getParticipant().equals(this.participant) && that.getBalance() == this.balance;
    }
    @Override
    public String toString() {
        return participant + ", " + balance;
    }

}
