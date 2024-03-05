package commons;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * This Transaction object will store a balance CHANGE for a participant.
 * When an expense is added to the event it will hold a list of these objects to store which participant
 * gained or lost what balance.
 */
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private double balance; // The balance change (can be negative)

    @ManyToMany(mappedBy = "")
    private Expense ExpAddEvent; //is the expense that is added to the event

    public Transaction(){

    }

    public Transaction(Expense expAddEvent, double balance){
        this.ExpAddEvent = expAddEvent;
        this.balance = balance;
    }


    public boolean isPositive() {
        return balance > 0;
    }

    public double getBalance() {
        return balance;
    }

    public Expense getExpAddEvent(){
        return ExpAddEvent;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Double.compare(balance, that.balance) == 0 && Objects.equals(ExpAddEvent, that.ExpAddEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, ExpAddEvent);
    }
}
