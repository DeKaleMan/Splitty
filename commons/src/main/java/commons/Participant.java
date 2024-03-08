package commons;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

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

    @EmbeddedId
    private ParticipantId id;

    protected Participant() {}
    public Participant(String name, double balance
            , String iBan, String bIC
            , String accountHolder, String email,
                       Event event) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.accountHolder = accountHolder;
        this.id = new ParticipantId(email, event);
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
        return id.getEmail();
    }

    public void setEmail(String email) {
        id.setEmail(email);
    }

    public void setEvent(Event event){
        id.setEvent(event);
    }

    public Event getEvent(){
        return id.getEvent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
