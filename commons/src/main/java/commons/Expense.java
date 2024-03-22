package commons;

import jakarta.persistence.*;
import java.util.Date;

import java.util.Objects;

@Entity
@IdClass(ExpenseId.class)
public class Expense {
    @Id
    @PrimaryKeyJoinColumn
    @ManyToOne
    private Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int expenseId;
    @Column(nullable = false)
    private String description;
    // all associated participants of the expense and how much they owe or are owed

    @Enumerated(EnumType.STRING)
    private Type type; // type of expense (i.e. food, drinks, travel)

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date; // date of expense
    @Column(nullable = false)
    private double totalExpense; // the amount of money of the expense
    @PrimaryKeyJoinColumn
    @ManyToOne
    private Participant payer; // the participant who paid



    public Expense() {

    }

    public Expense(Event event, String description, Type type, Date date,
                   double totalExpense, Participant payer) {
        this.event = event;
        this.description = description;
        this.type = type;
        this.date = date;
        this.totalExpense = totalExpense;
        this.payer = payer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Participant getPayer() {
        return payer;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public Event getEvent() {
        return event;
    }

    public int getExpenseId() {
        return expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return expenseId == expense.expenseId &&
            Double.compare(totalExpense, expense.totalExpense) == 0 &&
            Objects.equals(event, expense.event) &&
            Objects.equals(description, expense.description) && type == expense.type &&
            Objects.equals(date, expense.date) &&
            Objects.equals(payer, expense.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, expenseId, description, type, date, totalExpense, payer);
    }

    @Override
    public String toString() {
        return "This is an expense:\n" + description + "\nThe expense type is: " + this.type
            + ".\nThe total amount spent is: "
            + totalExpense + "."
            + "\nThe person who paid was: " + payer.getUuid() + ", on " + date
            + ".";

    }

}
