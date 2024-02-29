package commons;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@IdClass(ExpenseId.class)
public class Expense {
    @Id
    private int eventCode;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int expenseId;
    private String description;
    // all associated participants of the expense and how much they owe or are owed

    private Type type; // type of expense (i.e. food, drinks, travel)
    private Currency currency;
    private String date; // date of expense
    private double totalExpense; // the amount of money of the expense
    private String payerEmail; // the participant who paid

    public Expense(String description, Type type, Currency currency,
                   String date, double totalExpense, String payerEmail) {
        this.description = description;
        this.type = type;
        this.currency = currency;
        this.date = date;
        this.totalExpense = totalExpense;
        this.payerEmail = payerEmail;
    }

    public Expense() {

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

    public Currency getCurrency() {
        return currency;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payer) {
        this.payerEmail = payer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return eventCode == expense.eventCode && expenseId == expense.expenseId &&
            Double.compare(totalExpense, expense.totalExpense) == 0 &&
            Objects.equals(description, expense.description) && type == expense.type &&
            currency == expense.currency && Objects.equals(date, expense.date) &&
            Objects.equals(payerEmail, expense.payerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCode, expenseId, description, type, currency, date, totalExpense,
            payerEmail);
    }

    @Override
    public String toString() {
        return "This is an expense:\n" + description + "\nThe expense type is: " + this.type
            + ".\nThe total amount spent is: "
            + totalExpense + "."
            + "\nThe person who paid was: " + payerEmail + ", on " + date + " and paid in "
            + currency
            + ".";

    }

}
