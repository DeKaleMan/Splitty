package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    private String name;
    private String date;
    private List<String> participantList;
    private String owner;
    private List<String> expenseList;
    private String description;

    public Event(String name, String date,
                 List<String> participantList,
                 String owner, List<String> expenseList,
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

    public List<String> getParticipantList() {
        return participantList;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getExpenseList() {
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

    @Override
    public String toString() {


        String part = "";
        for(int i = 0; i < participantList.size()-1; i++){
            part = part + participantList.get(i) + ", ";
        }
        part = part + participantList.get(participantList.size()-1);

        String exp = "";
        for(int i = 0; i < expenseList.size()-1; i++){
            exp = exp + expenseList.get(i) + ", ";
        }
        exp = exp + expenseList.get(expenseList.size()-1);
        String res = "This is the "
                + this.name
                + " event. That is summarized as: "
                + description
                + ". It was created on "
                + this.date
                + ". The participants are: "
                + part
                + ". "
                + owner
                + " is the person that created the event. The expenses in the list are: "
                + exp
                + "." ;

        return res;
    }
}
