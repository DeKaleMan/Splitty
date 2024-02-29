package server;

import commons.Event;

import java.util.ArrayList;
import java.util.List;

public class UserOverview {

    //private final EventRepository repo;
    private String name;
    private String email;
    private List<Event> events;
    public UserOverview(String name, String email){
        this.name = name;
        this.email = email;
        this.events = new ArrayList<>(); //add all events here
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Event> getEvents() {
        return events;
    }

    public Event createEvent(String name, String date, List<String> participants,
                             String owner, List<String> expenseList, String description){
        //construct new event
        Event event = new Event(name, date, participants, owner, expenseList, description);
        events.add(event);
        return event;
    }

    public boolean changeTitleEvent(Event event, String title){
        if(event == null || title == null) return false;
        if(!events.contains(event)) return false;
        event.setName(title);
        //return event.setTitle(title);
        return true;
    }
//    public List<String> getExpenses(Event event){
//        List<String> res = event.getExpenseList();
//        return res;
//    }
}
