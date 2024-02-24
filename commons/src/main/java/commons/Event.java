package commons;

import java.util.ArrayList;
import java.util.Objects;

public class Event {
    private String name;
    private String date;
    private ArrayList<String> participantList;
    private String owner;
    private ArrayList<String> expenseList;
    private String description;

    public Event(String name, String date,
                 ArrayList<String> participantList,
                 String owner, ArrayList<String> expenseList,
                 String description) {
        this.name = name; // name of the event
        this.date = date; // date of when the event occured/was created
        this.participantList = participantList; // the list of people that have participated the event
        this.owner = owner; // the person that created the event
        this.expenseList = expenseList; // list of expenses in the event
        this.description = description; //description of the event
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getParticipantList() {
        return participantList;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getExpenseList() {
        return expenseList;
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

    public void setParticipantList(ArrayList<String> participantList) {
        this.participantList = participantList;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setExpenseList(ArrayList<String> expenseList) {
        this.expenseList = expenseList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(date, event.date) && Objects.equals(participantList, event.participantList) && Objects.equals(owner, event.owner) && Objects.equals(expenseList, event.expenseList) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, participantList, owner, expenseList, description);
    }
}
