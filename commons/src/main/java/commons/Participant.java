package commons;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

    @OneToMany(mappedBy = "participant")
    private List<Participant> ower; // the person that participated in the event, but didn't pay for the event so he needs to pay them back


    protected Participant() {}
    public Participant(String name, double balance, String iBan, String bIC, String accountHolder, String email) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.accountHolder = accountHolder;
        this.email = email;
        this.ower = ower;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
