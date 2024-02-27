package commons;
import java.util.ArrayList;
import java.util.List;

public class Participant {
    private String name;
    private double balance;
    private String iBan;
    private String bIC;
    private String accountHolder;
    private String email;
    private List<String> expenses; // Placeholder for Expense objects

    public Participant(String name, double balance, String iBan, String bIC, String accountHolder, String email) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.accountHolder = accountHolder;
        this.email = email;
        this.expenses = new ArrayList<>();
    }

    // Getters and setters
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

    public String getIBan() {
        return iBan;
    }

    public void setIBan(String iBan) {
        this.iBan = iBan;
    }

    public String getBIC() {
        return bIC;
    }

    public void setBIC(String bIC) {
        this.bIC = bIC;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getExpenses() {
        return expenses;
    }

    public void addExpense(String expense) {
        this.expenses.add(expense);
    }
    public double calculateTotalExpenses() {
        // Placeholder implementation
        double total = 0.0;
        for (String expense : expenses) {
            try {
                total += Double.parseDouble(expense);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing expense: " + expense);
            }
        }
        return total;
    }

    public void updateBalance(double amount) {
        this.balance += amount;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", IBAN='" + iBan + '\'' +
                ", BIC='" + bIC + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", email='" + email + '\'' +
                ", expenses=" + expenses +
                '}';
    }
}
