package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.Objects;
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String name;
    private Date date;
    private String host;
    private String description;
    private Date lastActivity;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Expense> expenseList;

    @JsonIgnore
    @OneToMany(mappedBy = "id.event", cascade = CascadeType.REMOVE)
    private List<Participant> participantList;

    @Column(unique = true)
    private String inviteCode;
    public Event(String name, Date date,
                 String host,
                 String description) {
        this.name = name; // name of the event
        this.date = date; // date of when the event occured/was created
        this.host = host; // the person that created the event
        this.description = description;//description of the event
        lastActivity = new Date();
    }

    public Event() {

    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getHost() {
        return host;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOwner(String host) {
        this.host = host;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void updateActivityDate() {
        lastActivity = new Date();
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id
                && Objects.equals(name, event.name)
                && Objects.equals(date, event.date)
                && Objects.equals(host, event.host)
                && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, host, description);
    }



    @Override
    public String toString() {
        String res = "This is event with name: "
                + name
                + " that is created on "
                + date
                + " the person that created is: "
                + host
                + " the description is: "
                + description;
        return res;
    }
}
