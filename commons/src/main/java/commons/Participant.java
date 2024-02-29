package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.io.Serializable;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;

@Entity
public class Participant implements Serializable {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private double balance;

    @Column(name = "iban", nullable = false, unique = true, length = 34)
    private String iBan;

    @Column(name = "bic", nullable = false, length = 11)
    private String bIC;

    @Column(name = "account_holder", nullable = false, length = 100)
    private String accountHolder;

    @Id
    @Column(nullable = false, unique = true)
    private String email;

//    @ManyToOne
//    @JoinColumn(name = "????") // This is the foreign key column in the Participant table.
//    private Event event;

    protected Participant() {}
    public Participant(String name, double balance, String iBan, String bIC, String accountHolder, String email) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.accountHolder = accountHolder;
        this.email = email;
//        this.event = event;
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

}
