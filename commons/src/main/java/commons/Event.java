package commons;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String name;
    private Date date;
    private String owner;
    private String description;

    public Event(String name, Date date,
                 String owner,
                 String description) {
        this.name = name; // name of the event
        this.date = date; // date of when the event occured/was created
        this.owner = owner; // the person that created the event
        this.description = description; //description of the event
    }

    public Event() {

    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getOwner() {
        return owner;
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

    public void setOwner(String owner) {
        this.owner = owner;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id
                && Objects.equals(name, event.name)
                && Objects.equals(date, event.date)
                && Objects.equals(owner, event.owner)
                && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, owner, description);
    }



    @Override
    public String toString() {
        String res = "This is event with name: "
                + name
                + " that is created on "
                + date
                + " the person that created is: "
                + owner
                + " the description is: "
                + description;
        return res;
    }
}
