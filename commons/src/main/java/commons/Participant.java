package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Participant implements Serializable {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private double balance;

    @Column(name = "iban", length = 34)
    private String iBan;

    @Column(name = "bic", length = 11)
    private String bIC;

    private String email;

    private String accountHolder;

    @Column(nullable = false)
    private boolean ghost;

    @JsonIgnore
    @OneToMany(mappedBy = "payer", cascade = CascadeType.REMOVE)
    private List<Payment> paymentPayerList;

    @JsonIgnore
    @OneToMany(mappedBy = "payee", cascade = CascadeType.REMOVE)
    private List<Payment> paymentPayeeList;


    @EmbeddedId
    private ParticipantId id;

    protected Participant() {
        id = new ParticipantId();
    }

    public Participant(String name, double balance, String iBan, String bIC,
                       String email, String accountHolder, String uuid, Event event) {
        this.name = name;
        this.balance = balance;
        this.iBan = iBan;
        this.bIC = bIC;
        this.email = email;
        this.ghost = false;
        this.id = new ParticipantId(uuid, event);
        this.accountHolder = accountHolder;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEvent(Event event){
        id.setEvent(event);
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Event getEvent(){
        return id.getEvent();
    }

    public String getUuid(){
        return id.getUuid();
    }

    public void setUuid(String uuid){
        id.setUuid(uuid);
    }

    public ParticipantId getId() {
        return id;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
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

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", iBan='" + iBan + '\'' +
                ", bIC='" + bIC + '\'' +
                ", email='" + email + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", isGhost=" + ghost +
                ", id=" + id +
                '}';
    }
}
