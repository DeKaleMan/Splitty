package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String name;
    private String date;
    private String owner;
    private String description;

    public Event(String name, String date,
                 List<String> participantList,
                 String owner, List<String> expenseList,
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

    public String getDate() {
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Objects.equals(name, event.name) && Objects.equals(date, event.date) && Objects.equals(owner, event.owner) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, owner, description);
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Event event = (Event) o;
//        return Objects.equals(name, event.name)
//                && Objects.equals(date, event.date)
//                && Objects.equals(participantList, event.participantList)
//                && Objects.equals(owner, event.owner)
//                && Objects.equals(expenseList, event.expenseList)
//                && Objects.equals(description, event.description);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(name, date, participantList, owner, expenseList, description);
//    }

//    @Override
//    public String toString() {
//
//
//        String part = "";
//        for(int i = 0; i < participantList.size()-1; i++){
//            part = part + participantList.get(i) + ", ";
//        }
//        part = part + participantList.get(participantList.size()-1);
//
//        String exp = "";
//        for(int i = 0; i < expenseList.size()-1; i++){
//            exp = exp + expenseList.get(i) + ", ";
//        }
//        exp = exp + expenseList.get(expenseList.size()-1);
//        String res = "This is the "
//                + this.name
//                + " event.\n That is summarized as: "
//                + description
//                + ".\n It was created on "
//                + this.date
//                + ".\n The participants are: "
//                + part
//                + ".\n "
//                + owner
//                + " is the person that created the event.\n The expenses in the list are: "
//                + exp
//                + "." ;
//
//        return res;
//    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", owner='" + owner + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
