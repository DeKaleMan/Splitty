package commons;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * This Transaction object will store a balance CHANGE for a participant.
 * When an expense is added to the event it will hold a list of these objects to store which participant
 * gained or lost what balance.
 */
@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;


    @Column(nullable = false)
    private double balance; // The balance change (can be negative)

    @PrimaryKeyJoinColumn
    @ManyToOne
    private Expense expense; //is the expense that is added to the event

    @PrimaryKeyJoinColumn
    @ManyToOne
    private Participant participant;

    public Debt(){

    }

    public Debt(Expense expense, double balance, Participant participant){
        this.expense = expense;
        this.balance = balance;
        this.participant = participant;
    }


    public double getBalance() {
        return balance;
    }

    public Expense getExpense(){
        return expense;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt that = (Debt) o;
        return id == that.id && Double.compare(balance, that.balance) == 0
                && Objects.equals(expense, that.expense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, expense);
    }
}
