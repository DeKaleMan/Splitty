package server;

import server.Currency;
import java.util.List;
import java.util.Objects;

public class Expense {
    private String description;
    private List<Transaction> transactions;
    // all associated participants of the expense and how much they owe or are owed

    private Type type; // type of expense (i.e. food, drinks, travel)
    private Currency currency;
    private String date; // date of expense
    private double totalExpense; // the amount of money of the expense
    private String payer; // the participant who paid

    public Expense(String description, List<Transaction> transactions,
                   Type type, Currency currency, String date, double totalExpense, String payer) {
        this.description = description;
        this.transactions = transactions;
        this.type = type;
        this.currency = currency;
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

    public List<Transaction> getTransactions() {
        return transactions;
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

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(totalExpense, expense.totalExpense) == 0
                && Objects.equals(description, expense.description)
                && Objects.equals(transactions, expense.transactions)
                && type == expense.type && currency == expense.currency
                && Objects.equals(date, expense.date)
                && Objects.equals(payer, expense.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, transactions, type, currency, date, totalExpense, payer);
    }

    @Override
    public String toString() {
        String res = "This is an expense:\n" + description + "\nThe expense type is: " + this.type
                + ".\nThe total amount spent is: " + totalExpense + ".\nThis is how much everyone owes:\n";

        for (Transaction t : transactions) {
            res += "\t" + t.getParticipant() + ": " + t.getBalance() + ".\n";
        }
        res += "The person who paid was: " + payer + ", on " + date + " and paid in " + currency + ".";
        return res;
    }

}
