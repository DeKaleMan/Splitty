package commons;

import java.util.ArrayList;

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
        this.name = name;
        this.date = date;
        this.participantList = participantList;
        this.owner = owner;
        this.expenseList = expenseList;
        this.description = description;
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
}
